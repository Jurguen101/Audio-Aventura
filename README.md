# Audio Aventura 🎧✨

Audio Aventura es una aplicación educativa e interactiva que combina narración de historias con juegos de trivia. Presenta una encantadora estética visual inspirada en recortes de papel y cartón. 

Este es un proyecto conjunto desarrollado por la **UNAN-Managua** y la **UNI**.

## 🌟 Características Principales

* **Narración de Voz (Text-to-Speech):** La aplicación lee en voz alta los fragmentos de la historia y las preguntas de trivia para lograr una inmersión profunda y mejorar la accesibilidad.
* **Sistema de Trivia y Recompensas:** Los usuarios responden preguntas basadas en la historia. Al responder correctamente, ganan "estrellas de papel". En caso de error, pueden usar un "comodín" para reintentar.
* **Estética de "Papel":** Todo el diseño de interfaz (UI) está construido con Jetpack Compose para simular texturas de cartón, costuras, recortes de papel y colores cálidos.
* **Persistencia Local:** El progreso del jugador, las estrellas obtenidas, los personajes desbloqueados y los capítulos completados se guardan de manera segura en el dispositivo mediante Room Database.
* **Pantallas de Créditos Animadas:** Una introducción fluida (Fade-In/Fade-Out) que reconoce el esfuerzo de las instituciones (UNAN y UNI) y los creadores del concepto.
* **Efectos de Sonido:** Integración de sonidos de interacción (pops, aciertos, errores y fanfarrias) a través de un `SoundManager` nativo.

## 🛠 Tecnologías Utilizadas

* **Lenguaje:** Kotlin
* **Interfaz de Usuario (UI):** Jetpack Compose (Material 3)
* **Arquitectura:** MVVM (Model-View-ViewModel) con Clean Architecture
* **Almacenamiento Local:** Room Database (SQLite)
* **Asincronía:** Kotlin Coroutines y StateFlow / SharedFlow
* **Accesibilidad/Audio:** Android TextToSpeech (TTS) y SoundPool
* **Diseño Responsivo:** Soporte para orientación vertical (Portrait) y horizontal (Landscape), ajustando la grilla de trivia dinámicamente.

## 📂 Estructura del Proyecto

El código fuente principal se encuentra en `app/src/main/java/com/example/`:

* **`data/`**: Contiene la configuración de Room (`AppDatabase`), los Data Access Objects (DAOs como `AdventureDao`), los modelos de datos (`AdventureChapter`, `CharacterProfile`) y el repositorio principal (`StoryRepository`) que maneja la lógica de negocio y persistencia.
* **`ui/`**:
  * **`MainViewModel.kt`**: El cerebro de la aplicación. Maneja el estado global, la navegación, el Text-to-Speech y la lógica de la trivia.
  * **`screens/`**: Contiene todas las pantallas construidas con Compose (`CreditsScreen`, `HomeScreen`, `StoryScreen`, `TriviaScreen`, `CharactersScreen`, etc.).
  * **`components/`**: Componentes visuales reutilizables con la estética del juego (ej. `CardboardContainer`, `CardboardButton`).
  * **`theme/`**: Definición de la paleta de colores (`PaperMarioColors`), tipografía y temática general de Compose.
* **`utils/`**:
  * **`SoundManager.kt`**: Clase utilitaria para cargar y reproducir efectos de sonido (SFX) sin interrumpir el audio principal o el TTS.

## 🚀 Flujo de la Aplicación

1. **CreditsScreen:** Pantalla inicial animada mostrando los logos de las universidades e integrantes del proyecto.
2. **HomeScreen:** Menú principal donde el usuario puede ver sus estadísticas (estrellas) y elegir un capítulo para jugar.
3. **StoryPreview / StoryScreen:** El usuario escucha y lee la aventura por fragmentos.
4. **TriviaScreen:** Tras la historia, el usuario es puesto a prueba. Gana estrellas por cada respuesta correcta.
5. **CharactersScreen:** (Inventario/Personajes) El usuario puede gastar sus estrellas para desbloquear aliados y visualizar su colección.

## 🎨 Recursos Gráficos y Sonoros

* Las imágenes de créditos (`creditos_1.png` y `creditos_2.png`) se encuentran en la carpeta `res/drawable/`.
* La aplicación se adapta automáticamente si faltan las imágenes, mostrando un marcador de posición, lo que asegura que no haya crasheos por recursos ausentes.
