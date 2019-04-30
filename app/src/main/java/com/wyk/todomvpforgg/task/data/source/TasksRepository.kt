package com.wyk.todomvpforgg.task.data.source

import com.google.common.collect.Lists
import com.wyk.todomvpforgg.task.data.model.Task

class TasksRepository
        constructor(val tasksRemoteDataSource: TasksDataSource,val tasksLocalDataSource: TasksDataSource): TasksDataSource {

    override fun clearCompletedTasks() {
       tasksRemoteDataSource.clearCompletedTasks()
       tasksLocalDataSource.clearCompletedTasks()
       cachedTasks = cachedTasks.filterValues {
            !it.isCompleted
       } as LinkedHashMap<String, Task>
    }

    override fun deleteTask(taskId: String) {
        cachedTasks.remove(taskId)
        tasksRemoteDataSource.deleteTask(taskId)
        tasksLocalDataSource.deleteTask(taskId)
    }

    override fun activateTask(activeTask: Task) {
        cacheAndPerform(activeTask){
            tasksRemoteDataSource.activateTask(activeTask)
            tasksLocalDataSource.activateTask(activeTask)
        }
    }
    override fun activateTask(taskId: String) {
        getTaskWithId(taskId)?.let {
            activateTask(it)
        }
    }

    override fun completedTask(completedTask: Task) {
        cacheAndPerform(completedTask){
            tasksRemoteDataSource.completedTask(completedTask)
            tasksLocalDataSource.completedTask(completedTask)
        }
    }
    override fun completeTask(taskId: String) {
        getTaskWithId(taskId)?.let{
            completedTask(it)
        }
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val task = getTaskWithId(taskId)
        if(task != null){
            callback.onTaskLoaded(task)
            return
        }
        tasksLocalDataSource.getTask(taskId, object: TasksDataSource.GetTaskCallback{
            override fun onTaskLoaded(task: Task) {
                cacheAndPerform(task){
                    callback.onTaskLoaded(task)
                }
            }
            override fun onDataNotAvailable() {
                tasksRemoteDataSource.getTask(taskId, object: TasksDataSource.GetTaskCallback{
                    override fun onTaskLoaded(task: Task) {
                        cacheAndPerform(task){
                            callback.onTaskLoaded(it)
                        }
                    }
                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })


    }

    private fun getTaskWithId(taskId: String) = cachedTasks[taskId]

    override fun deleteAllTasks() {

    }

    override fun saveTask(task: Task) {
        cacheAndPerform(task){
            tasksRemoteDataSource.saveTask(task)
            tasksLocalDataSource.saveTask(task)
        }
    }

    var cachedTasks: LinkedHashMap<String,Task> = LinkedHashMap()
    var cacheIsDirty = false

    override fun refreshTasks() {
        cacheIsDirty = true
    }

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        if(cachedTasks.isNotEmpty() && !cacheIsDirty){
            callback.onTasksLoaded(ArrayList(cachedTasks.values))
        }
        if(cacheIsDirty){
            getTasksFromRemoteDataSource(callback)
        }else{
            tasksLocalDataSource.getTasks(object: TasksDataSource.LoadTasksCallback{
                override fun onTasksLoaded(tasks: List<Task>) {
                    refreshCache(tasks)
                    callback.onTasksLoaded(Lists.newArrayList(cachedTasks.values))
                }
                override fun onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback)
                }
            })
        }
    }

    private fun refreshCache(tasks: List<Task>){
        cachedTasks.clear()
        tasks.forEach {
            cacheAndPerform(it){
            }
        }
    }
    private fun refreshLocalDataSource(tasks: List<Task>){
        tasksLocalDataSource.deleteAllTasks()
        tasks.forEach {
            tasksLocalDataSource.saveTask(it)
        }
    }

    private inline fun cacheAndPerform(task: Task, perform: (Task) -> Unit){
        val cacheTask = Task(task.title, task.description, task.id).apply {
            isCompleted = task.isCompleted
        }
        cachedTasks.put(cacheTask.id, cacheTask)
        perform(cacheTask)

    }


    /**网络重新获取数据*/
    private fun getTasksFromRemoteDataSource(callback: TasksDataSource.LoadTasksCallback) {
        tasksRemoteDataSource.getTasks(object: TasksDataSource.LoadTasksCallback{
            override fun onTasksLoaded(tasks: List<Task>) {
                refreshLocalDataSource(tasks)
                refreshCache(tasks)
                callback.onTasksLoaded(Lists.newArrayList(cachedTasks.values))
            }
            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    companion object {

            private var mInstance: TasksRepository? = null
            private val mLock = Any()

            fun getInstance(tasksRemoteDataSource: TasksDataSource, tasksLocalDataSource: TasksDataSource) =
                    mInstance
                            ?: synchronized(mLock){
                            mInstance
                                    ?: TasksRepository(tasksRemoteDataSource, tasksLocalDataSource).apply {
                                    mInstance = this
                            }
                    }

        }

}