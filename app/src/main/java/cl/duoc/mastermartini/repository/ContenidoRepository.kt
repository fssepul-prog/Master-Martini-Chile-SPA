package cl.duoc.mastermartini.repository
import cl.duoc.mastermartini.R
import cl.duoc.mastermartini.model.Contenido
import cl.duoc.mastermartini.model.TipoContenido

object ContenidoRepository {
    val lineasDeNegocio = listOf("Panadería y Pastelería", "Gelatería", "Food Service", "Retail")

    fun obtenerContenidos(): List<Contenido> = listOf(
        Contenido(1, TipoContenido.Receta, "Croissant con margarina vegetal",
            "60 min · Bollería",
            "Masa laminada paso a paso, con tiempos de reposo y temperatura del horno.",
            imagen = R.drawable.receta_croissant),

        Contenido(2, TipoContenido.Video, "Templado de chocolate",
            "12 min · Chocolatería",
            "Cómo templar chocolate para lograr brillo y un quiebre limpio.",
            imagen = R.drawable.video_templado),

        Contenido(3, TipoContenido.Curso, "Pastelería de vitrina",
            "6 módulos · Intermedio",
            "Montaje y terminación de tortas y postres para vitrina.",
            imagen = R.drawable.curso_vitrina),

        Contenido(4, TipoContenido.Receta, "Galletas de mantequilla",
            "30 min · Galletas",
            "Receta base para galletas de corte, ideal para producción diaria.",
            imagen = R.drawable.receta_galletas),

        Contenido(5, TipoContenido.Video, "Base de helado cremoso",
            "8 min · Heladería",
            "Preparación de una base neutra para helados artesanales.",
            R.drawable.video_helado),

        Contenido(6, TipoContenido.Receta, "Empanadas de horno",
            "50 min · Productos de horneo",
            "Masa y armado de empanadas con un dorado parejo.",
            imagen = R.drawable.receta_empanadas),
    )
}
