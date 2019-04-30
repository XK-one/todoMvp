package com.wyk.todomvpforgg.task.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wyk.todomvpforgg.task.data.model.Task

@Database(entities = [Task::class], version = 1)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun taskDao(): TasksDao

    companion object {
        private var mInstance: ToDoDatabase? = null
        fun getInstance(context: Context) = mInstance?:
                synchronized(this){
                    mInstance?: Room.databaseBuilder(context.applicationContext,
                            ToDoDatabase::class.java, "Tasks.db")
                            .build()
                            .also { mInstance = it }
                }
    }

}