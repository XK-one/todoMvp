package com.wyk.todomvpforgg.data

import com.google.common.collect.Lists
import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.task.data.source.TasksDataSource

class FakeTasksRemoteDataSource private constructor(): TasksDataSource {

    override fun clearCompletedTasks() {
        with(TASKS_SERVICE_DATA.entries.iterator()){
            while (hasNext()){
                if(next().value.isCompleted){
                    remove()
                }
            }
        }
        /*TASKS_SERVICE_DATA = TASKS_SERVICE_DATA.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>*/
    }

    override fun activateTask(taskId: String) {

    }
    override fun completeTask(taskId: String) {

    }
    override fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }

    override fun activateTask(activeTask: Task) {
        //TASKS_SERVICE_DATA[activeTask.id]?.isCompleted = false
        val newTask = Task(activeTask.title, activeTask.description, activeTask.id)
        TASKS_SERVICE_DATA.put(activeTask.id, newTask)
    }
    override fun completedTask(completedTask: Task) {
        //TASKS_SERVICE_DATA[completedTask.id]?.isCompleted = true
        val newTask = Task(completedTask.title, completedTask.description, completedTask.id).apply{
            isCompleted = true
        }
        TASKS_SERVICE_DATA.put(completedTask.id, newTask)
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val task =  TASKS_SERVICE_DATA[taskId]
        if(task != null){
            callback.onTaskLoaded(task)
        }else{
            callback.onDataNotAvailable()
        }
    }
    override fun deleteAllTasks() {

    }

    override fun saveTask(task: Task) {
        TASKS_SERVICE_DATA[task.id] =  task
    }

    private var TASKS_SERVICE_DATA: LinkedHashMap<String, Task> = LinkedHashMap()


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