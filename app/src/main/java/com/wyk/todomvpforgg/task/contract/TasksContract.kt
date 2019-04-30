package com.wyk.todomvpforgg.task.contract

import com.wyk.todomvpforgg.base.IBaseView
import com.wyk.todomvpforgg.base.IPresenter
import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.task.data.model.TasksFilterType

/**
 * 显示任务的契约类
 */
interface TasksContract {

    interface Presenter: IPresenter{
        var currentFiltering: TasksFilterType

        fun loadTasks(forceUpdate: Boolean)
        fun result(requestCode: Int, resultCode: Int)
        fun openTaskDetails(requestedTask: Task)
        fun completeTask(completedTask: Task)
        fun activateTask(activeTask: Task)
        fun clearCompletedTasks()
        fun addNewTask()

    }
    interface View: IBaseView<Presenter>{
        fun setLoadingIndicator(active: Boolean)
        var isActive: Boolean
        fun showLoadingTasksError()
        fun showTasks(tasks: List<Task>)
        fun showNoActiveTasks()
        fun showNoCompletedTasks()
        fun showNoTasks()
        fun showActiveFilterLabel()
        fun showCompletedFilterLabel()
        fun showAllFilterLabel()
        fun showFilteringPopUpMenu()
        fun showAddTask()
        fun showTaskDetailsUi(taskId: String)
        fun showSuccessfullySavedMessage()
        fun showTaskMarkedComplete()
        fun showTaskMarkedActivefun ()
        fun showCompletedTasksCleared()
    }

}