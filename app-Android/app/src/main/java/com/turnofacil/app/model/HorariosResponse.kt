package com.turnofacil.app.model

data class HorariosResponse(
    val fecha: String,
    val horarios_disponibles: List<String>,
    val total_horarios: Int,
    val horarios_ocupados: Int,
    val horarios_libres: Int
)