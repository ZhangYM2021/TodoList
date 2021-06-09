package com.android.todolist.ui.task

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.todolist.R
import com.android.todolist.database.TaskEntry
import com.android.todolist.databinding.RowLayoutBinding
import com.android.todolist.viewmodel.TaskViewModel
import com.bumptech.glide.Glide

class TaskAdapter(private val clickListener: TaskClickListener, val deleteListener: (taskEntry: TaskEntry) -> Unit) : ListAdapter<TaskEntry, TaskAdapter.ViewHolder>(TaskDiffCallback) {

    companion object TaskDiffCallback : DiffUtil.ItemCallback<TaskEntry>(){
        override fun areItemsTheSame(oldItem: TaskEntry, newItem: TaskEntry) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TaskEntry, newItem: TaskEntry) = oldItem == newItem
    }

    class ViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(taskEntry: TaskEntry, clickListener: TaskClickListener){
            binding.taskEntry = taskEntry
            binding.clickListener = clickListener
            binding.executePendingBindings()
            binding.apply {
                when (taskType.text) {
                    "Work" -> {
                        Glide.with(itemView).load(R.drawable.ic_work).into(imageView)
                    }
                    "Study" -> {
                        Glide.with(itemView).load(R.drawable.ic_study).into(imageView)
                    }
                    "Exercise" -> {
                        Glide.with(itemView).load(R.drawable.ic_exercise).into(imageView)
                    }
                    "Parenting" -> {
                        Glide.with(itemView).load(R.drawable.ic_parenting).into(imageView)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, clickListener)

        holder.binding.cbTodo.apply {
            setOnClickListener {
                holder.binding.apply {
                    if (isChecked) {
                        deleteListener(current)
                        //taskEntry.deleted = true
                        taskTitle.paintFlags =
                                taskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    } else {
                        taskTitle.paintFlags =
                                taskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                }
            }
        }
    }
}

class TaskClickListener(val clickListener: (taskEntry: TaskEntry) -> Unit){
    fun onClick(taskEntry: TaskEntry) = clickListener(taskEntry)
}