package com.example.teacherassistant.ui.students

import androidx.recyclerview.widget.RecyclerView
import com.example.teacherassistant.databinding.StudentViewBinding
import com.example.teacherassistant.models.Student

class StudentViewHolder(private val studentViewBinding: StudentViewBinding,
    private val clickListener: StudentViewClickListener): RecyclerView.ViewHolder(studentViewBinding.root)
{
        fun bindStudent(student: Student) {
            studentViewBinding.firstName.text = student.name
            studentViewBinding.lastName.text = student.lastName

            studentViewBinding.root.setOnClickListener {
                clickListener.onStudentClick(student)
            }
        }
}