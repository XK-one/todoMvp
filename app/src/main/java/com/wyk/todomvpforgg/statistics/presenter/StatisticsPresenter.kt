package com.wyk.todomvpforgg.statistics.presenter

import com.wyk.todomvpforgg.statistics.contract.StatisticsContract
import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.task.data.source.TasksDataSource
import com.wyk.todomvpforgg.task.data.source.TasksRepository

class StatisticsPresenter(val tasksRepository: TasksRepository,
                          val statisticsView: StatisticsContract.View):  StatisticsContract.Presenter{

    init {
        statisticsView.presenter = this
    }
    override fun start() {
        loadStatistics()
    }
    private fun loadStatistics() {
        statisticsView.setProgressIndicator(true)
        tasksRepository.getTasks(object: TasksDataSource.LoadTasksCallback{
            override fun onTasksLoaded(tasks: List<Task>) {
                if(!statisticsView.isActive){
                    return
                }
                val completedNum = tasks.filter { it.isCompleted }.size
                val inCompletedNum = tasks.size - completedNum
                statisticsView.setProgressIndicator(false)
                statisticsView.showStatistics(inCompletedNum, completedNum)

            }
            override fun onDataNotAvailable() {
                if(!statisticsView.isActive){
                    return
                }
                statisticsView.showLoadingStatisticsError()
            }
        })
    }
}