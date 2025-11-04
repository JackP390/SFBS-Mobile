package com.example.saintfiguresbraveshop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    private var keepSplashOn = true
    private val DELAY_MILLIS = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashOn }
        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOn = false }, DELAY_MILLIS)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val principalButton = findViewById<Button>(R.id.buttonBienvenida)
        val linkRecover = findViewById<TextView>(R.id.linkRecuperarClave)
        val linkRegister = findViewById<TextView>(R.id.linkRegistrarCuenta)

        principalButton.setOnClickListener {
            Toast.makeText(
                this,
                "Hola Buen día, Bienvenido a Saint Figures: Brave Shop",
                Toast.LENGTH_LONG
            ).show()

            AlertDialog.Builder(this)
                .setTitle("Saludo especial")
                .setMessage("¡Hola, Bienvenido a Saint Figures: Brave Shop, la tienda de figuras de calidad que necesitas Mobile :D!")
                .setPositiveButton("Gracias...") { d, _ -> d.dismiss() }
                .show()
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