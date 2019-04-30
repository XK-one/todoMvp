package com.wyk.todomvpforgg

import android.content.Context
import com.wyk.todomvpforgg.data.FakeTasksRemoteDataSource
import com.wyk.todomvpforgg.task.data.source.TasksRepository
import com.wyk.todomvpforgg.task.data.source.TasksLocalDataSource
import com.wyk.todomvpforgg.task.data.source.ToDoDatabase
import com.wyk.todomvpforgg.util.AppExecutors

object Injection {
    fun provideTaskRepository(context: Context): TasksRepository {
        val database = ToDoDatabase.getInstance(context)
        return TasksRepository.getInstance(FakeTasksRemoteDataSource.newInstance(),
                TasksLocalDataSource.newInstance(AppExecutors(),database.taskDao()))
    }
}