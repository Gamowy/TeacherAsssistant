package com.example.teacherassistant.ui.grade

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teacherassistant.databinding.GradeViewBinding
import com.example.teacherassistant.models.Grade

class GradeViewAdapter (private val grades: List<Grade>, private val clickListener: GradeViewClickListener) : RecyclerView.Adapter<GradeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = GradeViewBinding.inflate(from, parent, false)
        return GradeViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = grades.size

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        holder.bindGrade(grades[position])
    }
}