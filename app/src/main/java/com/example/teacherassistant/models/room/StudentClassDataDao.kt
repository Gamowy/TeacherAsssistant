package com.example.teacherassistant.models.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.TeacherClass

@Dao
interface StudentClassDataDao {
    @Upsert
    suspend fun insertStudentClassData(studentAndClass: StudentAndClass)

    @Query("SELECT * FROM Student WHERE studentId IN (SELECT student_id FROM StudentAndClass WHERE class_id = :classId)")
    fun getStudentsInClass(classId: Int): LiveData<List<Student>>

    @Query("SELECT * FROM Student WHERE studentId NOT IN (SELECT student_id FROM StudentAndClass WHERE class_id = :classId)")
    fun getStudentsNotInClass(classId: Int): LiveData<List<Student>>

    @Query("SELECT * FROM TeacherClass WHERE classId IN (SELECT class_id FROM StudentAndClass WHERE student_id = :studentId)")
    fun getClassesForStudent(studentId: Int): LiveData<List<TeacherClass>>
}