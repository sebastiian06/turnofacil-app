package com.turnofacil.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DetalleServicioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_servicio)

        val botonSolicitar = findViewById<Button>(R.id.btnSolicitar)

        botonSolicitar.setOnClickListener {
            val intent = Intent(this, SolicitarTurnoActivity::class.java)
            startActivity(intent)
        }
    }
}