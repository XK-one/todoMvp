package com.wyk.todomvpforgg.task.data.source

import com.wyk.todomvpforgg.task.data.model.Task

interface TasksDataSource {

    interface LoadTasksCallback {
        fun onTasksLoaded(tasks: List<Task>)
        fun onDataNotAvailable()
    }

    interface GetTaskCallback {
        fun onTaskLoaded(task: Task)
        fun onDataNotAvailable()
    }

    fun refreshTasks()
    fun getTasks(callback: LoadTasksCallback)
    fun getTask(taskId: String, callback: GetTaskCallback)

    fun deleteAllTasks()
    fun saveTask(task: Task)

    fun activateTask(activeTask: Task)
    fun completedTask(completedTask: Task)
    fun deleteTask(taskId: String)
    fun completeTask(taskId: String)
    fun activateTask(taskId: String)

    fun clearCompletedTasks()

}