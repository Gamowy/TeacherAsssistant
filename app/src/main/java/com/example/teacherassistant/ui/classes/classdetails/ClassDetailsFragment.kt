package com.example.teacherassistant.ui.classes.classdetails

import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat.getExtras
import androidx.fragment.app.activityViewModels
import com.example.teacherassistant.databinding.FragmentClassDetailsBinding
import com.example.teacherassistant.models.TeacherClass
import com.example.teacherassistant.models.room.AppDatabaseInstance

class ClassDetailsFragment : Fragment() {
    private val classDetailsViewModel: ClassDetailsViewModel by activityViewModels()
    private var _binding: FragmentClassDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)
        val db = AppDatabaseInstance.get(requireContext())
        val teacherClassDao = db.teacherClassDao

        val extras = activity?.intent?.extras
        if (extras != null) {
            val classId = extras.getInt("classId")
            val teacherClass = teacherClassDao.getTeacherClassById(classId)
            classDetailsViewModel.selectedClass.value = teacherClass.value
            teacherClass.observe(viewLifecycleOwner) {
                val whenText = "${it.weekDay} ${it.startTime} - ${it.endTime}"
                binding.className.text = it.name
                binding.description.text = it.description
                binding.time.text = whenText
            }
        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}