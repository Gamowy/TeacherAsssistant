package com.example.teacherassistant.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.example.teacherassistant.R
import com.example.teacherassistant.databinding.FragmentClassDetailsBinding
import com.example.teacherassistant.models.TeacherClass
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ClassDetailsFragment : Fragment() {
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
        var teacherClassLiveData : LiveData<TeacherClass>? = null


        val extras = activity?.intent?.extras
        if (extras != null) {
            val classId = extras.getInt("classId")
            teacherClassLiveData = teacherClassDao.getTeacherClassById(classId)
            teacherClassLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    val whenText = "${it.weekDay} ${it.startTime} - ${it.endTime}"
                    binding.className.text = it.name
                    binding.description.text = it.description
                    binding.time.text = whenText
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
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_class, AddEditClassFragment())
                            .commit()
                        true
                    }
                    R.id.menuDelete-> {
                        val dataToDelete: TeacherClass? = teacherClassLiveData?.value
                        if (dataToDelete != null ) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(resources.getString(R.string.dialog_delete_class_title))
                                .setMessage(resources.getString(R.string.dialog_delete_class_message))
                                .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                                }
                                .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                                    lifecycleScope.launch {
                                        teacherClassDao.deleteTeacherClass(dataToDelete)
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