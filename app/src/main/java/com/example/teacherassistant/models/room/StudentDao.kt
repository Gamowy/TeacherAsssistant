package com.example.teacherassistant.models.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.teacherassistant.models.Student

@Dao
interface StudentDao {
    @Upsert
    suspend fun insertStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM Student ORDER BY last_name, first_name, student_number")
    fun getStudentsOrdered(): LiveData<List<Student>>

    @Query("SELECT * FROM Student WHERE studentId = :studentId")
    fun getStudentById(studentId: Int): LiveData<Student>
}