package com.example.saintfiguresbraveshop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {

    private var keepSplashOn = true
    private val DELAY_MILLIS = 2000L
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashOn }
        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOn = false }, DELAY_MILLIS)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this, NewsActivity::class.java))
            finish()
            return
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailInput = findViewById<EditText>(R.id.editTextText)
        val passInput = findViewById<EditText>(R.id.editTextTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonBienvenida)
        val linkRecover = findViewById<TextView>(R.id.linkRecuperarClave)
        val linkRegister = findViewById<TextView>(R.id.linkRegistrarCuenta)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val pass = passInput.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "¡Bienvenido de vuelta!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, NewsActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // TRADUCCIÓN DE ERRORES DE LOGIN
                            val mensaje = when (task.exception) {
                                is FirebaseAuthInvalidUserException -> "Esta cuenta no existe. ¡Regístrate!"
                                is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
                                else -> "Error de conexión o datos incorrectos."
                            }
                            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        linkRecover.setOnClickListener {
            val intent = Intent(this, RecuperarClaveActivity::class.java)
            startActivity(intent)
        }

        linkRegister.setOnClickListener {
            val intent = Intent(this, RegistrarCuentaActivity::class.java)
            startActivity(intent)
        }
    }
}