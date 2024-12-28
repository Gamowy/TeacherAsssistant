package com.example.teacherassistant.ui.classes

import androidx.recyclerview.widget.RecyclerView
import com.example.teacherassistant.R
import com.example.teacherassistant.databinding.TeacherClassCardBinding
import com.example.teacherassistant.models.TeacherClass

class CardViewHolder(
    private val teacherClassCardBinding: TeacherClassCardBinding,
    private val clickListener: ClassCardClickListener)
    : RecyclerView.ViewHolder(teacherClassCardBinding.root)
{
    fun bindTeacherClass(teacherClass: TeacherClass) {
        val time = "${teacherClass.startTime} - ${teacherClass.endTime}"

        teacherClassCardBinding.className.text = teacherClass.name
        teacherClassCardBinding.weekDay.text = teacherClass.weekDay
        teacherClassCardBinding.timeStamp.text = time

        teacherClass.description?.let {
            teacherClassCardBinding.classDescription.text = it
        }
        teacherClassCardBinding.cardLayout.setBackgroundResource(getResource(teacherClass.backgroundId))
        teacherClassCardBinding.root.setOnClickListener {
            clickListener.onClick(teacherClass)
        }

        teacherClassCardBinding.card.setOnClickListener {
            clickListener.onClick(teacherClass)
        }
    }
}

private fun getResource(id: Int?): Int {
    return when (id) {
        1 -> R.drawable.gradient1
        2 -> R.drawable.gradient2
        3 -> R.drawable.gradient3
        4 -> R.drawable.gradient4
        5 -> R.drawable.gradient5
        6 -> R.drawable.gradient6
        else -> getResource((1..6).random())
    }
}