package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CollectibleCharacter
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.*

@Composable
fun CharactersScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val characters by viewModel.allCharacters.collectAsState()
    val progress by viewModel.userProgress.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val starsCount = progress?.starsCount ?: 0
    val wildcardsCount = progress?.wildcardsCount ?: 0

    var selectedCharacterId by remember { mutableStateOf<String?>(null) }
    val selectedCharacter = characters.find { it.id == selectedCharacterId }
    var showConfetti by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .background(Color(0xFFE5D3B3), RoundedCornerShape(16.dp))
            .border(2.dp, PaperMarioColors.BorderBrown.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)) // To ensure internal elements don't bleed out of the rounded corners
        ) {
            // Red Theater Canopy
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .background(Color(0xFFC62828)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🎪 TIENDA Y GALERÍA DE CARTÓN 2D 🎪",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(2.5.dp).background(PaperMarioColors.BorderBrown))

        if (isLandscape) {
            // LANDSCAPE: Dual Panel Split View
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // LEFT PANEL (Details, stats, buy wildcards)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1.1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Header row inside left panel
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CardboardButton(
                            onClick = { viewModel.navigateTo(Screen.Home) },
                            containerColor = PaperMarioColors.PaperWhite
                        ) {
                            Text(
                                text = "◀ ATRÁS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PaperMarioColors.BorderBrown
                            )
                        }

                        // Stars & Wildcards pocket stats
                        Box(
                            modifier = Modifier
                                .background(PaperMarioColors.PaperWhite, shape = RoundedCornerShape(10.dp))
                                .border(1.5.dp, PaperMarioColors.BorderBrown, shape = RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                                    Text("⭐", fontSize = 14.sp)
                                    Text(
                                        text = "$starsCount",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PaperMarioColors.BorderBrown
                                    )
                                }
                                Box(modifier = Modifier.width(1.dp).height(12.dp).background(PaperMarioColors.BorderBrown.copy(alpha = 0.3f)))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                                    Text("🃏", fontSize = 14.sp)
                                    Text(
                                        text = "$wildcardsCount",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PaperMarioColors.BorderBrown
                                    )
                                }
                            }
                        }
                    }

                    // Interactive Detail Pane (fills remaining vertical space)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (selectedCharacter != null) {
                            val char = selectedCharacter
                            val isUnlocked = char.unlocked

                            CardboardContainer(
                                modifier = Modifier.fillMaxSize(),
                                backgroundColor = PaperMarioColors.PaperWhite,
                                borderColor = if (isUnlocked) PaperMarioColors.GrassGreen else PaperMarioColors.BorderBrown,
                                cornerRadius = 14.dp,
                                hasStitches = true
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        StickerCharacter(
                                            emoji = if (isUnlocked) char.emoji else "🔒",
                                            size = 48.dp,
                                            hasPedestal = isUnlocked
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = char.name,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PaperMarioColors.BorderBrown
                                            )
                                            Text(
                                                text = if (isUnlocked) "EN ÁLBUM" else "BLOQUEADO",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUnlocked) Color(0xFF27AE60) else Color(0xFF7F8C8D)
                                            )
                                        }
                                    }

                                    Text(
                                        text = if (isUnlocked) char.description else "Este amigo de cartón aún no se ha mudado a tu álbum de recortes. ¡Gasta 3 estrellas para desbloquearlo!",
                                        fontSize = 11.sp,
                                        color = PaperMarioColors.BorderBrown.copy(alpha = 0.9f),
                                        lineHeight = 15.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .padding(vertical = 4.dp)
                                    )

                                    if (!isUnlocked) {
                                        CardboardButton(
                                            onClick = { 
                                                if (starsCount >= 3) {
                                                    showConfetti = true
                                                    com.example.utils.SoundManager.playPop()
                                                }
                                                viewModel.buyCharacter(char.id) 
                                            },
                                            containerColor = PaperMarioColors.GrassGreen,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "ADQUIRIR CON 3 ⭐",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.White
                                            )
                                        }
                                    } else {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            CardboardButton(
                                                onClick = { viewModel.speakText(char.soundPhrase) },
                                                containerColor = PaperMarioColors.SkyBlue,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = "🔊 ESCUCHAR",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = PaperMarioColors.BorderBrown
                                                )
                                            }

                                            CardboardButton(
                                                onClick = { 
                                                    viewModel.equipCharacter(char.id)
                                                    selectedCharacterId = null
                                                },
                                                containerColor = Color(0xFFF1C40F),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = "EQUIPAR 🐾",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = PaperMarioColors.BorderBrown
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // DEFAULT VIEW: Show Wildcard Buy
                            CardboardContainer(
                                modifier = Modifier.fillMaxSize(),
                                backgroundColor = Color(0xFFFFF9E6),
                                borderColor = PaperMarioColors.BorderBrown,
                                cornerRadius = 14.dp,
                                hasStitches = true
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text("🃏", fontSize = 28.sp)
                                            Column {
                                                Text(
                                                    text = "Cartas Comodín",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = PaperMarioColors.BorderBrown
                                                )
                                                Text(
                                                    text = "Tienes: $wildcardsCount en tu mazo",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFD35400)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Las cartas comodín te permiten repetir una pregunta que fallaste en la trivia sin perder tu progreso. ¡Esencial para ganar estrellas!",
                                            fontSize = 11.sp,
                                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.8f),
                                            lineHeight = 15.sp,
                                            textAlign = TextAlign.Start
                                        )
                                    }

                                    CardboardButton(
                                        onClick = { viewModel.buyWildcard() },
                                        containerColor = PaperMarioColors.BannerYellow,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "ADQUIRIR COMODÍN (4 ⭐)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PaperMarioColors.BorderBrown
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // RIGHT PANEL: The Great Shelf Grid
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1.5f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "ESTANTERÍA DE PERSONAJES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = PaperMarioColors.BorderBrown,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color(0xFFFFFDF9), shape = RoundedCornerShape(14.dp))
                            .border(3.dp, PaperMarioColors.BorderBrown, shape = RoundedCornerShape(14.dp))
                            .padding(8.dp)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Wildcard shortcut card
                            item {
                                val isSelected = selectedCharacterId == null
                                CardboardContainer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { 
                                            com.example.utils.SoundManager.playPop()
                                            selectedCharacterId = null 
                                        },
                                    backgroundColor = if (isSelected) Color(0xFFFEF9E7) else PaperMarioColors.PaperWhite,
                                    borderColor = if (isSelected) Color(0xFFE67E22) else PaperMarioColors.BorderBrown,
                                    cornerRadius = 10.dp,
                                    hasStitches = false
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        StickerCharacter(
                                            emoji = "🃏",
                                            size = 36.dp,
                                            hasPedestal = false
                                        )
                                        Column {
                                            Text(
                                                text = "Comodín",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PaperMarioColors.BorderBrown
                                            )
                                            Text(
                                                text = "Costo: 4 ⭐",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFD35400)
                                            )
                                        }
                                    }
                                }
                            }

                            // Collectible Characters
                            items(characters) { character ->
                                val isUnlocked = character.unlocked
                                val isSelected = selectedCharacterId == character.id

                                val bgTint = if (isUnlocked) {
                                    if (isSelected) Color(0xFFFEF9E7) else Color.White
                                } else {
                                    Color(0xFFE5DCD0)
                                }

                                CardboardContainer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            com.example.utils.SoundManager.playPop()
                                            selectedCharacterId = character.id
                                            if (isUnlocked) {
                                                viewModel.speakText(character.soundPhrase)
                                            } else {
                                                viewModel.speakText("Este es ${character.name}. Desbloquéalo por tres estrellas.")
                                            }
                                        },
                                    backgroundColor = bgTint,
                                    borderColor = if (isSelected) Color(0xFFE67E22) else PaperMarioColors.BorderBrown,
                                    cornerRadius = 10.dp,
                                    hasStitches = isUnlocked
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        StickerCharacter(
                                            emoji = if (isUnlocked) character.emoji else "🔒",
                                            size = 36.dp,
                                            hasPedestal = isUnlocked
                                        )
                                        Column {
                                            Text(
                                                text = character.name,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUnlocked) PaperMarioColors.BorderBrown else PaperMarioColors.BorderBrown.copy(alpha = 0.5f)
                                            )
                                            Text(
                                                text = if (isUnlocked) "✨ Desbloqueado" else "Costo: 3 ⭐",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUnlocked) Color(0xFF27AE60) else Color(0xFF7F8C8D)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // PORTRAIT: Scrollable Notebook Layout (Fits Portrait beautifully!)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CardboardButton(
                        onClick = { viewModel.navigateTo(Screen.Home) },
                        containerColor = PaperMarioColors.PaperWhite
                    ) {
                        Text(
                            text = "◀ VOLVER AL INICIO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PaperMarioColors.BorderBrown
                        )
                    }

                    // Stats
                    Box(
                        modifier = Modifier
                            .background(PaperMarioColors.PaperWhite, shape = RoundedCornerShape(10.dp))
                            .border(1.5.dp, PaperMarioColors.BorderBrown, shape = RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("⭐", fontSize = 16.sp)
                                Text(
                                    text = "$starsCount",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PaperMarioColors.BorderBrown
                                )
                            }
                            Box(modifier = Modifier.width(1.dp).height(12.dp).background(PaperMarioColors.BorderBrown.copy(alpha = 0.3f)))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("🃏", fontSize = 16.sp)
                                Text(
                                    text = "$wildcardsCount",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PaperMarioColors.BorderBrown
                                )
                            }
                        }
                    }
                }

                // Selected Detail Card or Wildcard Buy Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    if (selectedCharacter != null) {
                        val char = selectedCharacter
                        val isUnlocked = char.unlocked

                        CardboardContainer(
                            modifier = Modifier.fillMaxSize(),
                            backgroundColor = PaperMarioColors.PaperWhite,
                            borderColor = if (isUnlocked) PaperMarioColors.GrassGreen else PaperMarioColors.BorderBrown,
                            cornerRadius = 14.dp,
                            hasStitches = true
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                              ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    StickerCharacter(
                                        emoji = if (isUnlocked) char.emoji else "🔒",
                                        size = 44.dp,
                                        hasPedestal = isUnlocked
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = char.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PaperMarioColors.BorderBrown
                                        )
                                        Text(
                                            text = if (isUnlocked) "EN ÁLBUM" else "BLOQUEADO",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isUnlocked) Color(0xFF27AE60) else Color(0xFF7F8C8D)
                                        )
                                    }
                                }

                                Text(
                                    text = if (isUnlocked) char.description else "¡Gasta 3 estrellas para desbloquear a este personaje de cartón!",
                                    fontSize = 10.sp,
                                    color = PaperMarioColors.BorderBrown.copy(alpha = 0.9f),
                                    lineHeight = 14.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(vertical = 4.dp)
                                )

                                if (!isUnlocked) {
                                    CardboardButton(
                                        onClick = { 
                                            if (starsCount >= 3) {
                                                showConfetti = true
                                                com.example.utils.SoundManager.playPop()
                                            }
                                            viewModel.buyCharacter(char.id) 
                                        },
                                        containerColor = PaperMarioColors.GrassGreen,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "DESBLOQUEAR CON 3 ⭐",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        CardboardButton(
                                            onClick = { viewModel.speakText(char.soundPhrase) },
                                            containerColor = PaperMarioColors.SkyBlue,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = "🔊 ESCUCHAR",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PaperMarioColors.BorderBrown
                                            )
                                        }

                                        CardboardButton(
                                            onClick = { 
                                                viewModel.equipCharacter(char.id)
                                                selectedCharacterId = null
                                            },
                                            containerColor = Color(0xFFF1C40F),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = "EQUIPAR",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PaperMarioColors.BorderBrown
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Wildcard Purchase View
                        CardboardContainer(
                            modifier = Modifier.fillMaxSize(),
                            backgroundColor = Color(0xFFFFF9E6),
                            borderColor = PaperMarioColors.BorderBrown,
                            cornerRadius = 14.dp,
                            hasStitches = true
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("🃏", fontSize = 24.sp)
                                        Column {
                                            Text(
                                                text = "Cartas Comodín",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PaperMarioColors.BorderBrown
                                            )
                                            Text(
                                                text = "Tienes: $wildcardsCount en mazo",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFD35400)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = "Las cartas comodín te permiten re-intentar una pregunta fallada sin perder tu avance. ¡Esenciales!",
                                        fontSize = 10.sp,
                                        color = PaperMarioColors.BorderBrown.copy(alpha = 0.8f),
                                        lineHeight = 13.sp
                                    )
                                }

                                CardboardButton(
                                    onClick = { viewModel.buyWildcard() },
                                    containerColor = PaperMarioColors.BannerYellow,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "COMPRAR COMODÍN (4 ⭐)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PaperMarioColors.BorderBrown
                                    )
                                }
                            }
                        }
                    }
                }

                // Shelf Area with Items
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "🧸 ESTANTERÍA DE JUGUETES (TOCA UN ICONO)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = PaperMarioColors.BorderBrown,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFDF9), shape = RoundedCornerShape(14.dp))
                            .border(2.5.dp, PaperMarioColors.BorderBrown, shape = RoundedCornerShape(14.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quick Purchase Card
                        val isWildcardSelected = selectedCharacterId == null
                        CardboardContainer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    com.example.utils.SoundManager.playPop()
                                    selectedCharacterId = null 
                                },
                            backgroundColor = if (isWildcardSelected) Color(0xFFFEF9E7) else PaperMarioColors.PaperWhite,
                            borderColor = if (isWildcardSelected) Color(0xFFE67E22) else PaperMarioColors.BorderBrown,
                            cornerRadius = 10.dp,
                            hasStitches = false
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                StickerCharacter(
                                    emoji = "🃏",
                                    size = 32.dp,
                                    hasPedestal = false
                                )
                                Column {
                                    Text(
                                        text = "Comprar Comodín",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PaperMarioColors.BorderBrown
                                    )
                                    Text(
                                        text = "Costo: 4 ⭐",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD35400)
                                    )
                                }
                            }
                        }

                        // Chunk characters in pairs of 2 for grid representation inside scrollable Column
                        val chunkedCharacters = characters.chunked(2)
                        chunkedCharacters.forEach { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                pair.forEach { character ->
                                    val isUnlocked = character.unlocked
                                    val isSelected = selectedCharacterId == character.id

                                    val bgTint = if (isUnlocked) {
                                        if (isSelected) Color(0xFFFEF9E7) else Color.White
                                    } else {
                                        Color(0xFFE5DCD0)
                                    }

                                    CardboardContainer(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                com.example.utils.SoundManager.playPop()
                                                selectedCharacterId = character.id
                                                if (isUnlocked) {
                                                    viewModel.speakText(character.soundPhrase)
                                                } else {
                                                    viewModel.speakText("Este es ${character.name}. Desbloquéalo por tres estrellas.")
                                                }
                                            },
                                        backgroundColor = bgTint,
                                        borderColor = if (isSelected) Color(0xFFE67E22) else PaperMarioColors.BorderBrown,
                                        cornerRadius = 10.dp,
                                        hasStitches = isUnlocked
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            StickerCharacter(
                                                emoji = if (isUnlocked) character.emoji else "🔒",
                                                size = 32.dp,
                                                hasPedestal = isUnlocked
                                            )
                                            Column {
                                                Text(
                                                    text = character.name,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isUnlocked) PaperMarioColors.BorderBrown else PaperMarioColors.BorderBrown.copy(alpha = 0.5f)
                                                )
                                                Text(
                                                    text = if (isUnlocked) "✨ Adquirido" else "3 ⭐",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isUnlocked) Color(0xFF27AE60) else Color(0xFF7F8C8D)
                                                )
                                            }
                                        }
                                    }
                                }
                                // If the last row has only 1 item, put an empty box spacer to align nicely
                                if (pair.size < 2) {
                                    Box(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (showConfetti) {
            ConfettiEffect(onComplete = { showConfetti = false })
        }
    }
}
}
