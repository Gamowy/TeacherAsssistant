package com.example.teacherassistant.ui.classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teacherassistant.models.TeacherClass

class ClassesViewModel : ViewModel() {
    private val _classes = MutableLiveData<MutableList<TeacherClass>>(mutableListOf())
    val classes : LiveData<MutableList<TeacherClass>> = _classes

    fun addClass(teacherClass: TeacherClass) {
        _classes.value?.add(teacherClass)
    }
}