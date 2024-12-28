package com.example.teacherassistant.models

import androidx.room.Entity

@Entity(primaryKeys = ["student_id", "class_id"])
data class StudentAndClass(
    val student: Student,
    val teacherClass: TeacherClass
)