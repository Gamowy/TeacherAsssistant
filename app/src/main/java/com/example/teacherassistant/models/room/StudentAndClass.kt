package com.example.teacherassistant.models.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.TeacherClass

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
data class StudentAndClass(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "student_id", index = true) val studentId: Int,
    @ColumnInfo(name = "class_id", index = true) val classId: Int
)