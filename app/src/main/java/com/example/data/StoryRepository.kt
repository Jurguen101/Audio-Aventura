package com.example.data

import kotlinx.coroutines.flow.Flow

class StoryRepository(private val dao: AdventureDao) {

    val userProgress: Flow<UserProgress?> = dao.getUserProgressFlow()
    val allChapters: Flow<List<StoryChapter>> = dao.getAllChaptersFlow()
    val allCharacters: Flow<List<CollectibleCharacter>> = dao.getAllCharactersFlow()

    suspend fun ensureSeeded() {
        val currentProgress = dao.getUserProgress()
        if (currentProgress == null) {
            AppDatabase.seedDatabase(dao)
        }
    }

    suspend fun addStars(amount: Int) {
        val current = dao.getUserProgress() ?: UserProgress(id = 1, starsCount = 0, wildcardsCount = 0)
        val updated = current.copy(starsCount = current.starsCount + amount)
        dao.saveUserProgress(updated)
    }

    suspend fun buyWildcard(): Boolean {
        val current = dao.getUserProgress() ?: UserProgress(id = 1, starsCount = 0, wildcardsCount = 0)
        return if (current.starsCount >= 4) {
            val updated = current.copy(
                starsCount = current.starsCount - 4,
                wildcardsCount = current.wildcardsCount + 1
            )
            dao.saveUserProgress(updated)
            true
        } else {
            false
        }
    }

    suspend fun equipCharacter(characterId: String) {
        val current = dao.getUserProgress() ?: UserProgress(id = 1)
        val updated = current.copy(activePetId = characterId)
        dao.saveUserProgress(updated)
    }

    suspend fun useWildcard(): Boolean {
        val current = dao.getUserProgress() ?: UserProgress(id = 1, starsCount = 0, wildcardsCount = 0)
        return if (current.wildcardsCount >= 1) {
            val updated = current.copy(wildcardsCount = current.wildcardsCount - 1)
            dao.saveUserProgress(updated)
            true
        } else {
            false
        }
    }

    suspend fun buyCharacter(characterId: String): Boolean {
        val current = dao.getUserProgress() ?: UserProgress(id = 1, starsCount = 0, wildcardsCount = 0)
        val char = dao.getCharacterById(characterId) ?: return false
        if (!char.unlocked && current.starsCount >= char.priceStars) {
            val updatedProgress = current.copy(starsCount = current.starsCount - char.priceStars)
            dao.saveUserProgress(updatedProgress)
            dao.updateCharacter(char.copy(unlocked = true))
            return true
        }
        return false
    }

