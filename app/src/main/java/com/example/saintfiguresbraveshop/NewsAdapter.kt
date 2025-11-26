package com.example.saintfiguresbraveshop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Modelo de datos
data class News(
    val id: String = "",
    val title: String = "",
    val summary: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val author: String = ""
)

// Adaptador
class NewsAdapter(
    private val newsList: List<News>,
    private val onItemClick: (News) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvNewsTitle)
        val summary: TextView = view.findViewById(R.id.tvNewsSummary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.title.text = news.title
        holder.summary.text = news.summary
        holder.itemView.setOnClickListener { onItemClick(news) }
    }

    override fun getItemCount() = newsList.size
}