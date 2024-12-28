package com.example.teacherassistant.models

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StudentClassDataDao {
    @Transaction
    @Query("SELECT * FROM Student")
    fun getStudentsWithClasses(): List<StudentWithClasses>

    @Transaction
    @Query("SELECT * FROM TeacherClass")
    fun getClassesWithStudents(): List<ClassWithStudents>
}