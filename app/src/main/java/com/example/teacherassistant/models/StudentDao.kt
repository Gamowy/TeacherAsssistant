package com.example.teacherassistant.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface StudentDao {
    @Upsert
    suspend fun insertStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM Student ORDER BY last_name, first_name, student_id_number")
    fun getStudentsOrdered(): LiveData<List<Student>>
}