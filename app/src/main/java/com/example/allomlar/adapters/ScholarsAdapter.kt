package com.mac.allomalar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.mac.allomalar.R
import com.mac.allomalar.db.database.AppDatabase
import com.mac.allomalar.models.Alloma
import com.mac.allomalar.models.Madrasa
import com.mac.allomalar.models.MadrasaAndAllomas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScholarsAdapter(
    val context: Context,
    var list: List<MadrasaAndAllomas>,
    var onItemScholarClick: OnItemScholarClick
) : RecyclerView.Adapter<ScholarsAdapter.ViewHolder>() {

    private var db: AppDatabase = AppDatabase.getInstance(context)

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(madrasaAndAllomas: MadrasaAndAllomas?, position: Int) {
            val card = view.findViewById<MaterialCardView>(R.id.card_batafsil)
            card.setOnClickListener {
                onItemScholarClick.onClick(madrasaAndAllomas, position)
            }

            val tv = view.findViewById<TextView>(R.id.tv_scholar_name)
            tv.text = madrasaAndAllomas?.let {
                it.name
            }

            CoroutineScope(Dispatchers.Main).launch {
                val imageView = view.findViewById<ImageView>(R.id.iv_image_of_scholar)
                val image = db.imageDao().getImageById(madrasaAndAllomas?.image_url!!)?.image
                if (image == null) {
                    imageView.setImageResource(R.drawable.old_me)
                } else {
                    imageView.setImageBitmap(image)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scholar_in_madrasa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemScholarClick {
        fun onClick(madrasaAndAllomas: MadrasaAndAllomas?, position: Int)
    }
}