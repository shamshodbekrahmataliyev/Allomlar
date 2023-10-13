package com.mac.allomalar.adapters

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allomlar.R
import com.google.android.material.card.MaterialCardView
import com.mac.allomalar.models.Century
import com.mac.allomalar.models.Madrasa

class MadrasasAdapter(
    var list: List<Madrasa?>,
    var listPrevious: List<Madrasa?>?,
    val century: Century,
    private var onClick: MadrasaSetOnClickListener
) : RecyclerView.Adapter<MadrasasAdapter.ViewHolder>() {

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(madrasa: Madrasa, position: Int) {
           var isAvailable = false
            val card = view.findViewById<MaterialCardView>(R.id.card_root_name_madrasa)
            card.setOnClickListener {
                onClick.onMadrasaClickListener(madrasa, position)
            }


            card.findViewById<TextView>(R.id.tv_madrasa_name).text = madrasa.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_madrasa_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position]!!, position)
    }

    override fun getItemCount(): Int = list.size

    interface MadrasaSetOnClickListener {
        fun onMadrasaClickListener(madrasa: Madrasa, position: Int)
    }
}