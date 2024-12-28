package com.example.teacherassistant.models

data class TeacherClass(val id: Int, val name: String, val weekDay: String, val startTime: String, val endTime: String) {
    val students: MutableList<Student> = mutableListOf()
    var backgroundId: Int? = null
    var description: String? = null
    fun addStudent(student: Student) {
        students.add(student)
    }

    fun removeStudent(student: Student) {
        students.remove(student)
    }
}