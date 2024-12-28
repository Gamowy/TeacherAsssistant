package com.example.teacherassistant.models.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StudentClassDataDao {
    @Transaction
    @Query("SELECT * FROM Student")
    fun getStudentsWithClasses(): LiveData<List<StudentWithClasses>>

    @Transaction
    @Query("SELECT * FROM TeacherClass")
    fun getClassesWithStudents(): LiveData<List<ClassWithStudents>>
}