package com.example.teacherassistant.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = Student::class,
        parentColumns = ["studentId"],
        childColumns = ["student_id"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = TeacherClass::class,
        parentColumns = ["classId"],
        childColumns = ["class_id"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )
])
data class Grade (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "student_id") val studentId: Int,
    @ColumnInfo(name = "class_id") val classId: Int,
    @ColumnInfo(name = "grade_type" )val gradeType: GradeType,
    @ColumnInfo(name = "grade") val grade: Int,
)