package com.example.teacherassistant.ui.classes.classdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.teacherassistant.models.TeacherClass

class ClassDetailsViewModel : ViewModel() {
    var selectedClass = MutableLiveData<TeacherClass>()
}