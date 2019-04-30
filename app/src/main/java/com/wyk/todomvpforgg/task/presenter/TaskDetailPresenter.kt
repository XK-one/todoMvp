package com.wyk.todomvpforgg.task.presenter

import com.wyk.todomvpforgg.task.contract.TaskDetailContract
import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.task.data.source.TasksDataSource
import com.wyk.todomvpforgg.task.data.source.TasksRepository

class TaskDetailPresenter(val taskId: String,
                          val tasksRepository: TasksRepository,
                          val mDetailView: TaskDetailContract.View): TaskDetailContract.Presenter {
    override fun completeTask() {
        if(taskId.isEmpty()){
            mDetailView.showMissingTask()
            return
        }
        tasksRepository.completeTask(taskId)
        mDetailView.showTaskMarkedComplete()
    }
    override fun activateTask() {
        if(taskId.isEmpty()){
            mDetailView.showMissingTask()
            return
        }
        tasksRepository.activateTask(taskId)
        mDetailView.showTaskMarkedActive()
    }

    init {
        mDetailView.presenter = this
    }

    override fun deleteTask() {
        if(taskId.isEmpty()){
            mDetailView.showMissingTask()
            return
        }
        tasksRepository.deleteTask(taskId)
        mDetailView.showTaskDeleted()
    }

    override fun editTask() {
        if(taskId.isEmpty()){
            mDetailView.showMissingTask()
            return
        }
        mDetailView.showEditTask(taskId)
    }



    override fun start() {
        openTask()
    }

    private fun openTask() {
        if(taskId.isEmpty()){
            mDetailView.showMissingTask()
            return
        }
        mDetailView.setLoadingIndicator(true)
        tasksRepository.getTask(taskId, object: TasksDataSource.GetTaskCallback{
            override fun onTaskLoaded(task: Task) {
                with(mDetailView){
                    if(!isActive){
                        return
                    }
                    setLoadingIndicator(false)
                }
                showTask(task)
            }
            override fun onDataNotAvailable() {
                with(mDetailView){
                    if(!isActive){
                        return
                    }
                    showMissingTask()
                }
            }
        })
    }

    private fun showTask(task: Task) {
        with(mDetailView){
            if(taskId.isEmpty()){
                hideTitle()
                hideDescription()
            }else{
                showTitle(task.title)
                showDescription(task.description)
            }
            mDetailView.showCompletionStatus(task.isCompleted)
        }
    }


}