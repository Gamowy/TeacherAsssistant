package com.example.teacherassistant.models.room
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.teacherassistant.models.Grade
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.TeacherClass

@Database(entities = [Student::class, TeacherClass::class, StudentAndClass::class, Grade::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val studentDao: StudentDao
    abstract val teacherClassDao: TeacherClassDao
    abstract val studentClassDataDao: StudentClassDataDao
    abstract val gradeDao: GradeDao
}

object AppDatabaseInstance {
    private var dbInstance: AppDatabase? = null
    fun get(context : Context): AppDatabase {
        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }
        return dbInstance!!
    }
}
