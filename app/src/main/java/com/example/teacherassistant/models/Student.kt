package com.example.teacherassistant.models
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey val studentId: Int,
    @ColumnInfo(name = "first_name") val name: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "student_number") val studentNumber: String) {
}