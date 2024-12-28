package com.example.teacherassistant.ui.classes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teacherassistant.ClassActivity
import com.example.teacherassistant.databinding.FragmentClassesBinding
import com.example.teacherassistant.models.TeacherClass

class ClassesFragment : Fragment(), ClassCardClickListener {

    val classesViewModel: ClassesViewModel by activityViewModels()
    private var _binding: FragmentClassesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)

        addExampleData()
        binding.classesContainer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CardAdapter(classesViewModel.classes.value!!, this@ClassesFragment)
        }
        val root: View = binding.root
        return root
    }

    fun addExampleData() {
        // Add example data
        if (classesViewModel.classes.value?.size == 0) {
            classesViewModel.addClass(TeacherClass(0, "Math", "Monday", "8:00", "10:00").apply {
                description = "Math class for beginners"
                backgroundId = 1
            })
            classesViewModel.addClass(TeacherClass(1, "English", "Tuesday", "8:00", "10:00").apply {
                description = "English class for beginners"
                backgroundId = 2
            })
            classesViewModel.addClass(TeacherClass(2, "History", "Wednesday", "8:00", "10:00").apply {
                    description = "History class for beginners"
                    backgroundId = 3
            })
            classesViewModel.addClass(TeacherClass(3, "Biology", "Thursday", "8:00", "10:00").apply {
                    description = "Biology class for beginners"
                    backgroundId = 4
            })
        }
    }

    override fun onClick(teacherClass: TeacherClass) {
        val intent = Intent(context, ClassActivity::class.java)
        intent.putExtra("classId", teacherClass.id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}