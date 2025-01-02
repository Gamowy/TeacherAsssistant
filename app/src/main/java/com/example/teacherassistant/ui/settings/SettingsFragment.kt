package com.example.teacherassistant.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.teacherassistant.R
import com.example.teacherassistant.databinding.FragmentSettingsBinding
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var deleteDialog: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        deleteDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.warning))
            .setMessage(resources.getString(R.string.dialog_clear_app_data_message))
            .setNegativeButton(resources.getString(R.string.no)) { _, _ ->
            }
            .setPositiveButton(resources.getString(R.string.yes)){ _, _ ->
                val db = AppDatabaseInstance.get(requireContext())
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        db.clearAllTables()
                    }
                }
            }

        binding.clearData.setOnClickListener {
            deleteDialog.show()
        }

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}