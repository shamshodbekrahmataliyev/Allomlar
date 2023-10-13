package com.mac.allomalar.adapters

import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allomlar.R
import com.google.android.material.card.MaterialCardView
import com.mac.allomalar.models.Book

class ScientificWorksAdapter(var list: List<Book?>) :
    RecyclerView.Adapter<ScientificWorksAdapter.ViewHolder>() {

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(book: Book?, position: Int) {
            var tv = view.findViewById<TextView>(R.id.tv_books_of_scholar)
            tv.text = book?.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_literature, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

}