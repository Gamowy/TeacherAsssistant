package com.example.teacherassistant.ui.classes

import com.example.teacherassistant.models.TeacherClass

interface ClassCardClickListener {
    fun onClick(teacherClass: TeacherClass)
}