package com.example.teacherassistant.ui.students

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teacherassistant.R
import com.example.teacherassistant.databinding.FragmentStudentDetailsBinding
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.TeacherClass
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.example.teacherassistant.models.room.StudentClassDataDao
import com.example.teacherassistant.models.room.StudentDao
import com.example.teacherassistant.ui.classes.ClassCardAdapter
import com.example.teacherassistant.ui.classes.ClassCardClickListener
import com.example.teacherassistant.ui.grade.StudentGradesFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class StudentDetailsFragment : Fragment(), ClassCardClickListener {
    private var _binding: FragmentStudentDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var studentId: Int = 0
    private lateinit var studentDao: StudentDao
    private lateinit var studentClassDataDao: StudentClassDataDao

    private lateinit var deleteDialog: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDetailsBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Student Details"

        val db = AppDatabaseInstance.get(requireContext())
        studentDao = db.studentDao
        studentClassDataDao = db.studentClassDataDao
        var studentLiveData : LiveData<Student>? = null


        val extras = activity?.intent?.extras
        if (extras != null) {
            studentId = extras.getInt("studentId")
            studentLiveData = studentDao.getStudentById(studentId)
            val classesLiveData = studentClassDataDao.getClassesForStudent(studentId)

            studentLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.firstName.text = it.name
                    binding.lastName.text = it.lastName
                    binding.studentNumber.text = it.studentNumber
                }
            }
            classesLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.classesContainer.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = ClassCardAdapter(it, this@StudentDetailsFragment)
                    }
                }
                binding.enrolledClassesEmptyLabel.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Setup delete dialog
        deleteDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_delete_student_title))
            .setMessage(resources.getString(R.string.dialog_delete_student_message))
            .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
            }
            .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                lifecycleScope.launch {
                    studentLiveData?.value?.let { studentDao.deleteStudent(it) }
                }
                activity?.finish()
            }


        // Setup actionbar menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.details_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menuEdit -> {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_details, AddEditStudentFragment())
                            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit()
                        true
                    }
                    R.id.menuDelete-> {
                        if (studentLiveData?.value != null) {
                            deleteDialog.show()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val root: View = binding.root
        return root
    }

    override fun onCardClick(teacherClass: TeacherClass) {
        val bundle = Bundle()
        bundle.putInt("studentId", studentId)
        bundle.putInt("classId", teacherClass.classId)

        val fragment = StudentGradesFragment()
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_details, fragment)
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}