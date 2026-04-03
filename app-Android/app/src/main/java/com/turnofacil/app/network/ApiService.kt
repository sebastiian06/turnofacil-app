package com.turnofacil.app.network

import com.turnofacil.app.model.Servicio
import com.turnofacil.app.model.Turno
import com.turnofacil.app.model.HorariosResponse
import com.turnofacil.app.model.TurnoResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("api/servicios")
    fun obtenerServicios(): Call<List<Servicio>>

    @GET("api/servicios/{id}")
    fun obtenerServicio(
        @Path("id") id: Int
    ): Call<Servicio>

    @GET("api/servicios/{id}/horarios-disponibles")
    fun obtenerHorariosDisponibles(
        @Path("id") id: Int,
        @Query("fecha") fecha: String
    ): Call<HorariosResponse>  // ← Recibe HorariosResponse

    @POST("api/turnos")
    fun crearTurno(
        @Body turno: Turno  // ← Envía Turno
    ): Call<TurnoResponse>  // ← Recibe TurnoResponse
}