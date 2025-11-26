package com.example.saintfiguresbraveshop

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegistrarCuentaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_cuenta)

        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailEt = findViewById<EditText>(R.id.registerEmail)
        val passEt = findViewById<EditText>(R.id.registerPassword)
        val passConfirmEt = findViewById<EditText>(R.id.registerConfirmPassword)
        val btnRegistrar = findViewById<Button>(R.id.buttonRegistrarCuenta)
        val backToLoginLink = findViewById<TextView>(R.id.linkVolverLogin)

        btnRegistrar.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val pass = passEt.text.toString().trim()
            val confirm = passConfirmEt.text.toString().trim()

            // 1. Validar campos vacíos
            if (email.isEmpty() || pass.isEmpty()) {
                mostrarMensaje("Faltan datos por llenar")
                return@setOnClickListener
            }

            // 2. Validar coincidencia
            if (pass != confirm) {
                mostrarMensaje("Las contraseñas no coinciden")
                return@setOnClickListener
            }

            // --- TUS REQUISITOS DE SEGURIDAD ---

            // Validar largo (mínimo 6)
            if (pass.length < 6) {
                mostrarMensaje("La contraseña es muy corta (mínimo 6)")
                return@setOnClickListener
            }

            // Validar Mayúscula
            if (!pass.any { it.isUpperCase() }) {
                mostrarMensaje("La contraseña debe tener una MAYÚSCULA")
                return@setOnClickListener
            }

            // Validar Número
            if (!pass.any { it.isDigit() }) {
                mostrarMensaje("La contraseña debe tener un NÚMERO")
                return@setOnClickListener
            }

            // Validar Símbolo (Si todo es letra o número, falla)
            if (pass.all { it.isLetterOrDigit() }) {
                mostrarMensaje("La contraseña debe tener un SÍMBOLO (@, #, $, etc)")
                return@setOnClickListener
            }

            // --- FIN REQUISITOS ---

            // Crear usuario en Firebase
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        AlertDialog.Builder(this)
                            .setTitle("¡Cuenta Creada!")
                            .setMessage("Tu registro fue exitoso. Ahora puedes iniciar sesión.")
                            .setPositiveButton("Ir al Login") { _, _ ->
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                            .setCancelable(false)
                            .show()
                    } else {
                        val error = task.exception
                        val mensaje = when (error) {
                            is FirebaseAuthUserCollisionException -> "Ese correo YA EXISTE en el sistema."
                            is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil (usa números y letras)."
                            is FirebaseAuthInvalidCredentialsException -> "El correo está mal escrito."
                            else -> "Error de conexión: ${error?.message}"
                        }
                        mostrarMensaje(mensaje)
                    }
                }
        }

        backToLoginLink.setOnClickListener {
            finish()
        }
    }

    private fun mostrarMensaje(texto: String) {
        Toast.makeText(applicationContext, texto, Toast.LENGTH_SHORT).show()
    }
}