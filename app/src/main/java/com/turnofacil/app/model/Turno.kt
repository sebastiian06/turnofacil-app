package com.turnofacil.app.model

data class Turno(
    val nombre_cliente: String,
    val documento: String,
    val fecha: String,
    val hora: String,
    val servicio_id: Int
)