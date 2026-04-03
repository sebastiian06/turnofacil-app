package com.turnofacil.app.model

data class Servicio(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val duracion_minutos: Int,
    val hora_inicio: String,
    val hora_fin: String
)