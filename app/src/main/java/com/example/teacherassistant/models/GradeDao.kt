package com.example.teacherassistant.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface GradeDao {
    @Upsert
    suspend fun insertGrade(grade: Grade)

    @Delete
    suspend fun deleteGrade(grade: Grade)

    @Query("SELECT * FROM Grade WHERE student_id = :studentId AND class_id = :classId")
    fun getGradesForStudentIdAndClassId(studentId: Int, classId: Int): List<Grade>
}
