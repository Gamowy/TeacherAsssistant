package com.example.teacherassistant.ui.classes

import android.content.Intent
import android.content.Intent.ACTION_INSERT
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teacherassistant.ClassActivity
import com.example.teacherassistant.databinding.FragmentClassListBinding
import com.example.teacherassistant.models.TeacherClass
import com.example.teacherassistant.models.room.AppDatabaseInstance

class ClassListFragment : Fragment(), ClassCardClickListener {
    private var _binding: FragmentClassListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassListBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Classes"

        // Get the list of classes from the database
        val db = AppDatabaseInstance.get(requireContext())
        val teacherClassDao = db.teacherClassDao
        val classList = teacherClassDao.getTeacherClassesOrdered()

        // Observe the list of classes and update the UI when it changes
        classList.observe(viewLifecycleOwner) {
            binding.classesContainer.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ClassCardAdapter(it, this@ClassListFragment)
            }
            binding.classesEmptyLabel.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.addLessonButton.setOnClickListener {
            val intent = Intent(context, ClassActivity::class.java)
            intent.action = ACTION_INSERT
            startActivity(intent)
        }

        val root: View = binding.root
        return root
    }

    // Open the class details activity when a class card is clicked
    override fun onCardClick(teacherClass: TeacherClass) {
        val intent = Intent(context, ClassActivity::class.java)
        intent.action = ACTION_VIEW
        intent.putExtra("classId", teacherClass.classId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}