package com.example.teacherassistant.ui.classes

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
import com.example.teacherassistant.databinding.FragmentClassDetailsBinding
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.TeacherClass
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.example.teacherassistant.models.room.StudentClassDataDao
import com.example.teacherassistant.models.room.TeacherClassDao
import com.example.teacherassistant.ui.grade.StudentGradesFragment
import com.example.teacherassistant.ui.students.StudentViewAdapter
import com.example.teacherassistant.ui.students.StudentViewClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ClassDetailsFragment : Fragment(), StudentViewClickListener {
    private var _binding: FragmentClassDetailsBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var classId: Int = 0
    private lateinit var teacherClassDao: TeacherClassDao
    private lateinit var studentClassDataDao: StudentClassDataDao

    private lateinit var deleteDialog: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Class details"

        val db = AppDatabaseInstance.get(requireContext())
        teacherClassDao = db.teacherClassDao
        studentClassDataDao = db.studentClassDataDao
        var teacherClassLiveData : LiveData<TeacherClass>? = null

        val extras = activity?.intent?.extras
        if (extras != null) {
            classId = extras.getInt("classId")
            teacherClassLiveData = teacherClassDao.getTeacherClassById(classId)
            val studentsLiveData = studentClassDataDao.getStudentsInClass(classId)

            teacherClassLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    val whenText = "${it.weekDay} ${it.startTime} - ${it.endTime}"
                    binding.className.text = it.name
                    binding.description.text = it.description
                    binding.time.text = whenText
                }
            }
            studentsLiveData.observe(viewLifecycleOwner) {
                binding.studentsList.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = StudentViewAdapter(it, this@ClassDetailsFragment)
                }
            }
        }

        // Setup delete dialog
        deleteDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_delete_class_title))
            .setMessage(resources.getString(R.string.dialog_delete_class_message))
            .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
            }
            .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                lifecycleScope.launch {
                    teacherClassLiveData?.value?.let { teacherClassDao.deleteTeacherClass(it) }
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
                            .replace(R.id.fragment_container_details, AddEditClassFragment())
                            .commit()
                        true
                    }
                    R.id.menuDelete-> {
                        if (teacherClassLiveData?.value != null) {
                           deleteDialog.show()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.assignStudentButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_details, PickStudentFragment())
                .commit()
        }

        val root: View = binding.root
        return root
    }

    override fun onStudentClick(student: Student) {
        val bundle = Bundle()
        bundle.putInt("studentId", student.studentId)
        bundle.putInt("classId", classId)

        val fragment = StudentGradesFragment()
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_details, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}