package com.wyk.todomvpforgg.task.data.source

import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.util.AppExecutors

class TasksLocalDataSource private constructor(
        val executors: AppExecutors, val dao: TasksDao) : TasksDataSource {

    override fun activateTask(taskId: String) {

    }
    override fun completeTask(taskId: String) {

    }

    override fun clearCompletedTasks() {
        executors.mDiskIO.execute { dao.deleteCompletedTasks()}
    }
    override fun deleteTask(taskId: String) {
        executors.mDiskIO.execute { dao.deleteTaskById(taskId) }
    }

    override fun activateTask(activeTask: Task) {
        executors.mDiskIO.execute { dao.updateCompleted(activeTask.id, false)}
    }

    override fun completedTask(completedTask: Task) {
        executors.mDiskIO.execute { dao.updateCompleted(completedTask.id, true) }
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        executors.mDiskIO.execute {
            val task = dao.getTaskById(taskId)
            executors.mMainThread.execute {
                if(task != null){
                    callback.onTaskLoaded(task)
                }else{
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun deleteAllTasks() {
        executors.mDiskIO.execute { dao.deleteAllTasks() }
    }

    override fun saveTask(task: Task) {
        executors.mDiskIO.execute { dao.insertTask(task) }
    }

    override fun refreshTasks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        executors.mDiskIO.execute {
            val tasks = dao.getTasks()
            executors.mMainThread.execute {
                if(!tasks.isEmpty()){
                    callback.onTasksLoaded(tasks)
                }else{
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    /**伴生对象*/
    companion object {
        private var mLocalDataSource: TasksLocalDataSource? = null
        fun newInstance(executors: AppExecutors, dao:TasksDao) =
                mLocalDataSource ?: synchronized(this){
                    mLocalDataSource ?: TasksLocalDataSource(executors,dao).apply {
                        mLocalDataSource = this
                    }
                }
    }


}