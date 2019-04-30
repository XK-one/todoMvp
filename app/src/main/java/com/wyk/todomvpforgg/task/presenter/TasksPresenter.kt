package com.wyk.todomvpforgg.task.presenter

import android.app.Activity
import com.wyk.todomvpforgg.task.contract.TasksContract
import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.task.data.model.TasksFilterType
import com.wyk.todomvpforgg.task.data.source.TasksDataSource
import com.wyk.todomvpforgg.task.data.source.TasksRepository
import com.wyk.todomvpforgg.task.view.AddEditTaskActivity

class TasksPresenter(val taskRepository: TasksRepository, val view: TasksContract.View): TasksContract.Presenter{
    override fun addNewTask() {
        view.showAddTask()
    }

    override var currentFiltering: TasksFilterType = TasksFilterType.ALL_TASKS
    private var firstLoad = true

    init {
        view.presenter = this
    }

    override fun start() {
        loadTasks(false)
    }

    override fun loadTasks(forceUpdate: Boolean) {
        loadTasks(forceUpdate || firstLoad, true)
        firstLoad = false
    }

    private fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if(showLoadingUI){
            view.setLoadingIndicator(true)
        }
        if(forceUpdate){
            taskRepository.refreshTasks()
        }
        taskRepository.getTasks(object: TasksDataSource.LoadTasksCallback{
            override fun onTasksLoaded(tasks: List<Task>) {
                val tasksToShow = ArrayList<Task>()
                for(task in tasks){
                    when(currentFiltering){
                        TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                        TasksFilterType.ACTIVE_TASKS -> if(task.isActive) tasksToShow.add(task)
                        TasksFilterType.COMPLETE_TASKS -> if(task.isCompleted) tasksToShow.add(task)
                    }
                }
                if(!view.isActive){
                    return
                }
                if(showLoadingUI){
                    view.setLoadingIndicator(false)
                }
                processTasks(tasksToShow)
            }
            override fun onDataNotAvailable() {
                if(!view.isActive){
                    return
                }
                view.showLoadingTasksError()
            }

        })
    }

    private fun processTasks(tasksToShow: ArrayList<Task>) {
        if(tasksToShow.isEmpty()){
            processEmptyTasks()
        }else{
            view.showTasks(tasksToShow)
            showFilterLabel()
        }
    }

    /**显示数据上面的标签*/
    private fun showFilterLabel() {
        when(currentFiltering){
            TasksFilterType.COMPLETE_TASKS -> view.showCompletedFilterLabel()
            TasksFilterType.ACTIVE_TASKS -> view.showActiveFilterLabel()
            else -> view.showAllFilterLabel()
        }
    }
    /**处理无数据时，页面的显示情况*/
    private fun processEmptyTasks() {
        when(currentFiltering){
            TasksFilterType.COMPLETE_TASKS -> view.showNoCompletedTasks()
            TasksFilterType.ACTIVE_TASKS -> view.showNoActiveTasks()
            else -> view.showNoTasks()
        }

    }

    override fun result(requestCode: Int, resultCode: Int) {
        if(requestCode == AddEditTaskActivity.REQUEST_ADD_TASK &&
                resultCode == Activity.RESULT_OK){
            view.showSuccessfullySavedMessage()
        }
    }

    override fun completeTask(completedTask: Task) {
        taskRepository.completedTask(completedTask)
        view.showTaskMarkedComplete()
        loadTasks(false, false)
    }

    override fun activateTask(activeTask: Task) {
        taskRepository.activateTask(activeTask)
        view.showTaskMarkedActivefun()
        loadTasks(false, false)
    }

    override fun clearCompletedTasks() {
        taskRepository.clearCompletedTasks()
        view.showCompletedTasksCleared()
        loadTasks(false,false)

    }
    override fun openTaskDetails(requestedTask: Task) {
        view.showTaskDetailsUi(requestedTask.id)

    }

}