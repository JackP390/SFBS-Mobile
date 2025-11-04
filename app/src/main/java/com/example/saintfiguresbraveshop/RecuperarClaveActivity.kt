package com.example.saintfiguresbraveshop

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecuperarClaveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_clave)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recoverButton = findViewById<Button>(R.id.buttonRecuperar)
        val backToLoginLink = findViewById<TextView>(R.id.linkVolverLogin)

        recoverButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Solicitud Enviada")
                .setMessage("Se ha enviado un correo electrónico con el enlace de recuperación de clave a tu dirección.")
                .setPositiveButton("OK") { d, _ -> d.dismiss() }
                .show()
        }

        backToLoginLink.setOnClickListener {
            finish()
        }
    }
}