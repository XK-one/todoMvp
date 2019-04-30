package com.wyk.todomvpforgg.data

import android.accounts.AuthenticatorDescription
import com.google.common.collect.Lists
import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.task.data.source.TasksDataSource

class FakeTasksRemoteDataSource private constructor(): TasksDataSource {
    override fun deleteAllTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveTask(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val TASKS_SERVICE_DATA: LinkedHashMap<String, Task> = LinkedHashMap()

    init {
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }
    private fun addTask(title: String, description: String){
        val task = Task(title, description)
        TASKS_SERVICE_DATA[task.id] = task
    }

    override fun refreshTasks() {

    }
    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values))
    }


    companion object {
        var mRemoteDataSource: FakeTasksRemoteDataSource? = null
        fun newInstance() = mRemoteDataSource ?:
            synchronized(this){
                mRemoteDataSource?: FakeTasksRemoteDataSource().also { mRemoteDataSource = it }
            }
    }
}