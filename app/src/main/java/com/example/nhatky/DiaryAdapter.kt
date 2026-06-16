package com.example.nhatky

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryAdapter(private val diaries: List<DiaryEntry>) :
    RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>() {

    class DiaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMood: TextView = view.findViewById(R.id.txtDiaryMood)
        val txtDate: TextView = view.findViewById(R.id.txtDiaryDate)
        val txtTitle: TextView = view.findViewById(R.id.txtDiaryTitle)
        val txtContent: TextView = view.findViewById(R.id.txtDiaryContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diary, parent, false)
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        val diary = diaries[position]
        holder.txtMood.text = diary.mood
        holder.txtTitle.text = diary.title
        holder.txtContent.text = diary.content
        
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.txtDate.text = sdf.format(Date(diary.timestamp))
    }

    override fun getItemCount() = diaries.size
}
