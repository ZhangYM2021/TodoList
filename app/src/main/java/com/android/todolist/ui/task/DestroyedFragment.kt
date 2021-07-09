package com.android.todolist.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.todolist.databinding.FragmentDeletedBinding
import com.android.todolist.databinding.FragmentDestroyedBinding
import com.android.todolist.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar

class DestroyedFragment: Fragment() {
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDestroyedBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModeldestroy = viewModel

        adapter = TaskAdapter(TaskClickListener { taskEntry ->
            findNavController().navigate(TaskFragmentDirections.actionTaskFragmentToUpdateFragment(taskEntry))
        }) { taskEntry ->
            viewModel.delete(taskEntry)
        }

        viewModel.getDestroyedTasks.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.apply {
            this!!.destroyedView.setHasFixedSize(true)
            binding.destroyedView.adapter = adapter
        }

        ItemTouchHelper(object  : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskEntry = adapter.currentList[position]
                taskEntry.destroyed = true
                viewModel.delete(taskEntry)

                Snackbar.make(binding.root, "Deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.insert(taskEntry)
                        taskEntry.deleted = false
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.destroyedView)

        //setHasOptionsMenu(true)

        //hideKeyboard(requireActivity())


        return binding.root
    }
}