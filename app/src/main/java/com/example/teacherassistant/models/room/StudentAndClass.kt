package com.example.teacherassistant.models.room

import androidx.room.Entity

@Entity(primaryKeys = ["studentId", "classId"])
data class StudentAndClass(
    val studentId: Int,
    val classId: Int
)