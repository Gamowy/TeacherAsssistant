package com.example.teacherassistant.ui.students

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
import com.example.teacherassistant.StudentActivity
import com.example.teacherassistant.databinding.FragmentStudentsListBinding
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.example.teacherassistant.models.room.StudentDao

class StudentsListFragment : Fragment(), StudentViewClickListener {

    private var _binding: FragmentStudentsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var studentDao: StudentDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentsListBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Students"

        // Get the list of students from the database
        val db = AppDatabaseInstance.get(requireContext())
        studentDao = db.studentDao
        val studentList = studentDao.getStudentsOrdered()


        // Observe the student list and update the UI
        studentList.observe(viewLifecycleOwner) {
            binding.studentsContainer.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = StudentViewAdapter(it, this@StudentsListFragment)
            }
            binding.studentsEmptyLabel.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.addStudentButton.setOnClickListener {
            val intent = Intent(context, StudentActivity::class.java)
            intent.action = ACTION_INSERT
            startActivity(intent)
        }

        val root: View = binding.root
        return root
    }

    override fun onStudentClick(student: Student) {
        val intent = Intent(context, StudentActivity::class.java)
        intent.action = ACTION_VIEW
        intent.putExtra("studentId", student.studentId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}