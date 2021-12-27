package com.nombre.testeotecnico.model

import java.time.LocalDate

data class actividad(
    val primary: Int? = 0,
    var titulo: String = "",
    var contenido: String = "",
    var fechacreada: LocalDate=LocalDate.now(),
    var fechafinalizada: LocalDate=LocalDate.now(),
    var bole: Boolean = false
)
