package com.wyk.todomvpforgg.task.contract

import com.wyk.todomvpforgg.base.IBaseView
import com.wyk.todomvpforgg.base.IPresenter

interface TaskDetailContract {

    interface Presenter: IPresenter{
        fun editTask()
        fun deleteTask()
        fun completeTask()
        fun activateTask()
    }
    interface View: IBaseView<Presenter>{
        var isActive: Boolean
        fun showMissingTask()
        fun showEditTask(taskId: String)
        fun showTaskDeleted()
        fun hideTitle()
        fun hideDescription()
        fun showTitle(title: String)
        fun showDescription(description: String)
        fun setLoadingIndicator(active: Boolean)
        fun showCompletionStatus(complete: Boolean)
        fun showTaskMarkedComplete()
        fun showTaskMarkedActive()
    }
}