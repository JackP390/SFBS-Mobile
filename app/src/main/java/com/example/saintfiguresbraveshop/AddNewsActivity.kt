package com.example.saintfiguresbraveshop

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class AddNewsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_news)

        db = FirebaseFirestore.getInstance()

        val etTitle = findViewById<EditText>(R.id.etNewsTitle)
        val etSummary = findViewById<EditText>(R.id.etNewsSummary)
        val etContent = findViewById<EditText>(R.id.etNewsContent)
        val etImage = findViewById<EditText>(R.id.etNewsImage) // Referencia al nuevo campo
        val btnSave = findViewById<Button>(R.id.btnSaveNews)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val summary = etSummary.text.toString()
            val content = etContent.text.toString()
            val imageUrl = etImage.text.toString() // Obtenemos el link
            val author = FirebaseAuth.getInstance().currentUser?.email ?: "Anónimo"

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Completa título y contenido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear mapa de datos incluyendo la imagen
            val newsData = hashMapOf(
                "title" to title,
                "summary" to summary,
                "content" to content,
                "imageUrl" to imageUrl, // Guardamos el link
                "author" to author,
                "date" to Date().time
            )

            db.collection("noticias")
                .add(newsData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Noticia creada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
        }

        btnBack.setOnClickListener { finish() }
    }
}