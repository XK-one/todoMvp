package com.wyk.todomvpforgg.task.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.wyk.todomvpforgg.R
import com.wyk.todomvpforgg.task.contract.TaskDetailContract
import com.wyk.todomvpforgg.util.showSnackBar

class TaskDetailFragment: Fragment(), TaskDetailContract.View {

    override fun showTaskMarkedComplete() {
        view?.showSnackBar(getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG)
    }
    override fun showTaskMarkedActive() {
        view?.showSnackBar(getString(R.string.task_marked_active), Snackbar.LENGTH_LONG)
    }

    override fun showCompletionStatus(complete: Boolean) {
        detailCompleteStatus?.run {
            isChecked = complete
            setOnCheckedChangeListener{buttonView, isChecked ->
                if(isChecked){
                    presenter.completeTask()
                }else{
                    presenter.activateTask()
                }
            }
        }
    }

    override fun setLoadingIndicator(active: Boolean) {
        if(active){
            detailTitle.text = ""
            detailDescription.text = getString(R.string.loading)
        }
    }

    override fun hideTitle() {
        detailTitle.visibility = View.GONE
    }

    override fun hideDescription() {
        detailDescription.visibility = View.GONE
    }

    override fun showTitle(title: String) {
        with(detailTitle){
            visibility = View.VISIBLE
            text = title
        }

    }

    override fun showDescription(description: String) {
        with(detailDescription){
            visibility = View.VISIBLE
            text = description
        }
    }

    override fun showTaskDeleted() {
        activity?.finish()
    }

    override fun showMissingTask() {
        detailTitle.text = ""
        detailDescription.text = getString(R.string.no_data)
    }

    override fun showEditTask(taskId: String) {
        val intent = Intent(activity,AddEditTaskActivity::class.java).apply {
            putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
        }
        startActivityForResult(intent, REQUEST_EDIT_TASK)
    }

    private lateinit var detailTitle: TextView
    private lateinit var detailDescription: TextView
    private lateinit var detailCompleteStatus: CheckBox
    override var isActive: Boolean = false
        get() = isAdded

    lateinit override var presenter: TaskDetailContract.Presenter

    companion object {

        private val REQUEST_EDIT_TASK = 1
        private val ARGUMENT_TASK_ID = "TASK_ID"
        var taskDetailFragment: TaskDetailFragment? = null
        val lock = Any()
        fun newIntance(taskId: String) = taskDetailFragment?: synchronized(lock){
            taskDetailFragment?: TaskDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT_TASK_ID, taskId)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.taskdetail_frag, container, false).apply {
            detailTitle = findViewById(R.id.task_detail_title)
            detailDescription = findViewById(R.id.task_detail_description)
            detailCompleteStatus = findViewById(R.id.task_detail_complete)
        }
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_task)?.apply {
            setOnClickListener {
                presenter.editTask()
            }
        }
        setHasOptionsMenu(true)//true则显示顶部菜单
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.taskdetail_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val isDelete = item?.itemId == R.id.menu_delete
        if(isDelete) presenter.deleteTask()
        return isDelete
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_EDIT_TASK && resultCode == Activity.RESULT_OK){
            activity?.finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}