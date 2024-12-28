package com.example.teacherassistant.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StudentWithClasses(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "student_id",
        entityColumn = "class_id",
        associateBy = Junction(StudentAndClass::class)
    )
    val classes: List<TeacherClass>
)

data class ClassWithStudents(
    @Embedded val teacherClass: TeacherClass,
    @Relation(
        parentColumn = "class_id",
        entityColumn = "student_id",
        associateBy = Junction(StudentAndClass::class)
    )
    val students: List<Student>
)