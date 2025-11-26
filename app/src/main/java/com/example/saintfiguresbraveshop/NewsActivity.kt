package com.example.saintfiguresbraveshop

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NewsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private val newsList = mutableListOf<News>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news)

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerNoticias)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Botón Cerrar Sesión
        findViewById<ImageButton>(R.id.btnLogout).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas salir?")
                .setPositiveButton("Sí") { _, _ ->
                    auth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    // Limpiar pila de actividades para no volver atrás
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Botón Agregar Noticia
        findViewById<FloatingActionButton>(R.id.fabAddNews).setOnClickListener {
            startActivity(Intent(this, AddNewsActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        loadNews() // Recargar noticias al volver
    }

    private fun loadNews() {
        db.collection("noticias")
            .get()
            .addOnSuccessListener { result ->
                newsList.clear()
                for (document in result) {
                    val news = News(
                        id = document.id,
                        title = document.getString("title") ?: "",
                        summary = document.getString("summary") ?: "",
                        content = document.getString("content") ?: "",
                        // AQUÍ RECUPERAMOS EL LINK DE LA IMAGEN
                        imageUrl = document.getString("imageUrl") ?: "",
                        author = document.getString("author") ?: ""
                    )
                    newsList.add(news)
                }
                // Configurar adaptador
                adapter = NewsAdapter(newsList) { selectedNews ->
                    // Al hacer clic, ir al detalle y pasar los datos
                    val intent = Intent(this, ViewNewsActivity::class.java)
                    intent.putExtra("id", selectedNews.id)
                    intent.putExtra("title", selectedNews.title)
                    intent.putExtra("content", selectedNews.content)
                    intent.putExtra("author", selectedNews.author)
                    // AQUÍ ENVIAMOS EL LINK A LA SIGUIENTE PANTALLA
                    intent.putExtra("imageUrl", selectedNews.imageUrl)
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error cargando noticias", Toast.LENGTH_SHORT).show()
            }
    }
}