    suspend fun resetProgress() {
        dao.saveUserProgress(UserProgress(id = 1, starsCount = 0, wildcardsCount = 0))
        
        // Reset 10 chapters from easiest (Level 1) to hardest (Level 10)
        val chapters = listOf(
            StoryChapter(
                id = "ninja",
                title = "El ninja impaciente",
                description = "Acompaña al joven Ken a descubrir que toda gran destreza comienza con pequeños pasos.",
                emoji = "🥷",
                unlocked = true,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 1
            ),
            StoryChapter(
                id = "fantasma",
                title = "El fantasma que tenía miedo",
                description = "¡Ayuda al tierno Gaspar a descubrir que el verdadero coraje es actuar a pesar del miedo!",
                emoji = "👻",
                unlocked = false,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 2
            ),
            StoryChapter(
                id = "vaquero",
                title = "El vaquero que nunca compartía",
                description = "Cruza la llanura y aprende con Tomás que la amistad y compartir valen más que el oro.",
                emoji = "🤠",
                unlocked = false,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 3
            ),
            StoryChapter(
                id = "rey",
                title = "El rey que escuchó a todos",
                description = "Aprende con el rey Adrián que escuchar las opiniones de todos ayuda a tomar las mejores decisiones.",
                emoji = "👑",
                unlocked = false,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 4
            ),
            StoryChapter(
                id = "robot",
                title = "El robot que quería ser perfecto",
                description = "Descubre junto al robot R-7 que equivocarse es parte del aprendizaje y del crecimiento.",
                emoji = "🤖",
                unlocked = false,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 5
            ),
            StoryChapter(
                id = "dragon",
                title = "El dragón sin fuego",
                description = "Acompaña a Drake a descubrir que su verdadero poder está en la calidez de su gran corazón.",
                emoji = "🐉",
                unlocked = false,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 6
            ),
            StoryChapter(
                id = "pirata",
                title = "La pirata sin brújula",
                description = "Navega con Marina y aprende que el trabajo en equipo y la confianza mutua superan cualquier tormenta.",
                emoji = "🏴‍☠️",
                unlocked = false,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 7
            ),
            StoryChapter(
                id = "astronauta",
                title = "El astronauta nostálgico",
                description = "Viaja al espacio con Leo y descubre que los lazos del amor son más fuertes que la distancia física.",
                emoji = "🧑‍🚀",
                unlocked = false,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 8
            ),
            StoryChapter(
                id = "ardilla",
                title = "La ardilla olvidadiza",
                description = "Sigue a Susi en el bosque y descubre que pedir ayuda a tus amigos nos hace más unidos y fuertes.",
                emoji = "🐿️",
                unlocked = false,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 9
            ),
            StoryChapter(
                id = "mago",
                title = "El mago sin varita",
                description = "Acompaña a Mateo en su prueba final y descubre que la magia más real nace de la fe en ti mismo.",
                emoji = "🧙‍♂️",
                unlocked = false,
                completed = false,
                starsEarned = 0,
                maxStars = 5,
                orderIndex = 10
            )
        )
        dao.insertChapters(chapters)

        // Reset 10 characters
        val characters = listOf(
            CollectibleCharacter(
                id = "ninja_char",
                name = "Ken el Ninja",
                chapterId = "ninja",
                emoji = "🥷",
                description = "Un tierno ninja de origami que entrena con paciencia y aprende del canto de los gorriones.",
                soundPhrase = "¡Con paciencia y esfuerzo diario, cruzaré cualquier cuerda floja!",
                unlocked = false,
                priceStars = 3
            ),
            CollectibleCharacter(
                id = "fantasma_char",
                name = "Gaspar el Fantasmita",
                chapterId = "fantasma",
                emoji = "👻",
                description = "Un fantasmita amigable que brilla al encontrar el valor para dar el primer paso.",
                soundPhrase = "¡El miedo no decide por mí! ¡Vayamos juntos a explorar!",
                unlocked = false,
                priceStars = 3
            ),
            CollectibleCharacter(
                id = "vaquero_char",
                name = "Tomás el Vaquero",
                chapterId = "vaquero",
                emoji = "🤠",
                description = "Un vaquero con un gran sombrero y una cantimplora gigante siempre lista para compartir.",
                soundPhrase = "¡Compartir con los demás hace más fácil superar cualquier desierto!",
                unlocked = false,
                priceStars = 3
            ),
            CollectibleCharacter(
                id = "rey_char",
                name = "Rey Adrián",
                chapterId = "rey",
                emoji = "👑",
                description = "Un joven rey que descubrió que las mejores ideas nacen al escuchar a su pueblo.",
                soundPhrase = "¡La Hora de las Buenas Ideas está oficialmente inaugurada!",
                unlocked = false,
                priceStars = 3
            ),
            CollectibleCharacter(
                id = "robot_char",
                name = "R-7 el Robot",
                chapterId = "robot",
                emoji = "🤖",
                description = "Un tierno robot que descubrió que equivocarse es una oportunidad para aprender.",
                soundPhrase = "¡Bip-bop! No me rindo, ¡hoy aprendí una nueva manera de hacerlo mejor!",
                unlocked = false,
                priceStars = 3
            ),
            CollectibleCharacter(
                id = "dragon_char",
                name = "Drake el Dragón",
                chapterId = "dragon",
                emoji = "🐉",
                description = "Un dragón de escamas verdes que descubrió que la calidez de su corazón es su mayor poder.",
                soundPhrase = "¡Mis burbujas no queman, pero calientan con todo mi cariño!",
                unlocked = false,
                priceStars = 3
            ),
            CollectibleCharacter(
                id = "pirata_char",
                name = "Marina la Pirata",
                chapterId = "pirata",
                emoji = "🏴‍☠️",
                description = "Una intrépida capitana que descubrió que el tesoro más grande es su tripulación unida.",
                soundPhrase = "¡Con un buen equipo y confianza mutua, navegaremos cualquier tormenta!",
                unlocked = false,
                priceStars = 3
            ),
            CollectibleCharacter(
                id = "astronauta_char",
                name = "Leo el Astronauta",
                chapterId = "astronauta",
                emoji = "🧑‍🚀",
                description = "Un científico espacial que descubrió que el amor viaja a la velocidad de la luz.",
                soundPhrase = "¡La distancia no puede detener los abrazos de nuestro corazón!",
                unlocked = false,
                priceStars = 3
            ),
            CollectibleCharacter(
                id = "ardilla_char",
                name = "Susi la Ardilla",
                chapterId = "ardilla",
                emoji = "🐿️",
                description = "Una ardilla muy ágil que aprendió que pedir ayuda nos une en comunidad.",
                soundPhrase = "¡Pedir ayuda es de valientes! ¡Compartamos las nueces del bosque!",
                unlocked = false,
                priceStars = 3
            ),
            CollectibleCharacter(
                id = "mago_char",
                name = "Mateo el Mago",
                chapterId = "mago",
                emoji = "🧙‍♂️",
                description = "Un joven estudiante que aprendió que la magia real reside en confiar en uno mismo.",
                soundPhrase = "¡La varita solo ayuda, pero la magia nace de mi propio corazón!",
                unlocked = false,
                priceStars = 3
            )
        )
        dao.insertCharacters(characters)
    }

    suspend fun unlockChapter(chapterId: String) {
        val chapter = dao.getChapter(chapterId)
        if (chapter != null && !chapter.unlocked) {
            dao.updateChapter(chapter.copy(unlocked = true))
        }
    }

    suspend fun completeChapter(chapterId: String, starsEarned: Int) {
        val chapter = dao.getChapter(chapterId)
        if (chapter != null) {
            val previousBest = if (chapter.completed) chapter.starsEarned else 0
            val netNewStars = maxOf(0, starsEarned - previousBest)
            
            val maxStars = maxOf(previousBest, starsEarned)
            dao.updateChapter(chapter.copy(completed = true, starsEarned = maxStars))
            
            if (netNewStars > 0) {
                addStars(netNewStars)
            }
            
            // Auto unlock next chapter
            val nextChapterId = when (chapterId) {
                "ninja" -> "fantasma"
                "fantasma" -> "vaquero"
                "vaquero" -> "rey"
                "rey" -> "robot"
                "robot" -> "dragon"
                "dragon" -> "pirata"
                "pirata" -> "astronauta"
                "astronauta" -> "ardilla"
                "ardilla" -> "mago"
                else -> null
            }
            if (nextChapterId != null) {
                unlockChapter(nextChapterId)
            }
        }
    }

