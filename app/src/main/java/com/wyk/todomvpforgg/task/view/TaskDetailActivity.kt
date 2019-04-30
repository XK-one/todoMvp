package com.wyk.todomvpforgg.task.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wyk.todomvpforgg.Injection
import com.wyk.todomvpforgg.R
import com.wyk.todomvpforgg.task.presenter.TaskDetailPresenter
import com.wyk.todomvpforgg.util.replaceFragmentInActivity
import com.wyk.todomvpforgg.util.setupActionBar

/**
 * 任务详情
 */
class TaskDetailActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.taskdetail_act)

        val taskId = intent.getStringExtra(EXTRA_TASK_ID)
        setupActionBar(R.id.toolbar){
            //箭头返回上一页
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val taskDetailFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as TaskDetailFragment? ?: TaskDetailFragment.newIntance(taskId).also {
            replaceFragmentInActivity(it,R.id.contentFrame)
        }
        TaskDetailPresenter(taskId, Injection.provideTaskRepository(applicationContext),taskDetailFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_TASK_ID = "TASK_ID"
    }
}