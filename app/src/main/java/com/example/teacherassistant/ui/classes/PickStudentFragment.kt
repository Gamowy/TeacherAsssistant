package com.example.teacherassistant.ui.classes


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teacherassistant.R
import com.example.teacherassistant.databinding.FragmentPickStudentBinding
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.example.teacherassistant.models.room.StudentAndClass
import com.example.teacherassistant.ui.students.StudentViewAdapter
import com.example.teacherassistant.ui.students.StudentViewClickListener
import kotlinx.coroutines.launch

class PickStudentFragment : Fragment(), StudentViewClickListener {

    private var _binding: FragmentPickStudentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickStudentBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Assign student to class"
        val classId = requireActivity().intent.extras?.getInt("classId")

        // Get the list of students from the database
        val db = AppDatabaseInstance.get(requireContext())
        val studentClassDataDao = db.studentClassDataDao
        if (classId != null) {
            val studentList = studentClassDataDao.getStudentsNotInClass(classId)

            // Observe the student list and update the UI
            studentList.observe(viewLifecycleOwner) {
                binding.studentsContainer.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = StudentViewAdapter(it, this@PickStudentFragment)
                }
                binding.studentsEmptyLabel.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }
        val root: View = binding.root
        return root
    }

    override fun onStudentClick(student: Student) {
        val db = AppDatabaseInstance.get(requireContext())
        val studentClassDataDao = db.studentClassDataDao
        val classId = requireActivity().intent.extras?.getInt("classId")
        if (classId != null) {
            val studentAndClass = StudentAndClass(0, student.studentId, classId)
            lifecycleScope.launch {
                studentClassDataDao.insertStudentClassData(studentAndClass)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_details, ClassDetailsFragment())
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}