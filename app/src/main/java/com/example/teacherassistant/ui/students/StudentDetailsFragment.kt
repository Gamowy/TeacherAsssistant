package com.example.teacherassistant.ui.students

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.example.teacherassistant.R
import com.example.teacherassistant.databinding.FragmentStudentDetailsBinding
import com.example.teacherassistant.models.Student
import com.example.teacherassistant.models.TeacherClass
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class StudentDetailsFragment : Fragment() {
    private var _binding: FragmentStudentDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDetailsBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Student Details"

        val db = AppDatabaseInstance.get(requireContext())
        val studentDao = db.studentDao
        var studentLiveData : LiveData<Student>? = null


        val extras = activity?.intent?.extras
        if (extras != null) {
            val studentId = extras.getInt("studentId")
            studentLiveData = studentDao.getStudentById(studentId)
            studentLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.firstName.text = it.name
                    binding.lastName.text = it.lastName
                    binding.studentNumber.text = it.studentNumber
                }
            }
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
                        true
                    }
                    R.id.menuDelete-> {
                        val dataToDelete: Student? = studentLiveData?.value
                        if (dataToDelete != null ) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.dialog_delete_student_title))
                                .setMessage(resources.getString(R.string.dialog_delete_student_message))
                                .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                                }
                                .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                                    lifecycleScope.launch {
                                        studentDao.deleteStudent(dataToDelete)
                                    }
                                    activity?.finish()
                                }
                                .show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}