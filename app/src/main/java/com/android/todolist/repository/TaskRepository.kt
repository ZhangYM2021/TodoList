package com.android.todolist.repository

import androidx.lifecycle.LiveData
import com.android.todolist.database.TaskDao
import com.android.todolist.database.TaskEntry

class TaskRepository(val taskDao: TaskDao) {

    suspend fun insert(taskEntry: TaskEntry) = taskDao.insert(taskEntry)

    suspend fun updateData(taskEntry: TaskEntry) = taskDao.update(taskEntry)

    suspend fun deleteItem(taskEntry: TaskEntry) = taskDao.delete(taskEntry)

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    fun getAllTasks() : LiveData<List<TaskEntry>> = taskDao.getAllTasks()

    fun getDeletedTasks() : LiveData<List<TaskEntry>> = taskDao.getDeletedTasks()

    fun getDestroyedTasks() : LiveData<List<TaskEntry>> = taskDao.getDestroyedTasks()

    fun getAllPriorityTasks() : LiveData<List<TaskEntry>> = taskDao.getAllPriorityTasks()

    fun searchDatabase(searchQuery: String): LiveData<List<TaskEntry>> {
        return taskDao.searchDatabase(searchQuery)
    }

    fun timeOrder(): LiveData<List<TaskEntry>> = taskDao.timeOrder()
}