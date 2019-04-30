package com.wyk.todomvpforgg.task.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.wyk.todomvpforgg.R
import com.wyk.todomvpforgg.task.contract.AddEditTaskContract
import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.util.showSnackBar


class AddEditTaskFragment: Fragment(),AddEditTaskContract.View {

    override fun setEditTask(task: Task) {
        currTask = task
    }

    override fun setTitle(titleContxt: String) {
        title.text = titleContxt
    }
    override fun setDescription(descriptionContxt: String) {
        description.text = descriptionContxt
    }

    override fun showTasksList() {
        activity?.apply {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun showEmptyTaskError() {
        title.showSnackBar(getString(R.string.empty_task_message), Snackbar.LENGTH_LONG)
    }

    lateinit var title: TextView
    lateinit var description: TextView

    override var isActive: Boolean = false
        get() = isAdded

    lateinit override var presenter: AddEditTaskContract.Presenter

    var currTask: Task? = null

    companion object {
        const val ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID"

        fun newIntance(taskId: String?) =
                AddEditTaskFragment().apply {
                    arguments = Bundle().apply{
                        putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
                    }
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.addtask_frag, container, false)
        with(root){
            title = findViewById<TextView>(R.id.add_task_title)
            description = findViewById<TextView>(R.id.add_task_description)
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_task_done)?.apply {
            setImageResource(R.drawable.ic_done)
            setOnClickListener {
                /**编辑的情况下，会从已完成状态切换到未完成状态*/
                presenter.saveTask(title.text.toString(), description.text.toString())
            }
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

}