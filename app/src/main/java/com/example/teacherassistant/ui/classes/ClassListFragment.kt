package com.example.teacherassistant.ui.classes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // Get the list of classes from the database
        val db = AppDatabaseInstance.get(requireContext())
        val teacherClassDao = db.teacherClassDao
        val classList = teacherClassDao.getTeacherClassesOrdered()

        // Observe the list of classes and update the UI when it changes
        classList.observe(viewLifecycleOwner) {
            binding.classesContainer.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CardAdapter(it, this@ClassListFragment)
            }
            binding.emptyLabel.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        val root: View = binding.root
        return root
    }

    // Open the class details activity when a class card is clicked
    override fun onClick(teacherClass: TeacherClass) {
        val intent = Intent(context, ClassActivity::class.java)
        intent.putExtra("classId", teacherClass.classId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}