# Master Martini · App de contenidos técnicos

App móvil para Android pensada para los clientes de **Master Martini Chile**, una empresa que vende insumos para panaderías, pastelerías, heladerías y negocios gastronómicos.

La idea es simple: que un emprendedor pueda encontrar **recetas, videos y cursos** en un solo lugar, desde su teléfono, y que la empresa conozca mejor a sus clientes para ofrecerles contenido según sus intereses.

> Proyecto académico de la asignatura **DSY1105 Desarrollo de Aplicaciones Móviles** (Duoc UC), Evaluación Parcial 2.
> Desarrollado por **Felipe Echeverría**.
> Todos los datos de la app son **ficticios**.

---

## ¿Para quién es?

Para dueños y trabajadores de negocios gastronómicos. Muchos de ellos tienen más de 50 años y no usan tanto la tecnología, por eso la app usa:

- textos grandes y una opción de **letra aún más grande**;
- botones amplios y fáciles de tocar;
- mensajes de error claros, con texto e ícono (no solo color rojo).

---

## ¿Qué se puede hacer?

| Pantalla | Qué hace |
|---|---|
| **Carga** | Muestra el logo mientras la app inicia. Si ya te registraste antes, te lleva directo al inicio. |
| **Registro** | Te pide nombre, correo, teléfono, RUT de tu empresa, tipo de negocio y contraseña. Si algo está mal escrito, te avisa en ese mismo campo y el aviso desaparece cuando lo corriges. |
| **Inicio** | Te saluda por tu nombre y muestra un curso destacado, las líneas de negocio y las últimas recetas. Tiene un menú lateral (☰). |
| **Contenidos** | Lista de recetas, videos y cursos. Puedes filtrar por tipo y tocar uno para ver su detalle. En tablet, la lista y el detalle se ven lado a lado. |
| **Perfil** | Muestra tus datos y tus intereses. Puedes poner una foto de perfil **tomándola con la cámara** o **eligiéndola de la galería**. |
| **Preferencias** | Activa la **letra grande**, los avisos de nuevos cursos y elige los temas que te interesan. Al guardar, aparece un mensaje animado de confirmación. |

Lo que registras y tus preferencias **quedan guardados en el teléfono**, así que siguen ahí aunque cierres la app.

---

## ¿Cómo está construida?

La app está hecha en **Kotlin** con **Jetpack Compose** y **Material 3**, y sigue el patrón **MVVM**, que separa el código en capas para que cada parte tenga una sola tarea:

```
app/src/main/java/cl/duoc/mastermartini/
├── model/        Cómo son los datos (usuario, contenido, preferencias)
├── repository/   De dónde salen y dónde se guardan los datos (DataStore)
├── viewmodel/    La lógica: validaciones, filtros y estado de cada pantalla
├── navigation/   Las rutas entre pantallas
└── ui/
    ├── screens/      Las pantallas
    ├── components/   Piezas reutilizables (barras, foto de perfil, imágenes)
    ├── theme/        Colores y tamaños de letra
    └── utils/        Detección del tamaño de pantalla (teléfono o tablet)
```

En pocas palabras: **la pantalla solo muestra y avisa**, el **ViewModel decide** (por ejemplo, si un RUT es válido) y el **repositorio guarda**.

### Funciones destacadas

- **Navegación** con barra inferior, menú lateral y botón para volver.
- **Formulario validado**: correo con formato correcto, teléfono de 9 dígitos y RUT con dígito verificador.
- **Guardado local** con DataStore: el usuario registrado y sus preferencias.
- **Animaciones**: indicador de carga, cambio de color animado y mensaje que aparece y desaparece.
- **Recursos del teléfono**: cámara (con permiso) y galería.
- **Diseño adaptable**: en pantallas grandes cambia la distribución.
- **Pruebas automáticas** de las validaciones y del filtro de contenidos.

---

## ¿Cómo ejecutarla?

**Necesitas:**
- Una versión reciente de **Android Studio** (compatible con Android Gradle Plugin 9.4).
- Un teléfono o emulador con **Android 13 o superior**.

**Pasos:**
1. Clona el repositorio:
   ```bash
   git clone https://github.com/fssepul-prog/Master-Martini-Chile-SPA.git
   ```
2. En Android Studio, ve a **File → Open** y elige la carpeta del proyecto.
3. Espera a que termine de sincronizar (la primera vez descarga lo necesario).
4. Elige un emulador o conecta tu teléfono con la **depuración USB** activada.
5. Presiona **Run ▶**.

**Para probar la app:**
- Usa un RUT de ejemplo válido: `76.123.456-0`.
- En **Perfil**, prueba la cámara y la galería (en un teléfono real funciona mejor que en el emulador).
- Para volver a ver el registro, borra los datos de la app desde los ajustes del teléfono.

**Para correr las pruebas automáticas:**
```bash
./gradlew testDebugUnitTest
```
