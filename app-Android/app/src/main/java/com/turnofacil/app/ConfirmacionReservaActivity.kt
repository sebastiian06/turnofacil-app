package com.turnofacil.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class ConfirmacionReservaActivity : AppCompatActivity() {

    private lateinit var textViewServicio: TextView
    private lateinit var textViewFecha: TextView
    private lateinit var textViewHora: TextView
    private lateinit var textViewNombre: TextView
    private lateinit var textViewDocumento: TextView
    private lateinit var buttonVolverInicio: Button
    private lateinit var buttonVerMisTurnos: Button
    private lateinit var buttonNuevaReserva: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacion_reserva)

        // Inicializar vistas
        textViewServicio = findViewById(R.id.textViewServicio)
        textViewFecha = findViewById(R.id.textViewFecha)
        textViewHora = findViewById(R.id.textViewHora)
        textViewNombre = findViewById(R.id.textViewNombre)
        textViewDocumento = findViewById(R.id.textViewDocumento)
        buttonVolverInicio = findViewById(R.id.buttonVolverInicio)
        buttonVerMisTurnos = findViewById(R.id.buttonVerMisTurnos)
        buttonNuevaReserva = findViewById(R.id.buttonNuevaReserva)

        // Obtener datos del Intent
        val servicioNombre = intent.getStringExtra("servicio_nombre") ?: "No especificado"
        val fecha = intent.getStringExtra("fecha") ?: "No especificada"
        val hora = intent.getStringExtra("hora") ?: "No especificada"
        val nombreCliente = intent.getStringExtra("nombre_cliente") ?: "No especificado"
        val documento = intent.getStringExtra("documento") ?: "No especificado"

        // Mostrar datos
        textViewServicio.text = servicioNombre
        textViewFecha.text = formatearFecha(fecha)
        textViewHora.text = hora
        textViewNombre.text = nombreCliente
        textViewDocumento.text = documento

        // Configurar botones
        buttonVolverInicio.setOnClickListener {
            volverAlInicio()
        }

        buttonVerMisTurnos.setOnClickListener {
            verMisTurnos(nombreCliente, documento)
        }

        buttonNuevaReserva.setOnClickListener {
            hacerNuevaReserva()
        }
    }

    private fun formatearFecha(fecha: String): String {
        // Convertir YYYY-MM-DD a formato más legible
        return try {
            val partes = fecha.split("-")
            if (partes.size == 3) {
                val año = partes[0]
                val mes = when (partes[1]) {
                    "01" -> "Enero"
                    "02" -> "Febrero"
                    "03" -> "Marzo"
                    "04" -> "Abril"
                    "05" -> "Mayo"
                    "06" -> "Junio"
                    "07" -> "Julio"
                    "08" -> "Agosto"
                    "09" -> "Septiembre"
                    "10" -> "Octubre"
                    "11" -> "Noviembre"
                    "12" -> "Diciembre"
                    else -> partes[1]
                }
                val dia = partes[2]
                "$dia de $mes de $año"
            } else {
                fecha
            }
        } catch (e: Exception) {
            fecha
        }
    }

    private fun volverAlInicio() {
        val intent = Intent(this, ListaServiciosActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun verMisTurnos(nombre: String, documento: String) {
        // Aquí puedes implementar la actividad para ver turnos del cliente
        // Por ahora, mostramos un mensaje y volvemos al inicio
        val intent = Intent(this, ListaServiciosActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()

        // Mostrar Toast correctamente
        Toast.makeText(this, "Próximamente: Ver mis turnos", Toast.LENGTH_SHORT).show()
    }

    private fun hacerNuevaReserva() {
        val intent = Intent(this, ListaServiciosActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    // Actualizar para usar OnBackPressedDispatcher (nueva forma en Android)
    override fun onBackPressed() {
        // Al presionar atrás, volver al inicio
        volverAlInicio()
    }
}