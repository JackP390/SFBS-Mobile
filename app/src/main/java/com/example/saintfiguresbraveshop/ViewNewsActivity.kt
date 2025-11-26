package com.example.saintfiguresbraveshop

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide // Importamos Glide

class ViewNewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_news)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val author = intent.getStringExtra("author")
        val imageUrl = intent.getStringExtra("imageUrl") // Recibimos el link

        findViewById<TextView>(R.id.tvDetailTitle).text = title
        findViewById<TextView>(R.id.tvDetailContent).text = content
        findViewById<TextView>(R.id.tvDetailAuthor).text = "Por: $author"

        val imageView = findViewById<ImageView>(R.id.ivDetailImage)

        // Si hay un link de imagen, la cargamos con Glide
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery) // Mientras carga
                .error(android.R.drawable.ic_delete) // Si falla el link
                .into(imageView)
        } else {
            // Si no hay imagen, ocultamos el cuadro o ponemos una default
            Glide.with(this).load(R.drawable.logo).into(imageView)
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }
}