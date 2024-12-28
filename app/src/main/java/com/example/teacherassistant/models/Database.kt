package com.example.teacherassistant.models
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Student::class, TeacherClass::class, StudentAndClass::class, Grade::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract val studentDao: StudentDao
    abstract val teacherClassDao: TeacherClassDao
    abstract val studentClassDataDao: StudentClassDataDao
    abstract val gradeDao: GradeDao
}