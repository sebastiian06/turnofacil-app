package com.turnofacil.app

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.turnofacil.app.model.Turno
import com.turnofacil.app.model.HorariosResponse
import com.turnofacil.app.model.TurnoResponse
import com.turnofacil.app.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ReservarTurnoActivity : AppCompatActivity() {

    private lateinit var inputNombre: EditText
    private lateinit var inputDocumento: EditText
    private lateinit var textViewFecha: TextView
    private lateinit var buttonSeleccionarFecha: Button
    private lateinit var spinnerHorarios: Spinner
    private lateinit var botonConfirmar: Button
    private lateinit var textViewServicio: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewInfoCupos: TextView

    private var servicioId: Int = 0
    private var horariosDisponibles: List<String> = emptyList()
    private var fechaSeleccionada: String = ""
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservar_turno)

        inputNombre = findViewById(R.id.inputNombre)
        inputDocumento = findViewById(R.id.inputDocumento)
        textViewFecha = findViewById(R.id.textViewFecha)
        buttonSeleccionarFecha = findViewById(R.id.buttonSeleccionarFecha)
        spinnerHorarios = findViewById(R.id.spinnerHorarios)
        botonConfirmar = findViewById(R.id.botonConfirmar)
        textViewServicio = findViewById(R.id.textViewServicio)
        progressBar = findViewById(R.id.progressBar)
        textViewInfoCupos = findViewById(R.id.textViewInfoCupos)

        servicioId = intent.getIntExtra("servicio_id", 0)
        val servicioNombre = intent.getStringExtra("servicio_nombre") ?: "Servicio"

        textViewServicio.text = "Reservando: $servicioNombre"

        // Establecer fecha actual como predeterminada
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fechaSeleccionada = dateFormat.format(Date())
        textViewFecha.text = "Fecha seleccionada: $fechaSeleccionada"

        // Cargar horarios para la fecha actual
        cargarHorariosDisponibles()

        buttonSeleccionarFecha.setOnClickListener {
            mostrarDatePicker()
        }

        botonConfirmar.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val documento = inputDocumento.text.toString().trim()
            val horaSeleccionada = if (spinnerHorarios.selectedItem != null) {
                spinnerHorarios.selectedItem.toString()
            } else {
                ""
            }

            if (nombre.isEmpty()) {
                inputNombre.error = "Ingrese su nombre"
                return@setOnClickListener
            }

            if (documento.isEmpty()) {
                inputDocumento.error = "Ingrese su documento"
                return@setOnClickListener
            }

            if (horaSeleccionada.isEmpty() || horaSeleccionada == "No hay horarios disponibles") {
                Toast.makeText(this, "Seleccione un horario válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val turno = Turno(
                nombre_cliente = nombre,
                documento = documento,
                fecha = fechaSeleccionada,
                hora = horaSeleccionada,
                servicio_id = servicioId
            )

            crearTurno(turno)
        }
    }

    private fun mostrarDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Formatear fecha
                val fecha = Calendar.getInstance()
                fecha.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                fechaSeleccionada = dateFormat.format(fecha.time)
                textViewFecha.text = "Fecha seleccionada: $fechaSeleccionada"

                // Recargar horarios para la nueva fecha
                cargarHorariosDisponibles()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // No permitir seleccionar fechas pasadas
        datePickerDialog.datePicker.minDate = Date().time
        datePickerDialog.show()
    }

    private fun cargarHorariosDisponibles() {
        progressBar.visibility = ProgressBar.VISIBLE
        spinnerHorarios.isEnabled = false
        textViewInfoCupos.text = "Cargando horarios..."
        botonConfirmar.isEnabled = false

        val call = RetrofitClient.instance.obtenerHorariosDisponibles(servicioId, fechaSeleccionada)

        call.enqueue(object : Callback<HorariosResponse> {
            override fun onResponse(call: Call<HorariosResponse>, response: Response<HorariosResponse>) {
                progressBar.visibility = ProgressBar.GONE

                if (response.isSuccessful) {
                    val horariosResponse = response.body()

                    if (horariosResponse != null) {
                        horariosDisponibles = horariosResponse.horarios_disponibles

                        // Mostrar información de cupos
                        textViewInfoCupos.text = """
                            Cupos totales: ${horariosResponse.total_horarios}
                            Cupos ocupados: ${horariosResponse.horarios_ocupados}
                            Cupos disponibles: ${horariosResponse.horarios_libres}
                        """.trimIndent()

                        if (horariosDisponibles.isEmpty()) {
                            Toast.makeText(
                                this@ReservarTurnoActivity,
                                "No hay horarios disponibles para el ${horariosResponse.fecha}",
                                Toast.LENGTH_LONG
                            ).show()
                            botonConfirmar.isEnabled = false
                            val adapter = ArrayAdapter(
                                this@ReservarTurnoActivity,
                                android.R.layout.simple_spinner_item,
                                listOf("No hay horarios disponibles")
                            )
                            spinnerHorarios.adapter = adapter
                        } else {
                            botonConfirmar.isEnabled = true
                            val adapter = ArrayAdapter(
                                this@ReservarTurnoActivity,
                                android.R.layout.simple_spinner_item,
                                horariosDisponibles
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerHorarios.adapter = adapter
                            spinnerHorarios.isEnabled = true
                        }
                    }
                } else {
                    val errorMsg = try {
                        response.errorBody()?.string() ?: "Error al cargar horarios"
                    } catch (e: Exception) {
                        "Error al cargar horarios"
                    }
                    Toast.makeText(
                        this@ReservarTurnoActivity,
                        errorMsg,
                        Toast.LENGTH_LONG
                    ).show()
                    textViewInfoCupos.text = "Error al cargar los horarios"
                }
            }

            override fun onFailure(call: Call<HorariosResponse>, t: Throwable) {
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(
                    this@ReservarTurnoActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
                textViewInfoCupos.text = "Error de conexión"
            }
        })
    }

    private fun crearTurno(turno: Turno) {
        progressBar.visibility = ProgressBar.VISIBLE
        botonConfirmar.isEnabled = false

        val call = RetrofitClient.instance.crearTurno(turno)

        call.enqueue(object : Callback<TurnoResponse> {
            override fun onResponse(call: Call<TurnoResponse>, response: Response<TurnoResponse>) {
                progressBar.visibility = ProgressBar.GONE
                botonConfirmar.isEnabled = true

                if (response.isSuccessful) {
                    val turnoResponse = response.body()
                    if (turnoResponse?.success == true) {
                        // Navegar a la pantalla de confirmación
                        val intent = Intent(this@ReservarTurnoActivity, ConfirmacionReservaActivity::class.java)
                        intent.putExtra("servicio_nombre", textViewServicio.text.toString().replace("Reservando: ", ""))
                        intent.putExtra("fecha", fechaSeleccionada)
                        intent.putExtra("hora", if (spinnerHorarios.selectedItem != null) spinnerHorarios.selectedItem.toString() else "")
                        intent.putExtra("nombre_cliente", inputNombre.text.toString().trim())
                        intent.putExtra("documento", inputDocumento.text.toString().trim())
                        startActivity(intent)
                        finish() // Cerrar la actividad de reserva
                    } else {
                        Toast.makeText(
                            this@ReservarTurnoActivity,
                            turnoResponse?.message ?: "Error al reservar turno",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    // Leer mensaje de error del backend
                    val errorMsg = try {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody?.contains("horario ya está reservado") == true) {
                            "Este horario ya fue reservado. Por favor, seleccione otro."
                        } else if (errorBody?.contains("fuera de rango") == true) {
                            "El horario seleccionado está fuera del horario de atención."
                        } else {
                            "Error al reservar turno. Intente nuevamente."
                        }
                    } catch (e: Exception) {
                        "Error al reservar turno"
                    }

                    Toast.makeText(
                        this@ReservarTurnoActivity,
                        errorMsg,
                        Toast.LENGTH_LONG
                    ).show()

                    // Recargar horarios disponibles
                    cargarHorariosDisponibles()
                }
            }

            override fun onFailure(call: Call<TurnoResponse>, t: Throwable) {
                progressBar.visibility = ProgressBar.GONE
                botonConfirmar.isEnabled = true

                Toast.makeText(
                    this@ReservarTurnoActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}