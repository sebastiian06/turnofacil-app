package com.turnofacil.app.model

data class TurnoResponse(
    val success: Boolean,
    val message: String,
    val turno: TurnoData?
)

data class TurnoData(
    val id: Int,
    val nombre_cliente: String,
    val documento: String,
    val fecha: String,
    val hora: String,
    val servicio_id: Int
)