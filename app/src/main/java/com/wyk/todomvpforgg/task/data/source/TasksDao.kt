package com.wyk.todomvpforgg.task.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wyk.todomvpforgg.task.data.model.Task

@Dao interface TasksDao {

        @Query("SELECT * FROM TASKS")
        fun getTasks(): List<Task>

        @Query("SELECT * FROM TASKS WHERE entryid = :entryId")
        fun getTaskById(entryId: String): Task?

        @Query("DELETE FROM TASKS")
        fun deleteAllTasks()

        @Query("DELETE FROM TASKS WHERE entryid = :taskId")
        fun deleteTaskById(taskId: String):Int

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertTask(task: Task)

        @Query("UPDATE TASKS SET completed = :isCompleted WHERE entryid = :entryId")
        fun updateCompleted(entryId: String, isCompleted: Boolean)

        @Query("DELETE FROM TASKS WHERE completed = 1")
        fun deleteCompletedTasks(): Int

}