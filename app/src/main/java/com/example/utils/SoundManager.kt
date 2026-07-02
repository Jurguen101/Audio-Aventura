package com.example.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import java.io.File
import java.io.FileOutputStream

object SoundManager {
    private var soundPool: SoundPool? = null
    private var popSoundId: Int = 0
    private var defaultBgmSoundId: Int = 0
    private var isLoaded = false
    
    private var bgmStreamId: Int = 0
    val isBgmEnabled = androidx.compose.runtime.mutableStateOf(true) // Enabled by default now!
    
    // Map of chapterId to soundId
    private val chapterBgmIds = mutableMapOf<String, Int>()
    private var currentChapterId: String? = null

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
                if (sampleId == defaultBgmSoundId && isBgmEnabled.value && currentChapterId == null) {
                    playBgm()
                }
            }
        }
        try {
            val wavFile = createWavFile(context)
            popSoundId = soundPool?.load(wavFile.absolutePath, 1) ?: 0
            
            val bgmFile = createBgmWavFile(context)
            defaultBgmSoundId = soundPool?.load(bgmFile.absolutePath, 1) ?: 0
            
            // Load chapter themes
            val themes = listOf(
                "ninja", "fantasma", "vaquero", "rey", "robot", 
                "dragon", "pirata", "astronauta", "ardilla", "mago"
            )
            for (theme in themes) {
                val themeFile = createChapterThemeWav(context, theme)
                chapterBgmIds[theme] = soundPool?.load(themeFile.absolutePath, 1) ?: 0
            }
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
            if (currentChapterId != null) {
                playChapterBgm(currentChapterId!!)
            } else {
                playBgm()
            }
        } else {
            stopBgm()
        }
    }

    fun playBgm() {
        currentChapterId = null
        stopBgm()
        if (isLoaded && isBgmEnabled.value && bgmStreamId == 0) {
            bgmStreamId = soundPool?.play(defaultBgmSoundId, 0.4f, 0.4f, 0, -1, 1f) ?: 0
        }
    }
    
    fun playChapterBgm(chapterId: String) {
        currentChapterId = chapterId
        stopBgm()
        if (isLoaded && isBgmEnabled.value && bgmStreamId == 0) {
            val soundId = chapterBgmIds[chapterId] ?: defaultBgmSoundId
            bgmStreamId = soundPool?.play(soundId, 0.4f, 0.4f, 0, -1, 1f) ?: 0
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
        chapterBgmIds.clear()
    }

    private fun createWavFile(context: Context): File {
        val sampleRate = 44100
        val duration = 0.08
        val numSamples = (duration * sampleRate).toInt()
        val generatedSnd = ByteArray(2 * numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
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
        val notes = listOf(
            Pair(261.63, 2.0), Pair(329.63, 2.0), Pair(392.00, 2.0), Pair(349.23, 2.0),
            Pair(440.00, 2.0), Pair(392.00, 2.0), Pair(261.63, 4.0)
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
                val attack = if (tNote < 0.2) tNote / 0.2 else 1.0
                val envelope = attack * Math.exp(-0.6 * tNote)
                val fundamental = Math.sin(2.0 * Math.PI * freq * tNote)
                val harmonic1 = 0.2 * Math.sin(2.0 * Math.PI * (freq * 2) * tNote)
                val harmonic2 = 0.1 * Math.sin(2.0 * Math.PI * (freq * 3) * tNote)
                val piano = (fundamental + harmonic1 + harmonic2) * envelope
                val droneFreq = 130.81
                val lfo = Math.sin(2.0 * Math.PI * 0.1 * tTotal)
                val synthPad = 0.2 * Math.sin(2.0 * Math.PI * (droneFreq + lfo * 1.5) * tTotal) +
                               0.1 * Math.sin(2.0 * Math.PI * (droneFreq * 1.5) * tTotal)
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

    private fun createChapterThemeWav(context: Context, theme: String): File {
        val sampleRate = 44100
        val notes = when (theme) {
            "ninja" -> listOf(Pair(440.0, 0.5), Pair(523.25, 0.5), Pair(587.33, 0.5), Pair(659.25, 0.5), Pair(880.0, 1.0)) // A minor pentatonic
            "fantasma" -> listOf(Pair(523.25, 1.0), Pair(554.37, 1.0), Pair(523.25, 1.0), Pair(587.33, 2.0)) // Creepy semitones
            "vaquero" -> listOf(Pair(196.0, 0.5), Pair(293.66, 0.5), Pair(196.0, 0.5), Pair(329.63, 0.5), Pair(196.0, 1.0)) // Country bounce G
            "rey" -> listOf(Pair(261.63, 1.0), Pair(392.0, 1.0), Pair(523.25, 2.0), Pair(392.0, 1.0), Pair(523.25, 2.0)) // Fanfare C
            "robot" -> listOf(Pair(261.63, 0.25), Pair(329.63, 0.25), Pair(392.0, 0.25), Pair(523.25, 0.25), Pair(392.0, 0.25), Pair(329.63, 0.25), Pair(261.63, 1.0)) // Arpeggiator
            "dragon" -> listOf(Pair(65.41, 1.0), Pair(77.78, 1.0), Pair(98.0, 2.0)) // Deep menacing C min
            "pirata" -> listOf(Pair(261.63, 0.33), Pair(329.63, 0.33), Pair(392.0, 0.33), Pair(523.25, 1.0)) // Sea shanty 6/8
            "astronauta" -> listOf(Pair(523.25, 2.0), Pair(493.88, 2.0), Pair(392.0, 4.0)) // Ambient drift
            "ardilla" -> listOf(Pair(1046.5, 0.1), Pair(1318.5, 0.1), Pair(1046.5, 0.1), Pair(1567.98, 0.5)) // Crazy high pitched
            "mago" -> listOf(Pair(261.63, 0.5), Pair(293.66, 0.5), Pair(329.63, 0.5), Pair(369.99, 0.5), Pair(415.30, 2.0)) // Whole tone mysterious
            else -> listOf(Pair(261.63, 2.0), Pair(329.63, 2.0), Pair(392.0, 2.0))
        }
        
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
                
                var wave = 0.0
                var envelope = 1.0
                
                when (theme) {
                    "ninja" -> {
                        wave = Math.sin(2.0 * Math.PI * freq * tNote) // Pure flute
                        envelope = Math.exp(-2.0 * tNote)
                    }
                    "fantasma" -> {
                        val vibrato = Math.sin(2.0 * Math.PI * 6.0 * tNote) * 10.0
                        wave = Math.sin(2.0 * Math.PI * (freq + vibrato) * tNote) // Theremin
                        envelope = if (tNote < 0.5) tNote * 2.0 else Math.exp(-1.0 * (tNote - 0.5))
                    }
                    "vaquero" -> {
                        wave = (Math.sin(2.0 * Math.PI * freq * tNote) > 0).compareTo(false).toDouble() * 0.5 // Square wave
                        envelope = Math.exp(-5.0 * tNote)
                    }
                    "rey" -> {
                        wave = Math.sin(2.0 * Math.PI * freq * tNote) + 0.5 * Math.sin(2.0 * Math.PI * freq * 2 * tNote) // Brass
                        envelope = if (tNote < 0.2) tNote / 0.2 else 1.0
                    }
                    "robot" -> {
                        wave = ((tNote * freq) % 1.0) - 0.5 // Sawtooth
                        envelope = Math.exp(-10.0 * tNote)
                    }
                    "dragon" -> {
                        wave = Math.sin(2.0 * Math.PI * freq * tNote) + 0.3 * ((tNote * freq) % 1.0 - 0.5) // Deep growl
                        envelope = 1.0
                    }
                    "pirata" -> {
                        wave = Math.sin(2.0 * Math.PI * freq * tNote) + 0.5 * Math.sin(2.0 * Math.PI * freq * 3 * tNote) // Accordion-ish
                        envelope = Math.exp(-3.0 * tNote)
                    }
                    "astronauta" -> {
                        val slide = Math.exp(-0.5 * tTotal)
                        wave = Math.sin(2.0 * Math.PI * freq * slide * tNote) // Gliding synth
                        envelope = Math.sin(Math.PI * (currentSample.toDouble() / numSamples)) // Swell
                    }
                    "ardilla" -> {
                        wave = Math.sin(2.0 * Math.PI * freq * tNote)
                        envelope = Math.exp(-15.0 * tNote)
                    }
                    "mago" -> {
                        wave = Math.sin(2.0 * Math.PI * freq * tNote) * Math.sin(2.0 * Math.PI * (freq + 2.0) * tNote) // Bell/FM-like
                        envelope = Math.exp(-1.5 * tNote)
                    }
                    else -> {
                        wave = Math.sin(2.0 * Math.PI * freq * tNote)
                        envelope = Math.exp(-2.0 * tNote)
                    }
                }
                
                val dVal = wave * envelope * 0.4
                val valShort = (dVal * 32767).toInt().toShort()
                generatedSnd[currentSample * 2] = (valShort.toInt() and 0x00ff).toByte()
                generatedSnd[currentSample * 2 + 1] = ((valShort.toInt() and 0xff00) ushr 8).toByte()
                currentSample++
            }
        }
        val file = File(context.cacheDir, "bgm_$theme.wav")
        writeWavFile(file, sampleRate, generatedSnd)
        return file
    }

    private fun writeWavFile(file: File, sampleRate: Int, generatedSnd: ByteArray) {
        FileOutputStream(file).use { out ->
            val channels = 1
            val byteRate = sampleRate * channels * 2
            val totalDataLen = generatedSnd.size + 36
            val header = ByteArray(44)
            header[0] = 'R'.code.toByte(); header[1] = 'I'.code.toByte(); header[2] = 'F'.code.toByte(); header[3] = 'F'.code.toByte()
            header[4] = (totalDataLen and 0xff).toByte(); header[5] = ((totalDataLen shr 8) and 0xff).toByte()
            header[6] = ((totalDataLen shr 16) and 0xff).toByte(); header[7] = ((totalDataLen shr 24) and 0xff).toByte()
            header[8] = 'W'.code.toByte(); header[9] = 'A'.code.toByte(); header[10] = 'V'.code.toByte(); header[11] = 'E'.code.toByte()
            header[12] = 'f'.code.toByte(); header[13] = 'm'.code.toByte(); header[14] = 't'.code.toByte(); header[15] = ' '.code.toByte()
            header[16] = 16; header[17] = 0; header[18] = 0; header[19] = 0
            header[20] = 1; header[21] = 0; header[22] = channels.toByte(); header[23] = 0
            header[24] = (sampleRate and 0xff).toByte(); header[25] = ((sampleRate shr 8) and 0xff).toByte()
            header[26] = ((sampleRate shr 16) and 0xff).toByte(); header[27] = ((sampleRate shr 24) and 0xff).toByte()
            header[28] = (byteRate and 0xff).toByte(); header[29] = ((byteRate shr 8) and 0xff).toByte()
            header[30] = ((byteRate shr 16) and 0xff).toByte(); header[31] = ((byteRate shr 24) and 0xff).toByte()
            header[32] = (channels * 2).toByte(); header[33] = 0; header[34] = 16; header[35] = 0
            header[36] = 'd'.code.toByte(); header[37] = 'a'.code.toByte(); header[38] = 't'.code.toByte(); header[39] = 'a'.code.toByte()
            header[40] = (generatedSnd.size and 0xff).toByte(); header[41] = ((generatedSnd.size shr 8) and 0xff).toByte()
            header[42] = ((generatedSnd.size shr 16) and 0xff).toByte(); header[43] = ((generatedSnd.size shr 24) and 0xff).toByte()
            out.write(header, 0, 44)
            out.write(generatedSnd)
        }
    }
}
