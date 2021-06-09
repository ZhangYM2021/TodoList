package com.android.todolist.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DateFormat

@SuppressLint("SetTextI18n")
@BindingAdapter("setPriority")
fun setPriority(view: TextView, priority: Int){
    when(priority){
        0 -> {
            view.text = "High Priority"
            view.setTextColor(Color.RED)
        }
        1 -> {
            view.text = "Medium Priority"
            view.setTextColor(Color.BLUE)
        }
        else -> {
            view.text = "Low Priority"
            view.setTextColor(Color.GREEN)
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setType")
fun setType(view: TextView, type: Int){
    when(type){
        0 -> {
            view.text = "Work"
            view.setTextColor(Color.RED)
        }
        1 -> {
            view.text = "Study"
            view.setTextColor(Color.BLUE)
        }
        2 -> {
            view.text = "Exercise"
            view.setTextColor(Color.YELLOW)
        }
        else -> {
            view.text = "Parenting"
            view.setTextColor(Color.GREEN)
        }
    }
}

@BindingAdapter("setTimestamp")
fun setTimestamp(view: TextView, timestamp: Long){
    view.text = DateFormat.getInstance().format(timestamp)
}