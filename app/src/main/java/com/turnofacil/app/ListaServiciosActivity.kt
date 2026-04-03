package com.turnofacil.app

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.turnofacil.app.model.Servicio
import com.turnofacil.app.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent


class ListaServiciosActivity : AppCompatActivity() {

    private lateinit var listaServicios: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_servicios)

        listaServicios = findViewById(R.id.listaServicios)

        cargarServicios()
    }

    private fun cargarServicios() {

        val call = RetrofitClient.instance.obtenerServicios()

        call.enqueue(object : Callback<List<Servicio>> {

            override fun onResponse(
                call: Call<List<Servicio>>,
                response: Response<List<Servicio>>
            ) {

                if (response.isSuccessful) {

                    val servicios = response.body() ?: listOf()

                    val nombres = servicios.map { it.nombre }

                    val adapter = ArrayAdapter(
                        this@ListaServiciosActivity,
                        android.R.layout.simple_list_item_1,
                        nombres
                    )

                    listaServicios.adapter = adapter

                    listaServicios.setOnItemClickListener { _, _, position, _ ->

                        val servicioSeleccionado = servicios[position]

                        val intent = Intent(
                            this@ListaServiciosActivity,
                            DetalleServicioActivity::class.java
                        )

                        intent.putExtra("servicio_id", servicioSeleccionado.id)

                        startActivity(intent)
                    }

                }

            }

            override fun onFailure(call: Call<List<Servicio>>, t: Throwable) {

                Toast.makeText(
                    this@ListaServiciosActivity,
                    "Error de conexión",
                    Toast.LENGTH_LONG
                ).show()

            }

        })

    }
}