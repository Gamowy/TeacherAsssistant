package com.example.teacherassistant.ui.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teacherassistant.databinding.TeacherClassCardBinding
import com.example.teacherassistant.models.TeacherClass

class CardAdapter(private val classes: List<TeacherClass>, private val clickListener: ClassCardClickListener) : RecyclerView.Adapter<CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TeacherClassCardBinding.inflate(from, parent, false)
        return CardViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = classes.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindTeacherClass(classes[position])
    }
}