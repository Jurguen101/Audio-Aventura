package com.example.data

data class TriviaQuestion(
    val id: Int,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val difficultyLabel: String // "Fácil", "Medio-Fácil", "Medio", "Difícil", "Muy Difícil"
)

object TriviaProvider {
    fun getQuestionsForChapter(chapterId: String): List<TriviaQuestion> {
        return when (chapterId) {
            "ninja" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Quién es el pequeño ninja impaciente del cuento?",
                    options = listOf("A) El maestro Hayato", "B) El ninja Ken", "C) El gorrión alegre"),
                    correctAnswer = "B) El ninja Ken",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Qué accidente le ocurrió a Ken por querer cruzar la cuerda floja sin practicar?",
                    options = listOf("A) Cayó sobre un montón de hojas secas", "B) Se le voló el sombrero", "C) Se mojó con un balde de agua"),
                    correctAnswer = "A) Cayó sobre un montón de hojas secas",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Qué ocurrió cuando Ken intentó lanzar las estrellas de entrenamiento?",
                    options = listOf("A) Le dio al centro del blanco", "B) Rompió una cuerda y le cayó un balde de agua en la cabeza", "C) Perdió las estrellas en el río"),
                    correctAnswer = "B) Rompió una cuerda y le cayó un balde de agua en la cabeza",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿De quién aprendió Ken la importancia de ir ramita por ramita?",
                    options = listOf("A) De un pequeño gorrión que hacía su nido", "B) Del maestro Hayato", "C) De un castor en el río"),
                    correctAnswer = "A) De un pequeño gorrión que hacía su nido",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Qué valiosa lección aprendió Ken al final del entrenamiento?",
                    options = listOf("A) Que ganar requiere ser el más veloz de todos", "B) Que la paciencia, la práctica y el esfuerzo diario permiten lograr grandes metas", "C) Que el equilibrio no es importante"),
                    correctAnswer = "B) Que la paciencia, la práctica y el esfuerzo diario permiten lograr grandes metas",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            "fantasma" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Por qué Gaspar el fantasmita prefería quedarse escondido en la torre?",
                    options = listOf("A) Porque no le gustaba el frío de la noche", "B) Porque tenía miedo de que se burlaran de él por no dar miedo", "C) Porque prefería jugar en silencio"),
                    correctAnswer = "B) Porque tenía miedo de que se burlaran de él por no dar miedo",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Quién era el mejor amigo de Gaspar que se posaba en el tejado?",
                    options = listOf("A) El viejo búho Bruno", "B) Un murciélago veloz", "C) Un gatito gris"),
                    correctAnswer = "A) El viejo búho Bruno",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Qué descubrió Gaspar sobre la niña Sofía cuando llegó a la escalera oscura?",
                    options = listOf("A) Que Sofía no le temía a nada", "B) Que Sofía también tenía miedo y abrazó su linterna", "C) Que Sofía andaba buscando fantasmas"),
                    correctAnswer = "B) Que Sofía también tenía miedo y abrazó su linterna",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿Qué frase valiosa le dijo el búho Bruno a Gaspar sobre el miedo?",
                    options = listOf("A) Que los fantasmas nunca deben sentir temor", "B) Que todos sienten miedo, pero lo importante es no dejar que el miedo decida por nosotros", "C) Que es mejor quedarse escondido para siempre"),
                    correctAnswer = "B) Que todos sienten miedo, pero lo importante es no dejar que el miedo decida por nosotros",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Cuál es la moraleja que nos enseña la tierna historia de Gaspar?",
                    options = listOf("A) Que ser valiente significa no tener miedo jamás", "B) Que ser valiente no es la ausencia de miedo, sino enfrentarlo con confianza y ayuda", "C) Que los castillos antiguos son aburridos"),
                    correctAnswer = "B) Que ser valiente no es la ausencia de miedo, sino enfrentarlo con confianza y ayuda",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            "vaquero" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Qué problema de actitud tenía el joven vaquero Tomás?",
                    options = listOf("A) Siempre llegaba tarde a todas partes", "B) No le gustaba compartir sus pertenencias", "C) Tenía miedo de montar a caballo"),
                    correctAnswer = "B) No le gustaba compartir sus pertenencias",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Cómo se llamaba el fiel caballo de Tomás?",
                    options = listOf("A) Rayo", "B) Relámpago", "C) Trueno"),
                    correctAnswer = "B) Relámpago",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Qué le sucedió a Tomás durante su viaje por la llanura?",
                    options = listOf("A) Perdió su sombrero de ala ancha", "B) Una fuerte tormenta de arena lo desorientó y se perdió", "C) Su caballo escapó al anochecer"),
                    correctAnswer = "B) Una fuerte tormenta de arena lo desorientó y se perdió",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿Quiénes ayudaron a Tomás compartiendo comida y agua en la fogata?",
                    options = listOf("A) Otros vaqueros del pueblo", "B) La anciana y el niño a quienes él les había negado agua por la mañana", "C) Unos amigables guardabosques"),
                    correctAnswer = "B) La anciana y el niño a quienes él les había negado agua por la mañana",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Qué gran lección aprendió Tomás sobre el valor de compartir?",
                    options = listOf("A) Que compartir con los demás fortalece la amistad, genera confianza y nos ayuda en la dificultad", "B) Que es mejor viajar con dos cantimploras y no hablar con nadie", "C) Que el desierto siempre es un lugar peligroso para un vaquero"),
                    correctAnswer = "A) Que compartir con los demás fortalece la amistad, genera confianza y nos ayuda en la dificultad",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            "rey" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Qué defecto inicial tenía el joven rey Adrián al gobernar?",
                    options = listOf("A) No le interesaba el bienestar de su reino", "B) Pensaba que siempre tenía la respuesta correcta y no escuchaba a nadie", "C) No sabía leer mapas reales"),
                    correctAnswer = "B) Pensaba que siempre tenía la respuesta correcta y no escuchaba a nadie",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Qué gran problema afectó a los cultivos del reino?",
                    options = listOf("A) Una plaga de langostas de papel", "B) El gran estanque de agua se secó", "C) Una tormenta de granizo"),
                    correctAnswer = "B) El gran estanque de agua se secó",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Por qué falló el enorme canal que el rey mandó construir?",
                    options = listOf("A) Porque el canal estaba mal cavado", "B) Porque el problema real era una rama caída que bloqueaba el paso del agua", "C) Porque los campesinos se negaron a trabajar"),
                    correctAnswer = "B) Porque el problema real era una rama caída que bloqueaba el paso del agua",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿Quién había visto la rama bloqueando el paso días atrás?",
                    options = listOf("A) La consejera doña Elvira", "B) Una niña pequeña llamada Clara", "C) Un comerciante de la plaza"),
                    correctAnswer = "B) Una niña pequeña llamada Clara",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Qué hermosa iniciativa instauró el rey para escuchar a todos?",
                    options = listOf("A) La 'Hora de las Buenas Ideas' para recibir propuestas de todo su pueblo", "B) Una lista de reglas escritas en piedra", "C) Un concurso de dibujo semanal"),
                    correctAnswer = "A) La 'Hora de las Buenas Ideas' para recibir propuestas de todo su pueblo",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            "robot" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Quién era la brillante inventora que creó al robot R-7?",
                    options = listOf("A) Clara la niña", "B) Elena la inventora", "C) Doña Elvira"),
                    correctAnswer = "B) Elena la inventora",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Qué situación le causaba mucho estrés a R-7?",
                    options = listOf("A) Quería hacer todo perfectamente desde el primer intento and se frustraba al fallar", "B) No le gustaba ordenar libros en la biblioteca", "C) Le temía a la Gran Feria de Inventos"),
                    correctAnswer = "A) Quería hacer todo perfectamente desde el primer intento and se frustraba al fallar",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Qué percance sufrió R-7 durante la demostración en la Gran Feria?",
                    options = listOf("A) Se le descargó la batería por completo", "B) Dejó caer una pila entera de libros por ponerse nervioso", "C) Rompió una maceta al regar una planta"),
                    correctAnswer = "B) Dejó caer una pila entera de libros por ponerse nervioso",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿Qué le dijo el famoso inventor que felicitó a R-7?",
                    options = listOf("A) Que su velocidad para acomodar libros fue asombrosa", "B) Que la mejor parte fue que, tras equivocarse, no se rindió y continuó", "C) Que los robots nunca deben cometer fallos"),
                    correctAnswer = "B) Que la mejor parte fue que, tras equivocarse, no se rindió y continuó",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Cómo cambió la actitud de R-7 respecto a sus propios errores?",
                    options = listOf("A) Dejó de decir 'he fallado' y empezó a decir 'hoy aprendí una nueva manera de hacerlo mejor'", "B) Decidió no volver a participar en ferias de inventos", "C) Decidió que ordenar libros era demasiado difícil"),
                    correctAnswer = "A) Dejó de decir 'he fallado' y empezó a decir 'hoy aprendí una nueva manera de hacerlo mejor'",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            "dragon" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Qué salía de la boca de Drake en lugar de fuego?",
                    options = listOf("A) Humo negro denso", "B) Pequeñas burbujas de colores brillantes", "C) Hielo picado"),
                    correctAnswer = "B) Pequeñas burbujas de colores brillantes",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Por qué se sentía triste Drake al principio de la historia?",
                    options = listOf("A) Porque no tenía escamas verdes", "B) Porque pensaba que un dragón que no escupía fuego no servía para nada", "C) Porque sus hermanos eran malos con él"),
                    correctAnswer = "B) Porque pensaba que un dragón que no escupía fuego no servía para nada",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Qué gran problema ocurrió en el pueblo de los duendes?",
                    options = listOf("A) El estanque del pueblo se secó", "B) Una tormenta de nieve apagó las chimeneas y causó un frío intenso", "C) Un ogro gigante los asustó"),
                    correctAnswer = "B) Una tormenta de nieve apagó las chimeneas y causó un frío intenso",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿Por qué fallaron las llamaradas de los hermanos mayores de Drake?",
                    options = listOf("A) Eran tan calientes que quemaban los techos y asustaban a los duendes", "B) Se apagaron de inmediato por el viento", "C) No tenían suficiente poder"),
                    correctAnswer = "A) Eran tan calientes que quemaban los techos y asustaban a los duendes",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Qué gran lección aprendió Drake al final de su aventura?",
                    options = listOf("A) Que debía aprender a lanzar fuego como los demás", "B) Que ser diferente lo hacía único y valioso para ayudar con calidez", "C) Que los duendes sabían hacer mejores medallas"),
                    correctAnswer = "B) Que ser diferente lo hacía único y valioso para ayudar con calidez",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            "pirata" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Qué valioso objeto perdió la capitana Marina durante la tormenta?",
                    options = listOf("A) Su sombrero pirata", "B) Su brújula dorada tan precisa", "C) El mapa de la isla misteriosa"),
                    correctAnswer = "B) Su brújula dorada tan precisa",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Quién consoló a Marina diciéndole que un buen capitán mira a su tripulación?",
                    options = listOf("A) El loro Pico", "B) El carpintero del barco", "C) Un delfín del océano"),
                    correctAnswer = "A) El loro Pico",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Cómo lograron guiar el barco en la noche sin la brújula?",
                    options = listOf("A) Usando un mapa secreto", "B) Uniendo talentos (observando estrellas, viento y mástil)", "C) Esperando que amaneciera inmóviles"),
                    correctAnswer = "B) Uniendo talentos (observando estrellas, viento y mástil)",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿Qué peligro peligroso lograron esquivar trabajando en equipo?",
                    options = listOf("A) Un ataque de tiburones gigantes", "B) Unos arrecifes de coral muy peligrosos", "C) Un remolino gigante"),
                    correctAnswer = "B) Unos arrecifes de coral muy peligrosos",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Cuál fue el verdadero tesoro que descubrieron al llegar a la isla?",
                    options = listOf("A) Un cofre lleno de rubíes y monedas de oro", "B) Saber que el trabajo en equipo y la confianza mutua son la mejor guía", "C) Una estatua antigua de plata"),
                    correctAnswer = "B) Saber que el trabajo en equipo y la confianza mutua son la mejor guía",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            "astronauta" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Dónde vivía y trabajaba el astronauta Leo?",
                    options = listOf("A) En una base subterránea en Marte", "B) En la Estación Espacial Internacional", "C) En la Luna"),
                    correctAnswer = "B) En la Estación Espacial Internacional",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Qué comenzó a extrañar Leo después de varias semanas?",
                    options = listOf("A) Los postres espaciales", "B) El olor a lluvia, el canto de aves y los abrazos de su familia", "C) La falta de gravedad de la estación"),
                    correctAnswer = "B) El olor a lluvia, el canto de aves y los abrazos de su familia",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Qué le regaló su compañera astronauta Sara para animarlo?",
                    options = listOf("A) Un telescopio de alta tecnología", "B) Un proyector holográfico con mensajes en video de su familia", "C) Una planta espacial de menta"),
                    correctAnswer = "B) Un proyector holográfico con mensajes en video de su familia",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿Qué valioso descubrimiento hizo Leo sobre la distancia?",
                    options = listOf("A) Que la distancia física no rompe los lazos invisibles del amor sincero", "B) Que es imposible vivir lejos del hogar", "C) Que las estrellas brillan menos cuando estás triste"),
                    correctAnswer = "A) Que la distancia física no rompe los lazos invisibles del amor sincero",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Cómo completó Leo su misión en el espacio?",
                    options = listOf("A) Con desgana y ganas de regresar", "B) Con éxito, renovada alegría y sintiéndose conectado con los suyos", "C) Dejándola incompleta"),
                    correctAnswer = "B) Con éxito, renovada alegría y sintiéndose conectado con los suyos",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            "ardilla" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Qué alimento recolectaba y escondía Susi durante el otoño?",
                    options = listOf("A) Manzanas rojas", "B) Nueces deliciosas", "C) Bayas del bosque"),
                    correctAnswer = "B) Nueces deliciosas",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Por qué Susi no podía encontrar sus nueces escondidas en invierno?",
                    options = listOf("A) Porque se las robaron los castores", "B) Porque una tormenta de nieve cubrió todo el suelo y perdió la memoria", "C) Porque las nueces se desvanecieron"),
                    correctAnswer = "B) Porque una tormenta de nieve cubrió todo el suelo y perdió la memoria",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Qué le impedía a Susi pedir ayuda al principio?",
                    options = listOf("A) Su orgullo y vergüenza de admitir su error", "B) Que no hablaba con ningún vecino", "C) Que prefería buscar sola bajo el hielo"),
                    correctAnswer = "A) Su orgullo y vergüenza de admitir su error",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿Quién fue el primer amigo en ofrecerle compartir su comida?",
                    options = listOf("A) El oso de la montaña", "B) El conejo Pepe con zanahorias y piñones", "C) El loro sabio"),
                    correctAnswer = "B) El conejo Pepe con zanahorias y piñones",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Qué gran lección aprendió Susi al final del invierno?",
                    options = listOf("A) Que buscar comida sola es más rápido", "B) Que cometer errores es normal y pedir ayuda nos une en comunidad", "C) Que es mejor comer solo zanahorias"),
                    correctAnswer = "B) Que cometer errores es normal y pedir ayuda nos une en comunidad",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            "mago" -> listOf(
                TriviaQuestion(
                    id = 1,
                    questionText = "¿Por qué Mateo el joven mago se sentía tan inseguro?",
                    options = listOf("A) Porque no recordaba las palabras mágicas", "B) Porque creía que toda la magia provenía de su varita y no de sí mismo", "C) Porque sus hechizos salían al revés"),
                    correctAnswer = "B) Porque creía que toda la magia provenía de su varita y no de sí mismo",
                    difficultyLabel = "Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 2,
                    questionText = "¿Qué accidente le sucedió a la varita de Mateo camino al examen?",
                    options = listOf("A) Se le cayó en un pantano profundo", "B) Se partió en dos pedazos al tropezar con una raíz", "C) Un duende travieso se la robó"),
                    correctAnswer = "B) Se partió en dos pedazos al tropezar con una raíz",
                    difficultyLabel = "Medio-Fácil ⭐"
                ),
                TriviaQuestion(
                    id = 3,
                    questionText = "¿Qué difícil tarea le pidió el Gran Maestro Mago en el examen final?",
                    options = listOf("A) Hacer flotar un libro pesado", "B) Hacer brotar agua pura de una roca seca", "C) Transformar una flor en mariposa"),
                    correctAnswer = "B) Hacer brotar agua pura de una roca seca",
                    difficultyLabel = "Medio ⭐"
                ),
                TriviaQuestion(
                    id = 4,
                    questionText = "¿Cómo logró Mateo realizar el hechizo sin su varita?",
                    options = listOf("A) Copiando a otro estudiante", "B) Respirando hondo y concentrando su fe en que sí podía lograrlo", "C) Usando un amuleto secreto de la academia"),
                    correctAnswer = "B) Respirando hondo y concentrando su fe en que sí podía lograrlo",
                    difficultyLabel = "Difícil ⭐"
                ),
                TriviaQuestion(
                    id = 5,
                    questionText = "¿Qué gran verdad le reveló el Gran Maestro sobre las varitas?",
                    options = listOf("A) Que la varita es solo un canal y la verdadera magia está dentro de uno", "B) Que las varitas plateadas son de mala calidad", "C) Que el examen era una simple ilusión"),
                    correctAnswer = "A) Que la varita es solo un canal y la verdadera magia está dentro de uno",
                    difficultyLabel = "Muy Difícil ⭐"
                )
            )
            else -> emptyList()
        }
    }
}
