package com.example.saintfiguresbraveshop

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class RecuperarClaveActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_clave)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Ajuste de diseño (márgenes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailInput = findViewById<EditText>(R.id.recoverEmail)
        val recoverButton = findViewById<Button>(R.id.buttonRecuperar)
        val backToLoginLink = findViewById<TextView>(R.id.linkVolverLogin)

        // Lógica del Botón RECUPERAR
        recoverButton.setOnClickListener {
            val email = emailInput.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu correo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Enviar correo de reset de Firebase
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AlertDialog.Builder(this)
                            .setTitle("Correo Enviado")
                            .setMessage("Hemos enviado un enlace a tu correo ($email) para restablecer tu contraseña.")
                            .setPositiveButton("Entendido") { d, _ ->
                                d.dismiss()
                                finish() // Volver al login
                            }
                            .show()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Botón Volver
        backToLoginLink.setOnClickListener {
            finish()
        }
    }
}