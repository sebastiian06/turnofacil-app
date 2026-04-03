package com.turnofacil.app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.turnofacil.app.model.Servicio
import com.turnofacil.app.model.HorariosResponse
import com.turnofacil.app.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

class DetalleServicioActivity : AppCompatActivity() {

    private lateinit var nombreServicio: TextView
    private lateinit var descripcionServicio: TextView
    private lateinit var duracionServicio: TextView
    private lateinit var horarioAtencion: TextView
    private lateinit var cuposDisponibles: TextView
    private lateinit var botonReservar: Button

    private var servicioId: Int = 0
    private var servicioNombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_servicio)

        nombreServicio = findViewById(R.id.nombreServicio)
        descripcionServicio = findViewById(R.id.descripcionServicio)
        duracionServicio = findViewById(R.id.duracionServicio)
        horarioAtencion = findViewById(R.id.horarioAtencion)
        cuposDisponibles = findViewById(R.id.cuposDisponibles)
        botonReservar = findViewById(R.id.botonReservar)

        servicioId = intent.getIntExtra("servicio_id", 0)

        cargarDetalle(servicioId)

        botonReservar.setOnClickListener {
            val intent = Intent(this, ReservarTurnoActivity::class.java)
            intent.putExtra("servicio_id", servicioId)
            intent.putExtra("servicio_nombre", servicioNombre)
            startActivity(intent)
        }
    }

    private fun cargarDetalle(id: Int) {
        val call = RetrofitClient.instance.obtenerServicio(id)

        call.enqueue(object : Callback<Servicio> {
            override fun onResponse(call: Call<Servicio>, response: Response<Servicio>) {
                if (response.isSuccessful) {
                    val servicio = response.body()
                    servicio?.let {
                        servicioNombre = it.nombre
                        mostrarDetalleServicio(it)
                        calcularYCuposDisponibles(it)
                    }
                } else {
                    Toast.makeText(
                        this@DetalleServicioActivity,
                        "Error al cargar servicio",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Servicio>, t: Throwable) {
                Toast.makeText(
                    this@DetalleServicioActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun mostrarDetalleServicio(servicio: Servicio) {
        nombreServicio.text = servicio.nombre
        descripcionServicio.text = servicio.descripcion
        duracionServicio.text = "Duración: ${servicio.duracion_minutos} minutos"
        horarioAtencion.text = "Horario de atención: ${servicio.hora_inicio} - ${servicio.hora_fin}"
    }

    private fun calcularYCuposDisponibles(servicio: Servicio) {
        // Obtener fecha actual en formato YYYY-MM-DD
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaActual = dateFormat.format(Date())

        val call = RetrofitClient.instance.obtenerHorariosDisponibles(servicio.id, fechaActual)

        call.enqueue(object : Callback<HorariosResponse> {
            override fun onResponse(call: Call<HorariosResponse>, response: Response<HorariosResponse>) {
                if (response.isSuccessful) {
                    val horariosResponse = response.body()
                    if (horariosResponse != null) {
                        val cuposDisponiblesTexto = """
                            Cupos disponibles: ${horariosResponse.horarios_libres} de ${horariosResponse.total_horarios}
                            Horarios disponibles: ${horariosResponse.horarios_disponibles.size}
                        """.trimIndent()
                        cuposDisponibles.text = cuposDisponiblesTexto

                        // Mostrar primeros 3 horarios como ejemplo
                        if (horariosResponse.horarios_disponibles.isNotEmpty()) {
                            val horariosEjemplo = horariosResponse.horarios_disponibles.take(3).joinToString(", ")
                            cuposDisponibles.append("\nEjemplos: $horariosEjemplo...")
                        }
                    }
                } else {
                    cuposDisponibles.text = "No se pudieron cargar los cupos disponibles"
                }
            }

            override fun onFailure(call: Call<HorariosResponse>, t: Throwable) {
                cuposDisponibles.text = "Error al calcular cupos disponibles"
            }
        })
    }
}