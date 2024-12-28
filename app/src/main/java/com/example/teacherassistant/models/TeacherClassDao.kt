package com.example.teacherassistant.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TeacherClassDao {
    @Upsert
    suspend fun insertTeacherClass(teacherClass: TeacherClass)

    @Delete
    suspend fun deleteTeacherClass(teacherClass: TeacherClass)

    @Query("SELECT * FROM TeacherClass ORDER BY week_day, start_time, end_time")
    fun getTeacherClassesOrdered(): List<TeacherClass>
}