package com.example.examen_iplacex.ws

import com.squareup.moshi.Json


data class Dolar(
    @Json(name = "codigo")
    val codigo: String,

    @Json(name = "nombre")
    val nombre: String,

    @Json(name = "unidad_medida")
    val unidadMedida: String,

    @Json(name = "serie")
    val serie: List<SerieItem>
)


data class SerieItem(
    @Json(name = "fecha")
    val fecha: String,

    @Json(name = "valor")
    val valor: Double
)
