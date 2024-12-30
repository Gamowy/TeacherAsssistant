package com.example.teacherassistant.ui.students

import com.example.teacherassistant.models.Student

interface StudentViewClickListener {
    fun onStudentClick(student: Student)
}