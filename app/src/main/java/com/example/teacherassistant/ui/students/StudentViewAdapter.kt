package com.example.teacherassistant.ui.students

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teacherassistant.databinding.StudentViewBinding
import com.example.teacherassistant.models.Student

class StudentViewAdapter(private val students: List<Student>, private val clickListener: StudentViewClickListener) : RecyclerView.Adapter<StudentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = StudentViewBinding.inflate(from, parent, false)
        return StudentViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bindStudent(students[position])
    }
}