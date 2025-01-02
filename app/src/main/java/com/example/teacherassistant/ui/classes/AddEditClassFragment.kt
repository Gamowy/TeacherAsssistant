package com.example.teacherassistant.ui.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.teacherassistant.databinding.FragmentAddEditClassBinding
import com.example.teacherassistant.models.TeacherClass
import com.example.teacherassistant.models.room.AppDatabaseInstance
import com.example.teacherassistant.models.room.TeacherClassDao
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch

class AddEditClassFragment : Fragment() {
    private var _binding: FragmentAddEditClassBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var classId: Int = 0
    private lateinit var teacherClassDao: TeacherClassDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditClassBinding.inflate(inflater, container, false)
        binding.weekDayPicker.threshold = 100
        binding.backgroundColorPicker.threshold = 100

        setupStartTimePicker()
        setupEndTimePicker()
        setupEditTextListeners()

        val db = AppDatabaseInstance.get(requireContext())
        teacherClassDao = db.teacherClassDao

        val extras = activity?.intent?.extras
        if (extras != null) {
            classId = extras.getInt("classId")
            val teacherClassLiveData = teacherClassDao.getTeacherClassById(classId)
            teacherClassLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    val backgroundColorName = getBackgroundColorForId(it.backgroundId)
                    binding.editClassName.setText(it.name)
                    binding.editDescription.setText(it.description)
                    binding.weekDayPicker.setText(it.weekDay, false)
                    binding.startTime.text = it.startTime
                    binding.endTime.text = it.endTime
                    binding.backgroundColorPicker.setText(backgroundColorName, false)
                }
            }
            activity?.intent?.removeExtra("classId")
        }
        if (classId != 0) {
            (activity as AppCompatActivity?)?.supportActionBar?.title = "Edit class"
        }
        else {
            (activity as AppCompatActivity?)?.supportActionBar?.title = "Add class"
        }


        binding.addClassButton.setOnClickListener {
            lifecycleScope.launch {
                val className = binding.editClassName.text.toString()
                val description = binding.editDescription.text.toString()
                val weekDay = binding.weekDayPicker.text.toString()
                val startTime = binding.startTime.text.toString()
                val endTime = binding.endTime.text.toString()

                val backgroundColorId: Int = getIdForBackgroundColor(binding.backgroundColorPicker.text.toString()) ?: (0..6).random()

                val teacherClass = TeacherClass(
                    classId = classId,
                    name = className,
                    weekDay = weekDay,
                    startTime = startTime,
                    endTime = endTime
                )
                teacherClass.description = description
                teacherClass.backgroundId = backgroundColorId
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
        outState.putInt("class_id", classId)

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
            classId = it.getInt("class_id")

            binding.editClassName.setText(it.getString("class_name"))
            binding.editDescription.setText(it.getString("class_description"))
            binding.weekDayPicker.setText(it.getString("weekday"), false)
            binding.startTime.text = it.getString("start_time").toString()
            binding.endTime.text = it.getString("end_time")
            binding.backgroundColorPicker.setText(it.getString("background_color"), false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getBackgroundColorForId(colorId: Int?): String? {
        return when (colorId) {
             1 -> "Pink"
             2 -> "Red"
             3 -> "Green"
             4 -> "Orange"
             5 -> "Purple"
             6 -> "Blue"
            else -> null
        }
    }

    private fun getIdForBackgroundColor(colorName: String?): Int? {
        return when(colorName) {
            "Pink" -> 1
            "Red" -> 2
            "Green" -> 3
            "Orange" -> 4
            "Purple" -> 5
            "Blue" -> 6
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