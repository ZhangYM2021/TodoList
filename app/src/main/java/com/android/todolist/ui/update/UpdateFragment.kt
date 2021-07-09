package com.android.todolist.ui.update

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.todolist.R
import com.android.todolist.database.TaskEntry
import com.android.todolist.databinding.FragmentUpdateBinding
import com.android.todolist.viewmodel.TaskViewModel
import java.util.*


class UpdateFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentUpdateBinding.inflate(inflater)

        val args = UpdateFragmentArgs.fromBundle(requireArguments())

        binding.apply {
            updateEdtTask.setText(args.taskEntry.title)
            updateSpinner.setSelection(args.taskEntry.priority)
            updateType.setSelection(args.taskEntry.type)

            btnUpdate.setOnClickListener {
                if(TextUtils.isEmpty(updateEdtTask.text)){
                    Toast.makeText(requireContext(), "It's empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val task_str = updateEdtTask.text
                val priority = updateSpinner.selectedItemPosition
                val type = updateType.selectedItemPosition
                val AlarmTime = "${calendar.get(Calendar.YEAR)}.${calendar.get(Calendar.MONTH) + 1}" +
                        ".${calendar.get(Calendar.DAY_OF_MONTH)} ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

                val taskEntry = TaskEntry(
                    args.taskEntry.id,
                    task_str.toString(),
                    priority,
                    type,
                    args.taskEntry.timestamp,
                    deleted = false,
                    destroyed = false,
                    AlarmTime
                )

                viewModel.update(taskEntry)
                Toast.makeText(requireContext(), "Updated!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_taskFragment)
            }
        }

        return binding.root
    }

}