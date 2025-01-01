package com.example.teacherassistant.ui.students

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.teacherassistant.databinding.FragmentAddEditStudentBinding
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.example.teacherassistant.models.room.StudentDao
import kotlinx.coroutines.launch

class AddEditStudentFragment : Fragment() {
    private var _binding: FragmentAddEditStudentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var studentId: Int = 0
    private lateinit var studentDao: StudentDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditStudentBinding.inflate(inflater, container, false)
        setupEditTextListeners()

        val db = AppDatabaseInstance.get(requireContext())
        studentDao = db.studentDao

        val extras = activity?.intent?.extras
        if (extras != null) {
            studentId = extras.getInt("studentId")
            val studentLiveData = studentDao.getStudentById(studentId)
            studentLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.editStudentName.setText(it.name)
                    binding.editStudentLastName.setText(it.lastName)
                    binding.editStudentNumber.setText(it.studentNumber)
                }
            }
            activity?.intent?.removeExtra("studentId")
        }
        if (studentId != 0) {
            (activity as AppCompatActivity?)?.supportActionBar?.title = "Edit student"
        }
        else {
            (activity as AppCompatActivity?)?.supportActionBar?.title = "Add student"
        }


        binding.addStudentButton.setOnClickListener {
            lifecycleScope.launch {
                val studentName = binding.editStudentName.text.toString()
                val studentLastName = binding.editStudentLastName.text.toString()
                val studentNumber = binding.editStudentNumber.text.toString()

                val student = Student(
                    studentId = studentId,
                    name = studentName,
                    lastName = studentLastName,
                    studentNumber = studentNumber
                )
                studentDao.insertStudent(student)
            }
            activity?.finish()
        }

        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }

        val root: View = binding.root
        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("student_id", studentId)

        outState.putString("student_name", binding.editStudentName.text.toString())
        outState.putString("student_last_name", binding.editStudentLastName.text.toString())
        outState.putString("student_number", binding.editStudentNumber.text.toString())
    }

    override fun onViewStateRestored(savedState: Bundle?) {
        super.onViewStateRestored(savedState)
        savedState?.let {
            studentId = it.getInt("student_id")

            binding.editStudentName.setText(it.getString("student_name"))
            binding.editStudentLastName.setText(it.getString("student_last_name"))
            binding.editStudentNumber.setText(it.getString("student_number"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupEditTextListeners() {
        binding.editStudentName.addTextChangedListener {
            checkAddConditions()
        }
        binding.editStudentLastName.addTextChangedListener {
            checkAddConditions()
        }
        binding.editStudentNumber.addTextChangedListener {
            checkAddConditions()
        }
    }

    private fun checkAddConditions() {
        val name = binding.editStudentName.text
        val lastName = binding.editStudentLastName.text
        val number = binding.editStudentNumber.text
        val button = binding.addStudentButton

        button.setEnabled(name != null && lastName != null && number != null && name.isNotEmpty() && lastName.isNotEmpty() && number.isNotEmpty())
    }
}