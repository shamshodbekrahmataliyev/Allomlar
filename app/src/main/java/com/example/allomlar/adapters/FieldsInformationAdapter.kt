package com.mac.allomalar.adapters

import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allomlar.R
import com.google.android.material.card.MaterialCardView
import com.mac.allomalar.models.SubjectInfo

class FieldsInformationAdapter(var list: List<SubjectInfo>) : RecyclerView.Adapter<FieldsInformationAdapter.ViewHolder>() {

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(subjectInfo: SubjectInfo?, position: Int ){
            var tv = view.findViewById<TextView>(R.id.tv_information_last)
            tv.text = subjectInfo?.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_field_information, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

}