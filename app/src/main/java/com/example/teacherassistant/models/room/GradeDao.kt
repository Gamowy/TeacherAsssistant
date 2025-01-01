package com.example.teacherassistant.models.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.teacherassistant.models.Grade

@Dao
interface GradeDao {
    @Upsert
    suspend fun insertGrade(grade: Grade)

    @Delete
    suspend fun deleteGrade(grade: Grade)

    @Query("SELECT * FROM Grade WHERE student_id = :studentId AND class_id = :classId ORDER BY date(date)")
    fun getGradesForStudentIdAndClassId(studentId: Int, classId: Int): LiveData<List<Grade>>
}
