package com.example.teacherassistant.ui.classes


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.teacherassistant.databinding.FragmentAddClassBinding
import com.example.teacherassistant.models.TeacherClass
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch

class AddClassFragment : Fragment() {
    private var _binding: FragmentAddClassBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddClassBinding.inflate(inflater, container, false)
        setupStartTimePicker()
        setupEndTimePicker()
        setupEditTextListeners()


        val db = AppDatabaseInstance.get(requireContext())
        val teacherClassDao = db.teacherClassDao
        binding.addClassButton.setOnClickListener {
            lifecycleScope.launch {
                val className = binding.editClassName.text.toString()
                val description = binding.editDescription.text.toString()
                val weekDay = binding.weekDayPicker.text.toString()
                val startTime = binding.startTime.text.toString()
                val endTime = binding.endTime.text.toString()

                val backgroundColorId: Int =
                    getBackgroundColorPickerPosition(binding.backgroundColorPicker.text.toString())
                        ?: (0..5).random()

                val teacherClass = TeacherClass(
                    name = className,
                    weekDay = weekDay,
                    startTime = startTime,
                    endTime = endTime
                )
                teacherClass.description = description
                teacherClass.backgroundId = (backgroundColorId + 1)
                teacherClassDao.insertTeacherClass(teacherClass)
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
        outState.putString("class_name", binding.editClassName.text.toString())
        outState.putString("class_description", binding.editDescription.text.toString())
        outState.putString("weekday", binding.weekDayPicker.text.toString())
        outState.putString("start_time", binding.startTime.text.toString())
        outState.putString("end_time", binding.endTime.text.toString())
        outState.putString("background_color", binding.backgroundColorPicker.text.toString())
    }

    override fun onViewStateRestored(savedState: Bundle?) {
        super.onViewStateRestored(savedState)

        savedState?.let {
            val weekDayPosition = getWeekDayPickerPosition(it.getString("weekday"))
            val backgroundColorPosition =
                getBackgroundColorPickerPosition(it.getString("background_color"))
            binding.editClassName.setText(it.getString("class_name"))
            binding.editDescription.setText(it.getString("class_description"))
            binding.weekDayPicker.listSelection = weekDayPosition ?: 0
            binding.startTime.text = it.getString("start_time").toString()
            binding.endTime.text = it.getString("end_time")
            binding.backgroundColorPicker.listSelection = backgroundColorPosition ?: 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getWeekDayPickerPosition(weekDay: String?): Int? {
        return when (weekDay) {
            "Monday" -> 0
            "Tuesday" -> 1
            "Wednesday" -> 2
            "Thursday" -> 3
            "Friday" -> 4
            "Saturday" -> 5
            "Sunday" -> 6
            else -> null
        }
    }

    private fun getBackgroundColorPickerPosition(color: String?): Int? {
        return when (color) {
            "Pink" -> 0
            "Red" -> 1
            "Green" -> 2
            "Orange" -> 3
            "Purple" -> 4
            "Blue" -> 5
            else -> null
        }
    }

    private fun setupStartTimePicker() {
        binding.startTime.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(binding.startTime.text.toString().split(":")[0].toInt())
                .setMinute(binding.startTime.text.toString().split(":")[1].toInt())
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setTitleText("Select class start time")
                .build()

            picker.show(parentFragmentManager, "start_time_picker")
            picker.addOnPositiveButtonClickListener {
                val hour = if (picker.hour < 10) "0${picker.hour}" else picker.hour
                val minute = if (picker.minute < 10) "0${picker.minute}" else picker.minute
                val time = "$hour:$minute"
                binding.startTime.text = time
            }
        }
    }

    private fun setupEndTimePicker() {
        binding.endTime.setOnClickListener {
            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(binding.endTime.text.toString().split(":")[0].toInt())
                .setMinute(binding.endTime.text.toString().split(":")[1].toInt())
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setTitleText("Select class end time")
                .build()

            picker.show(parentFragmentManager, "end_time_picker")
            picker.addOnPositiveButtonClickListener {
                val hour = if (picker.hour < 10) "0${picker.hour}" else picker.hour
                val minute = if (picker.minute < 10) "0${picker.minute}" else picker.minute
                val time = "$hour:$minute"
                binding.endTime.text = time
            }
        }
    }

    private fun setupEditTextListeners() {
        binding.editClassName.addTextChangedListener {
            checkAddConditions()
        }
        binding.editDescription.addTextChangedListener {
            checkAddConditions()
        }
        binding.weekDayPicker.addTextChangedListener {
            checkAddConditions()
        }
    }

    private fun checkAddConditions() {
        val className = binding.editClassName.text
        val classDesc = binding.editDescription.text
        val day = binding.weekDayPicker.text
        val button = binding.addClassButton

        button.setEnabled(className != null && classDesc != null && day != null && className.isNotEmpty() && classDesc.isNotEmpty() && day.isNotEmpty())
    }
}