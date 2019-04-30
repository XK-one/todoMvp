package com.wyk.todomvpforgg.task.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wyk.todomvpforgg.Injection
import com.wyk.todomvpforgg.R
import com.wyk.todomvpforgg.task.presenter.AddEditTaskPresenter
import com.wyk.todomvpforgg.util.replaceFragmentInActivity
import com.wyk.todomvpforgg.util.setupActionBar

class AddEditTaskActivity: AppCompatActivity() {

    lateinit var addEditPresenter: AddEditTaskPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask_act)
        val taskId = intent.getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)

        setupActionBar(R.id.toolbar){
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(if(taskId == null) R.string.add_task else R.string.edit_task)
        }
        val addEditTaskFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as AddEditTaskFragment?
                                                        ?:AddEditTaskFragment.newIntance(taskId).also {
                                                            replaceFragmentInActivity(it, R.id.contentFrame)
                                                        }
        val shouldLoadDataFromRepo = savedInstanceState?.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY)?: true

        addEditPresenter = AddEditTaskPresenter(taskId,Injection.provideTaskRepository(applicationContext),
                addEditTaskFragment, shouldLoadDataFromRepo)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, false)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY"
        const val REQUEST_ADD_TASK = 1
    }
}