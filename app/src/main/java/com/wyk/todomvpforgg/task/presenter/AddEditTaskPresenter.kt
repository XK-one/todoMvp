package com.wyk.todomvpforgg.task.presenter

import com.wyk.todomvpforgg.task.contract.AddEditTaskContract
import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.task.data.source.TasksDataSource

class AddEditTaskPresenter(
        private val taskId: String?,
        val tasksRepository: TasksDataSource,
        val addTaskView: AddEditTaskContract.View,
        override var isDataMissing: Boolean
): AddEditTaskContract.Presenter, TasksDataSource.GetTaskCallback {

    var currTask: Task? = null

    override fun getStatusById(taskId: String) {
        tasksRepository.getTask(taskId, object: TasksDataSource.GetTaskCallback{
            override fun onTaskLoaded(task: Task) {
                currTask = task
            }
            override fun onDataNotAvailable() {
                //
            }
        })
    }

    init {
        addTaskView.presenter = this
    }

    /**添加或编辑操作*/
    override fun saveTask(title: String, description: String) {
        if(taskId.isNullOrBlank()){
            createTask(title,description)
        }else{
            updateTask(title,description)
        }
    }
    private fun updateTask(title: String, description: String) {
        if(taskId == null){
            throw RuntimeException("updateTask() was called but task is new.")
        }
        tasksRepository.saveTask(Task(title, description, taskId)
                /*.apply {
            isCompleted = currTask?.isCompleted?: false
        }*/)
        addTaskView.showTasksList()
    }
    private fun createTask(title: String, description: String) {
        val task = Task(title, description)
        if(task.isEmpty){
            addTaskView.showEmptyTaskError()
        }else{
            tasksRepository.saveTask(task)
            addTaskView.showTasksList()
        }
    }

    override fun start() {
        if(taskId != null && isDataMissing){
            populateTask()
            getStatusById(taskId)
        }
    }
    override fun populateTask() {
        if (taskId == null) {
            throw RuntimeException("populateTask() was called but task is new.")
        }
        tasksRepository.getTask(taskId, this)
    }
    override fun onTaskLoaded(task: Task) {
        if(addTaskView.isActive){
            addTaskView.setTitle(task.title)
            addTaskView.setDescription(task.description)
        }
        isDataMissing = false
    }

    override fun onDataNotAvailable() {
        if(addTaskView.isActive){
            addTaskView.showEmptyTaskError()
        }
    }
}