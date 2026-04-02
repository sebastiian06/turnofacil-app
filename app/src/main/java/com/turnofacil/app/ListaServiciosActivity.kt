package com.turnofacil.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ListaServiciosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_servicios)

        val botonServicio = findViewById<Button>(R.id.btnServicio1)

        botonServicio.setOnClickListener {
            val intent = Intent(this, DetalleServicioActivity::class.java)
            startActivity(intent)
        }
    }
}