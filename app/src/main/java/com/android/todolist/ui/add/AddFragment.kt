package com.android.todolist.ui.add

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.todolist.R
import com.android.todolist.database.TaskEntry
import com.android.todolist.databinding.FragmentAddBinding
import com.android.todolist.viewmodel.TaskViewModel
import java.util.*


class AddFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private val mAlarmManager: AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val calendar: Calendar = Calendar.getInstance()

    companion object {
        class AlarmData(var time: Long = 0,
                        var date: Calendar = Calendar.getInstance(),
                        private var timeLabel: String = "") {
            init {
                date.timeInMillis = time
                timeLabel = "${date.get(Calendar.YEAR)}.${date.get(Calendar.MONTH) + 1}" +
                        ".${date.get(Calendar.DAY_OF_MONTH)} ${date.get(Calendar.HOUR_OF_DAY)}:${date.get(Calendar.MINUTE)}"
            }

            override fun toString(): String {
                return timeLabel
            }

            fun getId(): Int {
                return (time / (1000 * 60)).toInt()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentAddBinding.inflate(inflater)

        val myAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.priorities)
        )

        binding.apply {
            spinner.adapter = myAdapter
            btnAlarm.setOnClickListener {
                setAlarm()
            }
            btnAdd.setOnClickListener {
                if(TextUtils.isEmpty((edtTask.text))){
                    Toast.makeText(requireContext(), "It's empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val title_str = edtTask.text.toString()
                val priority = spinner.selectedItemPosition
                val type = type.selectedItemPosition
                val AlarmTime = "${calendar.get(Calendar.YEAR)}.${calendar.get(Calendar.MONTH) + 1}" +
                        ".${calendar.get(Calendar.DAY_OF_MONTH)} ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"

                val taskEntry = TaskEntry(
                    0,
                    title_str,
                    priority,
                    type,
                    System.currentTimeMillis(),
                    deleted = false,
                    destroyed = false,
                    AlarmTime
                )

                viewModel.insert(taskEntry)
                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_taskFragment)
                //val intent = Intent(".Alarm.AlarmReceiver")
                //sendBroadcast(intent)
                Intent().also { intent ->
                    intent.setAction(".Alarm.AlarmReceiver")
                    sendBroadcast(intent)
                }
            }
        }

        return binding.root
    }

    private fun sendBroadcast(intent: Intent) {

        sendBroadcast(intent)

    }

    private fun setAlarm() {
        calendar.apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            context?.let {
                DatePickerDialog(
                    it,
                    0,
                    { _, year, month, day ->
                        this.set(Calendar.YEAR, year)
                        this.set(Calendar.MONTH, month)
                        this.set(Calendar.DAY_OF_MONTH, day)
                        TimePickerDialog(
                            context,
                            0,
                            { _, hour, minute ->
                                this.set(Calendar.HOUR_OF_DAY, hour)
                                this.set(Calendar.MINUTE, minute)
                            },
                            this.get(Calendar.HOUR_OF_DAY),
                            this.get(Calendar.MINUTE),
                            false
                        ).show()
                    },
                    this.get(Calendar.YEAR),
                    this.get(Calendar.MONTH),
                    this.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

}