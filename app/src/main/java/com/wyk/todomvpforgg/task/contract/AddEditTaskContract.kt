package com.wyk.todomvpforgg.task.contract

import com.wyk.todomvpforgg.base.IBaseView
import com.wyk.todomvpforgg.base.IPresenter
import com.wyk.todomvpforgg.task.data.model.Task

/**
 * 新增编辑任务的契约类
 */
interface AddEditTaskContract {

    interface Presenter: IPresenter{
        var isDataMissing: Boolean
        fun populateTask()
        fun saveTask(title: String, description: String)
        fun getStatusById(taskId: String)
    }

    interface View: IBaseView<Presenter>{
        var isActive: Boolean
        fun showEmptyTaskError()
        fun showTasksList()
        fun setTitle(title: String)
        fun setDescription(description: String)
        fun setEditTask(task: Task)
    }
}