package com.example.teacherassistant.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
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

    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.themePicker.threshold = 100

        preferences = requireActivity().getSharedPreferences("settings", 0)
        val theme = AppCompatDelegate.getDefaultNightMode()
        when (theme) {
            AppCompatDelegate.MODE_NIGHT_YES -> binding.themePicker.setText(resources.getStringArray(R.array.themeList)[2])
            AppCompatDelegate.MODE_NIGHT_NO -> binding.themePicker.setText(resources.getStringArray(R.array.themeList)[1])
            else -> binding.themePicker.setText(resources.getStringArray(R.array.themeList)[0])
        }

        binding.themePicker.onItemClickListener = AdapterView.OnItemClickListener{ _, _, _, _ ->
            val value = binding.themePicker.text.toString()
            when (value) {
                resources.getStringArray(R.array.themeList)[2] -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                resources.getStringArray(R.array.themeList)[1] -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                resources.getStringArray(R.array.themeList)[0] -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }

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
        preferences.edit().putInt("theme", AppCompatDelegate.getDefaultNightMode()).apply()
        super.onDestroyView()
        _binding = null
    }
}