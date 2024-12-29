package com.example.teacherassistant.models.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.teacherassistant.models.TeacherClass

@Dao
interface TeacherClassDao {
    @Upsert
    suspend fun insertTeacherClass(teacherClass: TeacherClass)

    @Delete
    suspend fun deleteTeacherClass(teacherClass: TeacherClass)

    @Query("SELECT * FROM TeacherClass ORDER BY classId, class_name, start_time, end_time")
    fun getTeacherClassesOrdered(): LiveData<List<TeacherClass>>

    @Query("SELECT * FROM TeacherClass WHERE classId = :classId")
    fun getTeacherClassById(classId: Int): LiveData<TeacherClass>
}