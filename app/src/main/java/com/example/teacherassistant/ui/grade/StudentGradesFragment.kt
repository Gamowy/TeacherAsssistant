package com.example.teacherassistant.ui.grade

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teacherassistant.R
import com.example.teacherassistant.databinding.FragmentStudentGradesBinding
import com.example.teacherassistant.models.Grade
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.example.teacherassistant.models.room.GradeDao
import com.example.teacherassistant.models.room.StudentClassDataDao
import com.example.teacherassistant.models.room.StudentDao
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StudentGradesFragment : Fragment(), GradeViewClickListener {
    private var _binding: FragmentStudentGradesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var studentDao: StudentDao
    private lateinit var gradesDao: GradeDao
    private lateinit var studentClassDataDao: StudentClassDataDao


    private lateinit var gradeDetailsDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentGradesBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Student Grades"
        val gridColumns = resources.getInteger(R.integer.grid_columns)

        val db = AppDatabaseInstance.get(requireContext())
        studentDao = db.studentDao
        gradesDao = db.gradeDao
        studentClassDataDao = db.studentClassDataDao

        if (arguments != null) {
            val classId = arguments?.getInt("classId") ?: 0
            val studentId = arguments?.getInt("studentId") ?: 0
            val studentLiveData = studentDao.getStudentById(studentId)
            val gradesLiveData = gradesDao.getGradesForStudentIdAndClassId(studentId, classId)


            studentLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.firstName.text = it.name
                    binding.lastName.text = it.lastName
                    binding.studentNumber.text = it.studentNumber
                }
            }

            gradesLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.gradesContainer.apply {
                        layoutManager = GridLayoutManager(context, gridColumns)
                        adapter = GradeViewAdapter(it, this@StudentGradesFragment)
                    }
                }
                binding.studentGradesEmptyLabel.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }

            // Setup dialogs
            gradeDetailsDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.dialog_grade_title))
                .setIcon(R.drawable.ic_grade_black_24dp)
                .setPositiveButton(resources.getString(R.string.ok)){ _, _ -> }


            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            binding.addGradeButton.setOnClickListener {
                val dialogView = layoutInflater.inflate(R.layout.add_grade_dialog_view, null)

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.add_grade_label))
                    .setView(dialogView)
                    .setPositiveButton(resources.getString(R.string.add_button)){ _, _ ->
                        val gradeInput = dialogView.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.grade_input).text.toString()
                        val descriptionInput = dialogView.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.grade_description_input).text.toString()
                        val grade = Grade(0, studentId, classId, gradeInput, descriptionInput, dateFormat.format(LocalDate.now()))
                        lifecycleScope.launch {
                            gradesDao.insertGrade(grade)
                        }
                    }
                    .setNegativeButton(resources.getString(R.string.cancel_label)){ _, _ -> }
                    .show()
            }
            binding.removeFromClassButton.setOnClickListener {
                lifecycleScope.launch {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.dialog_remove_from_class_title))
                        .setMessage(resources.getString(R.string.dialog_remove_from_class_message))
                        .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                        }
                        .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                            lifecycleScope.launch {
                                gradesDao.deleteGradesById(studentId, classId)
                                studentClassDataDao.deleteStudentClassDataById(studentId, classId)
                            }
                            activity?.finish()
                        }
                        .show()
                }
            }
        }
        val root: View = binding.root
        return root
    }

    override fun onGradeClick(grade: Grade) {
        gradeDetailsDialog.setMessage(resources.getString(R.string.dialog_grade_message, grade.grade, grade.description, grade.date))
            .setNeutralButton(resources.getString(R.string.delete)) { _, _ ->
                lifecycleScope.launch {
                    gradesDao.deleteGrade(grade)
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}