    fun getPagesForChapter(chapterId: String): List<StoryPage> {
        return when (chapterId) {
            "ninja" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "En una aldea escondida entre montañas vivía un pequeño ninja llamado Ken. Soñaba con convertirse en el mejor guerrero de todo el reino. Cada mañana veía a los ninjas mayores correr sobre los árboles, lanzar estrellas con gran precisión y moverse tan rápido que parecían desaparecer.\n\n—¡Yo también quiero hacer eso! —decía emocionado.\n\nEl maestro Hayato sonreía cada vez que escuchaba a Ken:\n—Todo llegará a su tiempo. Primero debes aprender a caminar antes de correr.\n\nPero Ken no quería esperar. El primer día de entrenamiento, mientras los demás practicaban cómo mantener el equilibrio sobre un tronco de madera, Ken pensó que aquel ejercicio era demasiado fácil:\n—Eso es para principiantes —murmuró.",
                    backgroundGradientColors = listOf(0xFF2E2E3E, 0xFF1E1E28),
                    mainDioramaEmoji = "🥷",
                    floatingDioramaEmojis = listOf("💮", "🍃", "🪵", "🥋")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = "Sin que nadie lo viera, corrió hasta el patio donde los ninjas expertos entrenaban. Allí había una cuerda colgada entre dos árboles muy altos.\n\n—Si logro cruzarla, todos sabrán que soy un gran ninja.\n\nRespiró profundamente y comenzó a caminar sobre la cuerda. Al principio dio dos pasos con seguridad, pero enseguida perdió el equilibrio... ¡Ay! Cayó sobre un montón de hojas secas. Por suerte no se lastimó, aunque terminó cubierto de ramas y polvo.\n\nUn pequeño gorrión que estaba cerca comenzó a cantar como si se riera de él. Ken bajó la cabeza, avergonzado.",
                    backgroundGradientColors = listOf(0xFF4A3525, 0xFF2B1C10),
                    mainDioramaEmoji = "🐦",
                    floatingDioramaEmojis = listOf("🍃", "💥", "🥷", "🪵")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = "Al día siguiente volvió a intentarlo. Esta vez encontró unas estrellas ninja de entrenamiento:\n\n—Si aprendo a lanzarlas, seré el mejor.\n\nTomó una estrella, apuntó hacia un tronco y lanzó con todas sus fuerzas. La estrella giró en el aire... pero salió en otra dirección y terminó cortando una cuerda que sostenía un balde lleno de agua. ¡Splash! Toda el agua cayó sobre la cabeza de Ken. Los demás alumnos soltaron una pequeña risa.\n\nEl maestro Hayato caminó hasta donde estaba el niño:\n—¿Sabes por qué ocurrió esto?\n—Porque tuve mala suerte —suspiró Ken.\n—No —negó el maestro—. Ocurrió porque quisiste empezar por el final.",
                    backgroundGradientColors = listOf(0xFF5D4037, 0xFF3E2723),
                    mainDioramaEmoji = "💦",
                    floatingDioramaEmojis = listOf("🎯", "🥋", "💮", "⭐")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "Esa tarde, mientras caminaba por el bosque, vio al gorrión recogiendo una pequeña ramita y volando hasta un árbol. Luego regresaba por otra. Y otra. Ken observó durante un largo rato:\n\n—¿Por qué no llevas todas de una sola vez? —preguntó.\n\nEl gorrión respondió con un alegre canto. Entonces Ken comprendió: ningún nido aparecía de un momento a otro. Se construía ramita por ramita.\n\nAl día siguiente, Ken fue el primero en colocarse sobre el tronco de equilibrio. Practicó despacio, luego a saltar, luego a caer sin hacerse daño. Su cuerpo se volvió más fuerte y sus movimientos más seguros.",
                    backgroundGradientColors = listOf(0xFF33691E, 0xFF1B5E20),
                    mainDioramaEmoji = "🪺",
                    floatingDioramaEmojis = listOf("🌳", "🐦", "🍂", "💪")
                ),
                StoryPage(
                    pageNumber = 5,
                    textContent = "Semanas después, el maestro reunió a todos:\n\n—Hoy cruzarán la cuerda entre los árboles.\n\nKen respiró hondo. Esta vez no tenía prisa. Recordó cada práctica y dio un paso, luego otro, y otro más. ¡Había llegado al otro lado! Todos aplaudieron. El maestro Hayato sonrió con orgullo:\n\n—Hoy no ganaste porque fueras el más rápido.\n—Gané porque aprendí a tener paciencia —sonrió Ken.\n\nEl gorrión cantó feliz en una rama. Ken ahora entendía que las grandes metas siempre comienzan con pequeños pasos, y quien practica con paciencia llega mucho más lejos.\n\nMoraleja: La paciencia, la práctica constante y el esfuerzo diario permiten alcanzar metas que parecen imposibles.",
                    backgroundGradientColors = listOf(0xFFE8A87C, 0xFFC38D9E),
                    mainDioramaEmoji = "🌟",
                    floatingDioramaEmojis = listOf("🥷", "🐦", "🏔️", "✨")
                )
            )
            "fantasma" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "En lo alto de una colina se encontraba un castillo antiguo. Sus grandes paredes de piedra guardaban muchos secretos y el viento soplaba con susurros. Allí vivía un pequeño fantasma llamado Gaspar.\n\nA diferencia de los demás fantasmas, Gaspar no disfrutaba aparecer entre las sombras ni atravesar las paredes para sorprender a las personas. Él era quien más se asustaba.\n\n—¿Y si alguien se ríe de mí porque no doy miedo? —pensaba.\n\nCada noche, los fantasmas mayores salían a recorrer el bosque y Gaspar prefería quedarse escondido en la torre más alta, mirando la luna desde una pequeña ventana.",
                    backgroundGradientColors = listOf(0xFF2C3E50, 0xFF0F171E),
                    mainDioramaEmoji = "👻",
                    floatingDioramaEmojis = listOf("🏰", "🌙", "☁️", "🪵")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = "Su mejor amigo era un viejo búho llamado Bruno, que todas las noches se posaba en el tejado:\n\n—¿Por qué nunca sales? —preguntó el búho.\n—Porque tengo miedo de hacer el ridículo... de que nadie me quiera... de no ser un buen fantasma.\n\nBruno movió lentamente la cabeza:\n—Todos sienten miedo alguna vez. Lo importante es no dejar que el miedo decida por nosotros.\n\nUnos días después llegó al castillo una niña llamada Sofía junto a su abuelo, el encargado de cuidar el lugar. Mientras él trabajaba, Sofía decidió explorar los antiguos e imponentes pasillos.",
                    backgroundGradientColors = listOf(0xFF4A154B, 0xFF2A082C),
                    mainDioramaEmoji = "🦉",
                    floatingDioramaEmojis = listOf("🏰", "🌙", "🦉", "👧")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = "Gaspar la observó desde detrás de una columna: '¡Qué valiente es!', pensó. Pero cuando la niña llegó a una escalera muy oscura, dejó de caminar, abrazó su linterna y susurró: 'Creo que mejor regreso...'.\n\nGaspar comprendió que Sofía también tenía miedo. Salió de su escondite y levantó una mano con timidez: 'Hola...'.\n\nSofía parpadeó varias veces:\n—¿Tú... eres un fantasma?\n—Sí... pero también tengo miedo. Miedo de que se burlen de mí.\n\nSofía sonrió:\n—Yo también tengo miedo a veces. Hay lugares oscuros que me asustan.",
                    backgroundGradientColors = listOf(0xFF1F1C2C, 0xFF928DAB),
                    mainDioramaEmoji = "🔦",
                    floatingDioramaEmojis = listOf("👻", "👧", "🕸️", "🪜")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "Los dos comenzaron a conversar y descubrieron que compartían muchos gustos. Al llegar a la escalera oscura, Sofía respiró profundamente:\n\n—Si tú vienes conmigo, creo que podré subir.\n—Y si tú vienes conmigo, creo que dejaré de esconderme —respondió Gaspar.\n\nSubieron los escalones poco a poco. Al llegar arriba encontraron una ventana hermosa desde donde se veía todo el bosque iluminado por la luna.\n\nDesde aquella noche, Gaspar dejó de esconderse. No pretendía ser el más aterrador; prefería ser amable y recordar que el miedo desaparece cuando nos atrevemos a dar el primer paso junto a alguien.\n\nMoraleja: Ser valiente no significa no tener miedo, sino enfrentarlo con confianza y ayuda de los demás.",
                    backgroundGradientColors = listOf(0xFF0F2027, 0xFF203A43),
                    mainDioramaEmoji = "🌟",
                    floatingDioramaEmojis = listOf("👻", "👧", "🌲", "🌙")
                )
            )
            "vaquero" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "En un pueblo rodeado de montañas vivía un joven vaquero llamado Tomás. Llevaba un sombrero de ala ancha, botas de cuero y una cantimplora colgada de su cinturón. Tomás cuidaba muy bien sus cosas, pero tenía un problema: no le gustaba compartir.\n\nSi le pedían una cuerda, decía: 'La necesito'. Si le pedían una manzana: 'No tendré suficiente'. Y si le pedían agua: 'Debo guardar toda la que tengo'. Los habitantes pensaban que algún día aprendería la lección.\n\nUna mañana, Tomás emprendió un viaje a caballo con su fiel Relámpago a través de una enorme llanura bajo un sol radiante. Llenó su cantimplora y se dijo: 'Esta agua es solo para mí'.",
                    backgroundGradientColors = listOf(0xFFD38312, 0xFFA83279),
                    mainDioramaEmoji = "🤠",
                    floatingDioramaEmojis = listOf("🌵", "☀️", "🐎", "🏜️")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = "Al mediodía vio a una anciana bajo la sombra de un árbol:\n\n—Buenos días, joven. ¿Podrías regalarme un poco de agua? Mi botella está vacía.\n—Lo siento. No puedo compartirla —respondió Tomás y siguió de largo.\n\nMás adelante encontró a un niño empujando una pesada carreta:\n\n—¿Me ayudas un momento con un poco de agua y un descanso?\n\nTomás volvió a negar: 'Debo cuidar mis provisiones'. Relámpago bajó las orejas, disgustado con la decisión de su dueño. Al ocultarse el sol, una fuerte tormenta de arena apareció y Tomás perdió de vista el sendero. Estaba completamente perdido.",
                    backgroundGradientColors = listOf(0xFFE65C00, 0xFFF9D423),
                    mainDioramaEmoji = "🌪️",
                    floatingDioramaEmojis = listOf("👵", "👦", "🐎", "🏜️")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = "Caminó sin rumbo y su cantimplora quedó casi vacía. Por primera vez comprendió lo importante que era la ayuda de los demás. Al anochecer, vio una pequeña fogata y se acercó con esperanza. ¡Allí estaban la anciana y el niño, viajando juntos!\n\nAl reconocer a Tomás, sonrieron:\n—¡Qué bueno que estás bien!\n\nTomás bajó la cabeza con vergüenza:\n—Me perdí... y casi no me queda agua.\n\nLa anciana le ofreció su botella: 'Bebe un poco'. Tomás dudó: 'Pero... esta mañana yo no quise compartir con ustedes'.",
                    backgroundGradientColors = listOf(0xFF0F2027, 0xFF203A43),
                    mainDioramaEmoji = "🔥",
                    floatingDioramaEmojis = listOf("🤠", "👵", "👦", "🏕️")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "El niño sonrió, y la anciana le dijo con tranquilidad:\n\n—Because ayudar a alguien siempre vale la pena, incluso cuando esa persona todavía está aprendiendo.\n\nTomás sintió que aquellas palabras pesaban más que cualquier piedra. Compartieron comida y agua alrededor de la fogata, y al día siguiente la anciana le mostró el sendero correcto.\n\nTomás entregó su paquete a tiempo y regresó a casa para comprar una cantimplora mucho más grande. Desde entonces, comparte todo con alegría, porque aprendió que las personas nunca llegan muy lejos si caminan pensando solo en sí mismas.\n\nMoraleja: Compartir con los demás fortalece la amistad, crea confianza y nos ayuda a superar dificultades.",
                    backgroundGradientColors = listOf(0xFF11998E, 0xFF38EF7D),
                    mainDioramaEmoji = "🤝",
                    floatingDioramaEmojis = listOf("🤠", "🤝", "💦", "🌟")
                )
            )
            "rey" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "Hace muchos años existía un reino hermoso donde vivía el joven rey Adrián, quien quería ser recordado como el mejor gobernante de la historia. Cada mañana revisaba mapas y escribía listas con ideas para mejorar el reino.\n\nSin embargo, Adrián pensaba que siempre tenía la mejor respuesta y nunca escuchaba a nadie:\n\n—No es necesario, ya sé qué hacer —les decía a sus consejeros y campesinos.\n\nSu sabia consejera, doña Elvira, le advirtió: 'Majestad, un buen rey también aprende a escuchar'. Pero Adrián respondió seguro: 'Escuchar toma demasiado tiempo'.",
                    backgroundGradientColors = listOf(0xFF8E2DE2, 0xFF4A00E0),
                    mainDioramaEmoji = "👑",
                    floatingDioramaEmojis = listOf("🏰", "🗺️", "📜", "👑")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = "Poco después, el gran estanque que abastecía de agua a los huertos comenzó a vaciarse. Los cultivos se secaban. El rey reunió a todos:\n\n—¡Ya sé cómo resolverlo! Construiremos un canal enorme desde el río.\n\nTodos trabajaron duro cavando y levantando zanjas de piedra, pero cuando terminaron, el agua seguía sin llegar. El canal estaba bien construido, pero el estanque seguía seco. El rey no entendía qué sucedía.",
                    backgroundGradientColors = listOf(0xFFE65C00, 0xFFF9D423),
                    mainDioramaEmoji = "🌾",
                    floatingDioramaEmojis = listOf("⛏️", "🪨", "🏜️", "💦")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = "Caminando solo por los campos, Adrián encontró a un anciano jardinero observando el suelo. Le mostró unas pequeñas huellas que conducían a unos arbustos. Allí descubrieron una enorme rama caída que bloqueaba el paso del agua.\n\nAl retirarla, una niña llamada Clara se acercó corriendo:\n\n—¡Majestad! Yo había visto esa rama hace varios días, pero pensé que usted estaba muy ocupado para escucharme.\n\nEl rey sintió un profundo silencio en su interior. Recordó todas las veces que había ignorado a los demás.",
                    backgroundGradientColors = listOf(0xFF0F2027, 0xFF203A43),
                    mainDioramaEmoji = "🌿",
                    floatingDioramaEmojis = listOf("👑", "👴", "👧", "🪵")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "Al día siguiente, el rey colocó una silla en medio de la plaza y dijo: 'Hoy quiero escucharlos'. Campesinos, agricultores, panaderas y niños compartieron sus valiosas ideas.\n\nDesde entonces, instauró la 'Hora de las Buenas Ideas'. Los niños cuidaban los parques, los agricultores mejoraban cosechas y el reino se volvió más fuerte.\n\nAdrián descubrió el gran secreto: 'Antes creía que un rey debía tener todas las respuestas. Ahora sé que un buen rey hace las mejores preguntas y escucha con atención las respuestas de su pueblo'.\n\nMoraleja: Escuchar con atención las ideas de los demás nos ayuda a encontrar mejores soluciones y trabajar unidos.",
                    backgroundGradientColors = listOf(0xFF11998E, 0xFF38EF7D),
                    mainDioramaEmoji = "🗣️",
                    floatingDioramaEmojis = listOf("👑", "🤝", "🌾", "🌟")
                )
            )
            "robot" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "En una ciudad llena de inventos vivía un pequeño robot curioso llamado R-7, creado por la brillante inventora Elena. A R-7 le encantaba aprender: ordenar libros, regar plantas o cocinar sopa. Pero tenía un gran problema: quería hacer todo perfectamente desde el primer intento.\n\nSi colocaba un libro mal o regaba de más una flor, bajaba la cabeza triste: 'He fallado, nunca seré un buen robot'. Elena trataba de animarlo: 'Todos aprendemos mientras practicamos'. Pero R-7 no lograba convencerse y sus circuitos se llenaban de estrés.",
                    backgroundGradientColors = listOf(0xFF2C3E50, 0xFFFD746C),
                    mainDioramaEmoji = "🤖",
                    floatingDioramaEmojis = listOf("⚙️", "🔋", "🌱", "📚")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = "Elena invitó a R-7 a la Gran Feria de Inventos de la ciudad. El pequeño robot estaba muy nervioso: '¿Y si me equivoco delante de todos?'. En la feria, llegó el turno de R-7 de demostrar cómo organizaba una biblioteca.\n\nComenzó muy bien, pero con los aplausos del público se puso tan nervioso que dejó caer una pila entera de libros con un gran ¡Pum! El silencio se apoderó de la sala y R-7 quiso apagar sus luces y esconderse debajo de la mesa.",
                    backgroundGradientColors = listOf(0xFFF3904F, 0xFF3B4371),
                    mainDioramaEmoji = "👩‍🎨",
                    floatingDioramaEmojis = listOf("📚", "💥", "🤖", "⚙️")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = "De repente, un niño del público recogió un libro y sonrió: 'No pasa nada, yo también dejo caer mis cuadernos'. Otros lo ayudaron a ordenar la mesa rápidamente. R-7 vio que nadie se reía, todos querían ayudar. Respiró profundo y completó la demostración despacio, sin preocuparse por ser perfecto.\n\nAl terminar, un famoso inventor lo felicitó. R-7 pensó que hablaría de su rapidez, pero el inventor le dijo: 'La mejor parte fue que, después de equivocarte, no te rendiste'.",
                    backgroundGradientColors = listOf(0xFF5D4037, 0xFF3E2723),
                    mainDioramaEmoji = "👦",
                    floatingDioramaEmojis = listOf("🤖", "📚", "🤝", "🌟")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "Aquellas palabras cambiaron para siempre a R-7. Dejó de decir 'he fallado' y comenzó a decir: 'Hoy aprendí una nueva manera de hacerlo mejor'.\n\nSe convirtió en un robot sabio que ayudaba a los niños diciéndoles: 'Si practicas, algún día lo lograrás'. Comprendió que el verdadero éxito no consiste en ser perfecto, sino en seguir aprendiendo con entusiasmo de cada tropiezo.\n\nMoraleja: Equivocarse es parte del aprendizaje. Lo importante es no rendirse y seguir intentando con esfuerzo y confianza.",
                    backgroundGradientColors = listOf(0xFF11998E, 0xFF38EF7D),
                    mainDioramaEmoji = "💡",
                    floatingDioramaEmojis = listOf("🤖", "⚙️", "💪", "🌟")
                )
            )
            "dragon" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "En la gran Montaña de Humo vivía un joven dragón llamado Drake. A diferencia de sus hermanos mayores, que podían lanzar lenguas de fuego gigantescas para encender fogatas o asustar a los intrusos, Drake solo lograba escupir pequeñas burbujas de colores brillantes cada vez que abría la boca.",
                    backgroundGradientColors = listOf(0xFF1ABC9C, 0xFF16A085),
                    mainDioramaEmoji = "🐉",
                    floatingDioramaEmojis = listOf("💭", "✨", "🫧", "⛰️")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = "Sus hermanos se burlaban cariñosamente de él, pero Drake se sentía muy triste. Él pensaba que un dragón que no escupía fuego no servía para nada. Una tarde de invierno, una tormenta de nieve cubrió la montaña de frío y hielo, apagando todas las chimeneas del pueblo de los duendes.",
                    backgroundGradientColors = listOf(0xFF34495E, 0xFF2C3E50),
                    mainDioramaEmoji = "🌨️",
                    floatingDioramaEmojis = listOf("❄️", "🧊", "🏠", "🐉")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = "El frío era tan intenso que las familias temblaban en sus casas. Sus hermanos intentaron calentar el pueblo lanzando llamaradas de fuego, pero sus llamas eran tan calientes que quemaban los techos de paja y asustaban a los pequeños duendes.",
                    backgroundGradientColors = listOf(0xFFD35400, 0xFFE67E22),
                    mainDioramaEmoji = "🔥",
                    floatingDioramaEmojis = listOf("🧝", "🔥", "🏠", "💥")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "Entonces, Drake se acercó despacio. Abrió su boca y sopló con ternura. En lugar de quemar, sus burbujas de colores flotaron por las calles, estallando en ráfagas de calor suave y acogedor que envolvieron las casas sin dañarlas. Los duendes salieron a aplaudirle agradecidos.",
                    backgroundGradientColors = listOf(0xFF9B59B6, 0xFF8E44AD),
                    mainDioramaEmoji = "🫧",
                    floatingDioramaEmojis = listOf("🧝", "💖", "🌡️", "🫧")
                ),
                StoryPage(
                    pageNumber = 5,
                    textContent = "Al final del día, todos celebraron. El gran jefe de los dragones le dio una medalla dorada: 'El fuego destruye, pero la calidez de tu corazón protege'. Drake sonrió orgulloso, comprendiendo que ser diferente lo hacía único y valioso para ayudar a los demás.\n\nMoraleja: Tu verdadero valor no está en imitar a los demás, sino en usar tus talentos únicos para dar amor y protección a quienes lo necesitan.",
                    backgroundGradientColors = listOf(0xFFF1C40F, 0xFFD68910),
                    mainDioramaEmoji = "🏅",
                    floatingDioramaEmojis = listOf("🐉", "✨", "❤️", "🌟")
                )
            )
            "pirata" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "En el inmenso mar del Sur navegaba el barco de la capitana Marina, una joven pirata conocida por su valentía y por tener la brújula dorada más precisa de todo el océano. Con ella, Marina siempre sabía exactamente hacia dónde dirigir las velas para encontrar islas misteriosas.",
                    backgroundGradientColors = listOf(0xFF2980B9, 0xFF1F4E79),
                    mainDioramaEmoji = "🏴‍☠️",
                    floatingDioramaEmojis = listOf("⛵", "🌊", "🧭", "⚓")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = "Pero una noche de tormenta, una enorme ola sacudió el timón y la preciada brújula de Marina resbaló de sus manos, hundiéndose en las profundidades del agua. Marina se asustó muchísimo: '¡Sin mi brújula estamos perdidos! Jamás volveré a guiar a mi tripulación', lloró desesperada.",
                    backgroundGradientColors = listOf(0xFF2C3E50, 0xFF1A252F),
                    mainDioramaEmoji = "🌪️",
                    floatingDioramaEmojis = listOf("⚡", "🌧️", "🧭", "🌊")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = "Su fiel contramaestre, un loro llamado Pico, voló hasta su hombro: '¡Un verdadero capitán no solo mira un trozo de metal, mira a su tripulación y al cielo!'. Los demás marineros se acercaron y le dijeron: 'No necesitamos una brújula mágica, te tenemos a ti y podemos trabajar juntos'.",
                    backgroundGradientColors = listOf(0xFF27AE60, 0xFF2196F3),
                    mainDioramaEmoji = "🦜",
                    floatingDioramaEmojis = listOf("⛵", "🦜", "🤝", "🌌")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "Marina respiró hondo y confió. Esa noche, el timonel observó las estrellas, el carpintero midió la dirección del viento y el loro Pico voló alto para vigilar desde el mástil principal. Uniendo los talentos de todos, lograron esquivar los peligrosos arrecifes de coral.",
                    backgroundGradientColors = listOf(0xFF111E25, 0xFF000000),
                    mainDioramaEmoji = "🌌",
                    floatingDioramaEmojis = listOf("⭐", "🧭", "🌬️", "🪸")
                ),
                StoryPage(
                    pageNumber = 5,
                    textContent = "Al amanecer, avistaron una hermosa isla que no estaba en ningún mapa. Habían descubierto el tesoro más grande de todos: saber que el trabajo en equipo y la confianza mutua son la mejor guía cuando las tormentas de la vida nos hacen perder el rumbo.\n\nMoraleja: No dependas de un solo objeto o respuesta; el trabajo en equipo y la confianza en los demás nos ayudan a superar cualquier tempestad.",
                    backgroundGradientColors = listOf(0xFF2ECC71, 0xFF27AE60),
                    mainDioramaEmoji = "🏝️",
                    floatingDioramaEmojis = listOf("👑", "📦", "⚓", "🌟")
                )
            )
            "astronauta" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "El astronauta Leo vivía en la Estación Espacial Internacional, flotando entre estrellas resplandecientes y planetas de colores. Su trabajo era fascinante: estudiaba cometas helados, hacía experimentos con plantas espaciales y tomaba fotos espectaculares de la Tierra.",
                    backgroundGradientColors = listOf(0xFF34495E, 0xFF1B2A47),
                    mainDioramaEmoji = "🧑‍🚀",
                    floatingDioramaEmojis = listOf("🚀", "☄️", "🪐", "🌌")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = "Al principio todo era emocionante, pero después de varias semanas, Leo comenzó a sentir una gran tristeza. Al mirar por la escotilla la pequeña esfera azul de la Tierra, extrañaba el olor de la lluvia, el canto de los pájaros y, sobre todo, los cálidos abrazos de su familia.",
                    backgroundGradientColors = listOf(0xFF2C3E50, 0xFF1A252F),
                    mainDioramaEmoji = "🌍",
                    floatingDioramaEmojis = listOf("🌍", "🐦", "🌧️", "❤️")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = " 'Estoy tan lejos que siento que mi corazón está vacío', pensaba con nostalgia. Su compañera de misión, una experimentada astronauta llamada Sara, vio su semblante decaído y le obsequió un pequeño proyector holográfico con mensajes de video de sus seres queridos.",
                    backgroundGradientColors = listOf(0xFF1ABC9C, 0xFF16A085),
                    mainDioramaEmoji = "📽️",
                    floatingDioramaEmojis = listOf("🧑‍🚀", "👩‍🚀", "📦", "📽️")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "Leo encendió el proyector y vio a su hijo sonriendo y a su esposa saludándolo: '¡Te amamos, Leo! ¡Cada noche miramos al cielo y sabemos que estás ahí cuidándonos!'. Al escuchar sus voces, Leo comprendió que la distancia física no podía romper el lazo del amor sincero.",
                    backgroundGradientColors = listOf(0xFF8E44AD, 0xFF2C3E50),
                    mainDioramaEmoji = "💖",
                    floatingDioramaEmojis = listOf("🧒", "👩", "🌌", "✉️")
                ),
                StoryPage(
                    pageNumber = 5,
                    textContent = "Con renovada alegría, Leo completó su misión con éxito. Descubrió que no importa cuántos kilómetros de espacio nos separen de quienes amamos; los lazos del corazón son invisibles, indestructibles y brillan con más fuerza que cualquier estrella del universo.\n\nMoraleja: El verdadero amor no conoce de distancias físicas; la conexión con nuestros seres queridos nos acompaña dondequiera que vayamos.",
                    backgroundGradientColors = listOf(0xFF3498DB, 0xFF2980B9),
                    mainDioramaEmoji = "🛰️",
                    floatingDioramaEmojis = listOf("🚀", "✨", "🏡", "🌟")
                )
            )
            "ardilla" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "En un frondoso bosque lleno de pinos vivía Susi, una pequeña y traviesa ardilla muy orgullosa de su memoria. Cada otoño recolectaba cientos de nueces deliciosas y las enterraba en escondites secretos por todo el bosque para tener comida durante el frío invierno.",
                    backgroundGradientColors = listOf(0xFFD35400, 0xFF873600),
                    mainDioramaEmoji = "🐿️",
                    floatingDioramaEmojis = listOf("🌲", "🌰", "🍂", "🐿️")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = " 'Soy la ardilla más inteligente, ¡nunca olvido nada!', presumía siempre. Pero un invierno, tras caer una gran tormenta que cubrió todo el suelo de una gruesa capa de nieve blanca, Susi salió de su nido con hambre y... ¡oh, no! ¡No recordaba dónde había enterrado sus nueces!",
                    backgroundGradientColors = listOf(0xFFBDC3C7, 0xFF7F8C8D),
                    mainDioramaEmoji = "🌨️",
                    floatingDioramaEmojis = listOf("❄️", "🧊", "🌰", "🐿️")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = "Desesperada, comenzó a cavar por todas partes de forma caótica, pero solo encontraba hojas mojadas y barro frío. Susi se sintió muy frustrada y asustada, pero le daba demasiada vergüenza admitir su error ante las demás ardillas y pedirles ayuda.",
                    backgroundGradientColors = listOf(0xFFE67E22, 0xFFD35400),
                    mainDioramaEmoji = "🐾",
                    floatingDioramaEmojis = listOf("🪵", "❄️", "🍂", "🐾")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "Al verla tiritar de frío, su vecino, el conejo orejón Pepe, se acercó amablemente y le ofreció compartir sus zanahorias y piñones secos. Luego, llamaron a los castores y pájaros, quienes ayudaron a Susi a buscar pistas y desenterrar sus escondites bajo la nieve.",
                    backgroundGradientColors = listOf(0xFFF39C12, 0xFFD68910),
                    mainDioramaEmoji = "🐇",
                    floatingDioramaEmojis = listOf("🐇", "🥕", "🪵", "🤝")
                ),
                StoryPage(
                    pageNumber = 5,
                    textContent = "Con el estómago lleno y el corazón contento, Susi dio gracias a todos. Aprendió que no tiene nada de malo cometer errores o tener olvidos, y que pedir ayuda a nuestros amigos no nos hace débiles, sino que nos enseña a ser humildes y a vivir en comunidad.\n\nMoraleja: No dejes que el orgullo te impida pedir ayuda; la comunidad y los amigos están siempre listos para apoyarnos en los momentos difíciles.",
                    backgroundGradientColors = listOf(0xFF2ECC71, 0xFF16A085),
                    mainDioramaEmoji = "🤝",
                    floatingDioramaEmojis = listOf("🌰", "🤝", "🐿️", "🌟")
                )
            )
            "mago" -> listOf(
                StoryPage(
                    pageNumber = 1,
                    textContent = "En la gran Academia del Trueno estudiaba el joven mago Mateo. Todos los estudiantes de magia utilizaban varitas de madera pulida con gemas brillantes en la punta para lanzar hechizos de luces, hacer flotar objetos y transformar flores en mariposas gigantes.",
                    backgroundGradientColors = listOf(0xFF9B59B6, 0xFF4A154B),
                    mainDioramaEmoji = "🧙‍♂️",
                    floatingDioramaEmojis = listOf("⚡", "🪄", "🔮", "🏰")
                ),
                StoryPage(
                    pageNumber = 2,
                    textContent = "Mateo practicaba horas y horas, pero se sentía inseguro. Pensaba que toda la magia provenía únicamente de su hermosa varita plateada y que él no tenía ningún poder real por sí mismo. 'Sin mi varita, soy solo un chico común', se lamentaba con frecuencia.",
                    backgroundGradientColors = listOf(0xFF34495E, 0xFF1A252F),
                    mainDioramaEmoji = "🪄",
                    floatingDioramaEmojis = listOf("🪄", "📚", "🔮", "😔")
                ),
                StoryPage(
                    pageNumber = 3,
                    textContent = "El día de la gran prueba final para convertirse en Mago Oficial, mientras Mateo caminaba hacia el templo del examen, tropezó con una raíz en el camino y... ¡crac! Su varita se partió en dos pedazos. El pánico se apoderó de él: '¡Estoy perdido! Ya no podré hacer magia'.",
                    backgroundGradientColors = listOf(0xFFC0392B, 0xFF7F8C8D),
                    mainDioramaEmoji = "🪵",
                    floatingDioramaEmojis = listOf("💥", "🪜", "🪄", "👟")
                ),
                StoryPage(
                    pageNumber = 4,
                    textContent = "Entró al examen temblando. El Gran Maestro Mago le pidió el hechizo más difícil: hacer brotar agua de una roca seca. Mateo cerró los ojos con fuerza, extendió sus manos desnudas hacia la piedra, respiró hondo y concentró toda su energía y fe en que él sí podía lograrlo.",
                    backgroundGradientColors = listOf(0xFF1ABC9C, 0xFF2C3E50),
                    mainDioramaEmoji = "🪨",
                    floatingDioramaEmojis = listOf("🪨", "⚡", "🧘", "👋")
                ),
                StoryPage(
                    pageNumber = 5,
                    textContent = "¡Para sorpresa de todos, de la roca brotó un manantial de agua pura y cristalina! El Gran Maestro sonrió y le dijo: 'La varita es solo un canal. La verdadera magia siempre estuvo dentro de ti, en tu mente y tu corazón'. Mateo se graduó con honores, lleno de confianza.\n\nMoraleja: Los objetos o herramientas que usas solo te ayudan, pero tu verdadera fuerza y capacidad nacen de la confianza que tienes en ti mismo.",
                    backgroundGradientColors = listOf(0xFFF1C40F, 0xFFD68910),
                    mainDioramaEmoji = "🌊",
                    floatingDioramaEmojis = listOf("🧙‍♂️", "🌊", "🎓", "🌟")
                )
            )
            else -> emptyList()
        }
    }
}
