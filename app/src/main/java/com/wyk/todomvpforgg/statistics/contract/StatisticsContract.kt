package com.wyk.todomvpforgg.statistics.contract

import com.wyk.todomvpforgg.base.IBaseView
import com.wyk.todomvpforgg.base.IPresenter

interface StatisticsContract {

    interface Presenter: IPresenter{

    }
    interface View: IBaseView<Presenter>{
        val isActive: Boolean
        fun setProgressIndicator(active: Boolean)
        fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompletedTasks: Int)
        fun showLoadingStatisticsError()
    }
}