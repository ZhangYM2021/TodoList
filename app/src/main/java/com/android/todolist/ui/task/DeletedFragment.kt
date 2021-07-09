package com.android.todolist.ui.task

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.todolist.R
import com.android.todolist.databinding.FragmentDeletedBinding
import com.android.todolist.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar

class DeletedFragment: Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDeletedBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModeldelete = viewModel

        adapter = TaskAdapter(TaskClickListener { taskEntry ->
            findNavController().navigate(TaskFragmentDirections.actionTaskFragmentToUpdateFragment(taskEntry))
        }) { taskEntry ->
            viewModel.delete(taskEntry)
        }

        viewModel.getDeletedTasks.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.apply {
            this!!.deletedView.setHasFixedSize(true)
            binding.deletedView.adapter = adapter
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
                viewModel.update(taskEntry)

                Snackbar.make(binding.root, "Deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.insert(taskEntry)
                        taskEntry.destroyed = false
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.deletedView)

        //setHasOptionsMenu(true)

        //hideKeyboard(requireActivity())


        return binding.root
    }

    /*private fun hideKeyboard(activity: Activity){
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        inputMethodManager.hideSoftInputFromWindow(currentFocusedView?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    runQuery(newText)
                }
                return true
            }

        })
    }

    fun runQuery(query: String){
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, { tasks ->
            adapter.submitList(tasks)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_priority -> viewModel.getAllPriorityTasks.observe(viewLifecycleOwner,
                { tasks ->
                    adapter.submitList(tasks)
                })
            R.id.action_delete_all -> deleteAllItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllItem() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes"){dialog, _ ->
                viewModel.deleteAll()
                dialog.dismiss()
            }.setNegativeButton("No"){dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }*/
}