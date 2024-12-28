package com.example.teacherassistant.models

class Student(val id: Int, val name: String, val lastName: String, val studentId: String) {
    override fun toString(): String {
        return "$name $lastName"
    }
}