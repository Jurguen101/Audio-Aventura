package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProgress::class,
        StoryChapter::class,
        CollectibleCharacter::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun adventureDao(): AdventureDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "adventure_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed data on database creation
                        INSTANCE?.let { database ->
                            scope.launch(Dispatchers.IO) {
                                seedDatabase(database.adventureDao())
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedDatabase(dao: AdventureDao) {
            // Seed initial progress with 0 stars and 0 wildcards
            if (dao.getUserProgress() == null) {
                dao.saveUserProgress(UserProgress(id = 1, starsCount = 0, wildcardsCount = 0))
            }

            // Seed 10 story chapters from easiest (Level 1) to hardest (Level 10)
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

            // Seed 10 collectible characters for the shop
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
    }
}
