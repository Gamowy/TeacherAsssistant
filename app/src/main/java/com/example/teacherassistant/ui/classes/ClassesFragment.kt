package com.example.teacherassistant.ui.classes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teacherassistant.databinding.FragmentClassesBinding
import com.example.teacherassistant.models.TeacherClass
import com.example.teacherassistant.ui.settings.SettingsFragment

class ClassesFragment : Fragment() {

    private var _binding: FragmentClassesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[ClassesViewModel::class.java]

        // crate example data
        val exampleData: MutableList<TeacherClass> = mutableListOf()
        exampleData.add(TeacherClass(0, "Math", "Monday", "8:00", "10:00").apply {
            description = "Math class for beginners"
            backgroundId = 1
        })
        exampleData.add(TeacherClass(1, "English", "Tuesday", "8:00", "10:00").apply {
            description = "English class for beginners"
            backgroundId = 2
        })
        exampleData.add(TeacherClass(2, "History", "Wednesday", "8:00", "10:00").apply {
            description = "History class for beginners"
            backgroundId = 3
        })
        exampleData.add(TeacherClass(3, "Biology", "Thursday", "8:00", "10:00").apply {
            description = "Biology class for beginners"
            backgroundId = 4
        })

        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        binding.classesContainer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CardAdapter(exampleData)
        }
        val root: View = binding.root
        return root
    }

    fun onClassClick(teacherClass: TeacherClass) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}