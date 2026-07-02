package com.example.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import java.io.File
import java.io.FileOutputStream

object SoundManager {
    private var soundPool: SoundPool? = null
    private var popSoundId: Int = 0
    private var bgmSoundId: Int = 0
    private var isLoaded = false
    
    private var bgmStreamId: Int = 0
    val isBgmEnabled = androidx.compose.runtime.mutableStateOf(false)

    fun init(context: Context) {
        if (soundPool != null) return

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .build()

        soundPool?.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                isLoaded = true
                if (sampleId == bgmSoundId && isBgmEnabled.value) {
                    playBgm()
                }
            }
        }

        try {
            val wavFile = createWavFile(context)
            popSoundId = soundPool?.load(wavFile.absolutePath, 1) ?: 0
            
            val bgmFile = createBgmWavFile(context)
            bgmSoundId = soundPool?.load(bgmFile.absolutePath, 1) ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playPop() {
        if (isLoaded) {
            soundPool?.play(popSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun toggleBgm() {
        isBgmEnabled.value = !isBgmEnabled.value
        if (isBgmEnabled.value) {
            playBgm()
        } else {
            stopBgm()
        }
    }

    fun playBgm() {
        if (isLoaded && isBgmEnabled.value && bgmStreamId == 0) {
            bgmStreamId = soundPool?.play(bgmSoundId, 0.4f, 0.4f, 0, -1, 1f) ?: 0
        }
    }

    fun stopBgm() {
        if (bgmStreamId != 0) {
            soundPool?.stop(bgmStreamId)
            bgmStreamId = 0
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        isLoaded = false
        bgmStreamId = 0
    }

    private fun createWavFile(context: Context): File {
        val sampleRate = 44100
        val duration = 0.08 // seconds
        val numSamples = (duration * sampleRate).toInt()
        val generatedSnd = ByteArray(2 * numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            // Simple pop: sweeping down frequency + rapid decay
            val phase = 2.0 * Math.PI * (200.0 * t - 10.0 * Math.exp(-40.0 * t))
            val envelope = Math.exp(-45.0 * t)
            val dVal = Math.sin(phase) * envelope

            val valShort = (dVal * 32767).toInt().toShort()
            generatedSnd[i * 2] = (valShort.toInt() and 0x00ff).toByte()
            generatedSnd[i * 2 + 1] = ((valShort.toInt() and 0xff00) ushr 8).toByte()
        }

        val file = File(context.cacheDir, "pop.wav")
        writeWavFile(file, sampleRate, generatedSnd)
        return file
    }

    private fun createBgmWavFile(context: Context): File {
        val sampleRate = 44100
        // Ambient, minimalist, peaceful/uplifting melody
        val notes = listOf(
            Pair(261.63, 2.0), // C4
            Pair(329.63, 2.0), // E4
            Pair(392.00, 2.0), // G4
            Pair(349.23, 2.0), // F4
            Pair(440.00, 2.0), // A4
            Pair(392.00, 2.0), // G4
            Pair(261.63, 4.0)  // C4
        )
        
        val totalDuration = notes.sumOf { it.second }
        val numSamples = (totalDuration * sampleRate).toInt()
        val generatedSnd = ByteArray(2 * numSamples)
        
        var currentSample = 0
        for ((freq, duration) in notes) {
            val noteSamples = (duration * sampleRate).toInt()
            for (i in 0 until noteSamples) {
                if (currentSample >= numSamples) break
                val tNote = i.toDouble() / sampleRate
                val tTotal = currentSample.toDouble() / sampleRate
                
                // Soft piano-like envelope: slow attack, long decay
                val attack = if (tNote < 0.2) tNote / 0.2 else 1.0
                val envelope = attack * Math.exp(-0.6 * tNote)
                
                // Base sine wave (piano body) + some harmonics
                val fundamental = Math.sin(2.0 * Math.PI * freq * tNote)
                val harmonic1 = 0.2 * Math.sin(2.0 * Math.PI * (freq * 2) * tNote)
                val harmonic2 = 0.1 * Math.sin(2.0 * Math.PI * (freq * 3) * tNote)
                val piano = (fundamental + harmonic1 + harmonic2) * envelope
                
                // Ambient synth pad (Drone shifting slowly)
                val droneFreq = 130.81 // C3
                val lfo = Math.sin(2.0 * Math.PI * 0.1 * tTotal) // 0.1 Hz LFO
                val synthPad = 0.2 * Math.sin(2.0 * Math.PI * (droneFreq + lfo * 1.5) * tTotal) +
                               0.1 * Math.sin(2.0 * Math.PI * (droneFreq * 1.5) * tTotal) // fifth
                               
                val dVal = (piano * 0.4 + synthPad) * 0.6 
                
                val valShort = (dVal * 32767).toInt().toShort()
                generatedSnd[currentSample * 2] = (valShort.toInt() and 0x00ff).toByte()
                generatedSnd[currentSample * 2 + 1] = ((valShort.toInt() and 0xff00) ushr 8).toByte()
                currentSample++
            }
        }
        
        val file = File(context.cacheDir, "bgm.wav")
        writeWavFile(file, sampleRate, generatedSnd)
        return file
    }

    private fun writeWavFile(file: File, sampleRate: Int, generatedSnd: ByteArray) {
        FileOutputStream(file).use { out ->
            val channels = 1
            val byteRate = sampleRate * channels * 2
            val totalDataLen = generatedSnd.size + 36

            val header = ByteArray(44)
            header[0] = 'R'.code.toByte()
            header[1] = 'I'.code.toByte()
            header[2] = 'F'.code.toByte()
            header[3] = 'F'.code.toByte()
            header[4] = (totalDataLen and 0xff).toByte()
            header[5] = ((totalDataLen shr 8) and 0xff).toByte()
            header[6] = ((totalDataLen shr 16) and 0xff).toByte()
            header[7] = ((totalDataLen shr 24) and 0xff).toByte()
            header[8] = 'W'.code.toByte()
            header[9] = 'A'.code.toByte()
            header[10] = 'V'.code.toByte()
            header[11] = 'E'.code.toByte()
            header[12] = 'f'.code.toByte() 
            header[13] = 'm'.code.toByte()
            header[14] = 't'.code.toByte()
            header[15] = ' '.code.toByte()
            header[16] = 16
            header[17] = 0
            header[18] = 0
            header[19] = 0
            header[20] = 1
            header[21] = 0
            header[22] = channels.toByte()
            header[23] = 0
            header[24] = (sampleRate and 0xff).toByte()
            header[25] = ((sampleRate shr 8) and 0xff).toByte()
            header[26] = ((sampleRate shr 16) and 0xff).toByte()
            header[27] = ((sampleRate shr 24) and 0xff).toByte()
            header[28] = (byteRate and 0xff).toByte()
            header[29] = ((byteRate shr 8) and 0xff).toByte()
            header[30] = ((byteRate shr 16) and 0xff).toByte()
            header[31] = ((byteRate shr 24) and 0xff).toByte()
            header[32] = (channels * 2).toByte()
            header[33] = 0
            header[34] = 16
            header[35] = 0
            header[36] = 'd'.code.toByte()
            header[37] = 'a'.code.toByte()
            header[38] = 't'.code.toByte()
            header[39] = 'a'.code.toByte()
            header[40] = (generatedSnd.size and 0xff).toByte()
            header[41] = ((generatedSnd.size shr 8) and 0xff).toByte()
            header[42] = ((generatedSnd.size shr 16) and 0xff).toByte()
            header[43] = ((generatedSnd.size shr 24) and 0xff).toByte()

            out.write(header, 0, 44)
            out.write(generatedSnd)
        }
    }
}

