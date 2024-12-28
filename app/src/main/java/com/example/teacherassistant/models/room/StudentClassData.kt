package com.example.teacherassistant.models.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.TeacherClass

data class StudentWithClasses(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "studentId",
        entityColumn = "classId",
        associateBy = Junction(StudentAndClass::class)
    )
    val classes: List<TeacherClass>
)

data class ClassWithStudents(
    @Embedded val teacherClass: TeacherClass,
    @Relation(
        parentColumn = "classId",
        entityColumn = "studentId",
        associateBy = Junction(StudentAndClass::class)
    )
    val students: List<Student>
)