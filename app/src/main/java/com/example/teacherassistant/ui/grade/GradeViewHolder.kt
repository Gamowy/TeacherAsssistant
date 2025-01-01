package com.example.teacherassistant.ui.grade

import androidx.recyclerview.widget.RecyclerView
import com.example.teacherassistant.R
import com.example.teacherassistant.databinding.GradeViewBinding
import com.example.teacherassistant.models.Grade

class GradeViewHolder(private val gradeViewBinding: GradeViewBinding,
                      private val clickListener: GradeViewClickListener): RecyclerView.ViewHolder(gradeViewBinding.root)
{
        fun bindGrade(grade: Grade) {
            val gradeValue = grade.grade.toString()
            gradeViewBinding.gradeValue.text = gradeValue

            when (gradeValue) {
                "2" -> gradeViewBinding.gradeViewContainer.setBackgroundResource(R.drawable.background_2)
                "3", "3.5" -> gradeViewBinding.gradeViewContainer.setBackgroundResource(R.drawable.background_3)
                "4", "4.5" -> gradeViewBinding.gradeViewContainer.setBackgroundResource(R.drawable.background_4)
                "5" -> gradeViewBinding.gradeViewContainer.setBackgroundResource(R.drawable.background_5)
                else -> gradeViewBinding.gradeViewContainer.setBackgroundResource(R.drawable.background_other)
            }

            gradeViewBinding.root.setOnClickListener {
                clickListener.onGradeClick(grade)
            }
        }
}