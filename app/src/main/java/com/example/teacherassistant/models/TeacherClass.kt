package com.example.teacherassistant.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TeacherClass(
    @PrimaryKey(autoGenerate = true) val classId: Int = 0,
    @ColumnInfo(name = "class_name")val name: String,
    @ColumnInfo(name = "week_day")val weekDay: String,
    @ColumnInfo(name = "start_time")val startTime: String,
    @ColumnInfo(name = "end_time")val endTime: String) {
    @ColumnInfo(name = "description")var description: String? = null
    @ColumnInfo(name = "background_id") var backgroundId: Int? = null
}