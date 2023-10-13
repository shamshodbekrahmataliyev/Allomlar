package com.mac.allomalar.adapters

import android.content.Context
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allomlar.R
import com.google.android.material.card.MaterialCardView
import com.mac.allomalar.db.database.AppDatabase
import com.mac.allomalar.models.Subject
import com.mac.allomalar.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FieldsAdapter(var context: Context, var list: List<Subject?>, var onFieldClick: OnFieldClick) :
    RecyclerView.Adapter<FieldsAdapter.ViewHolder>() {

    private var db = AppDatabase.getInstance(context)

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(subject: Subject?, position: Int) {
            view.setOnClickListener {
                onFieldClick.onClick(subject, position)
            }
            val tv = view.findViewById<TextView>(R.id.tv_field_name_2)
            val imageView = view.findViewById<ImageView>(R.id.iv_field_logo)
            tv.text = subject?.name

            CoroutineScope(Dispatchers.Main).launch {
                var image = db.imageDao().getImageById(subject?.image_url!!)?.image
                if (image != null)
                    imageView.setImageBitmap(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fields, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list?.get(position), position)
    }

    override fun getItemCount(): Int = list.size

    interface OnFieldClick {
        fun onClick(subject: Subject?, position: Int)
    }
}