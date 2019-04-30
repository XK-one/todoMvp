package com.wyk.todomvpforgg.task.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.wyk.todomvpforgg.R
import com.wyk.todomvpforgg.task.contract.TasksContract
import com.wyk.todomvpforgg.task.data.model.Task
import com.wyk.todomvpforgg.task.data.model.TasksFilterType
import com.wyk.todomvpforgg.util.showSnackBar
import java.util.ArrayList


class TasksFragment: Fragment(), TasksContract.View {

    override fun showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared))
    }
    override fun showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete))
    }
    override fun showTaskMarkedActivefun() {
        showMessage(getString(R.string.task_marked_active))
    }

    override fun showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message))
    }
    override fun showTaskDetailsUi(taskId: String) {
        val intent = Intent(activity, TaskDetailActivity::class.java).apply {
            putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId)
        }
        startActivity(intent)
    }

    override fun showAddTask() {
        val intent = Intent(activity, AddEditTaskActivity::class.java)
        startActivityForResult(intent,AddEditTaskActivity.REQUEST_ADD_TASK)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear -> presenter.clearCompletedTasks()
            R.id.menu_filter -> showFilteringPopUpMenu()
            R.id.menu_refresh -> presenter.loadTasks(true)
        }
        return true
    }
    override fun showFilteringPopUpMenu() {
        val context = context?: return
        val activity = activity?: return
        PopupMenu(context,activity.findViewById(R.id.menu_filter)).apply {
            menuInflater.inflate(R.menu.filter_tasks, menu)
            setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.active -> presenter.currentFiltering = TasksFilterType.ACTIVE_TASKS
                    R.id.completed -> presenter.currentFiltering = TasksFilterType.COMPLETE_TASKS
                    else -> presenter.currentFiltering = TasksFilterType.ALL_TASKS
                }
                presenter.loadTasks(false)
                true
            }
            show()
        }
    }

    override fun showActiveFilterLabel() {
        filteringLabelView.text = resources.getString(R.string.label_active)
    }

    override fun showCompletedFilterLabel() {
        filteringLabelView.text = resources.getString(R.string.label_completed)
    }

    override fun showAllFilterLabel() {
        filteringLabelView.text = resources.getString(R.string.label_all)
    }

    private fun showNoTasksViews(mainText: String, iconRes: Int, showAddView: Boolean) {
        tasksView.visibility = View.GONE
        noTasksView.visibility = View.VISIBLE

        noTaskMainView.text = mainText
        noTaskIcon.setImageResource(iconRes)
        noTaskAddView.visibility = if(showAddView) View.VISIBLE else View.GONE
    }

    override fun showNoActiveTasks() {
        showNoTasksViews(resources.getString(R.string.no_tasks_active), R.drawable.ic_check_circle_24dp, false)
    }

    override fun showNoCompletedTasks() {
        showNoTasksViews(resources.getString(R.string.no_tasks_completed), R.drawable.ic_verified_user_24dp, false)
    }

    override fun showNoTasks() {
        showNoTasksViews(resources.getString(R.string.no_tasks_all), R.drawable.ic_assignment_turned_in_24dp, false)
    }

    override fun showTasks(tasks: List<Task>) {
        listAdapter.mTask = tasks
        tasksView.visibility = View.VISIBLE
        noTasksView.visibility = View.GONE
    }
    override fun showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error))
    }

    private fun showMessage(message: String) {
        view?.showSnackBar(message, Snackbar.LENGTH_LONG)
    }

    override var isActive: Boolean = false
        get() = isAdded

    override lateinit var presenter: TasksContract.Presenter

    private lateinit var noTasksView: View
    private lateinit var noTaskIcon: ImageView
    private lateinit var noTaskMainView: TextView
    private lateinit var noTaskAddView: TextView
    private lateinit var tasksView: LinearLayout
    private lateinit var filteringLabelView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.tasks_frag, container, false)
        with(root){
            val listView = findViewById<ListView>(R.id.tasks_list).apply { adapter = listAdapter }

            // Set up progress indicator
            findViewById<ScrollChildSwipeRefreshLayout>(R.id.refresh_layout).apply {
                setColorSchemeColors(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                        ContextCompat.getColor(requireContext(), R.color.colorAccent),
                        ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
                )
                // Set the scrolling view in the custom SwipeRefreshLayout.
                scrollUpChild = listView
                setOnRefreshListener { presenter.loadTasks(false) }
            }

            filteringLabelView = findViewById(R.id.filteringLabel)
            tasksView = findViewById(R.id.tasksLL)

            // Set up  no tasks view
            noTasksView = findViewById(R.id.noTasks)
            noTaskIcon = findViewById(R.id.noTasksIcon)
            noTaskMainView = findViewById(R.id.noTasksMain)
            noTaskAddView = (findViewById<TextView>(R.id.noTasksAdd)).also {
                it.setOnClickListener {
                    showAddTask()
                }
            }
        }
        requireActivity().findViewById<FloatingActionButton>(R.id.fab_add_task).apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener { presenter.addNewTask() }
        }
        setHasOptionsMenu(true)//true则显示顶部菜单
        return root
    }


    override fun setLoadingIndicator(active: Boolean) {
        val root = view ?: return
        with(root.findViewById<SwipeRefreshLayout>(R.id.refresh_layout)){
            post {
                isRefreshing = active
            }
        }
    }

    companion object {
        fun newInstance() = TasksFragment()
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    internal var mItemListener: TaskItemListener = object: TaskItemListener{
        override fun onTaskClick(clickTask: Task) {
            presenter.openTaskDetails(clickTask)
        }
        override fun onCompleteTaskClick(completeTask: Task) {
            presenter.completeTask(completeTask)
        }
        override fun onActivateTaskClick(activateTask: Task) {
            presenter.activateTask(activateTask)
        }
    }
    private val listAdapter = TasksAdapter(ArrayList(0), mItemListener)

    private class TasksAdapter(tasks: List<Task>, private val itemListener: TaskItemListener): BaseAdapter(){

        var mTask: List<Task> = tasks
        set(tasks) {
            field = tasks
            notifyDataSetChanged()
        }
        override fun getCount() = mTask.size
        override fun getItem(position: Int) = mTask[position]
        override fun getItemId(position: Int) = position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val task = getItem(position)
            val rowView = convertView?: LayoutInflater.from(parent?.context).
                    inflate(R.layout.task_item, parent, false)
            with(rowView){
                findViewById<TextView>(R.id.title).apply {
                    text = task.titleForList
                }
                findViewById<CheckBox>(R.id.complete).apply {
                    isChecked = task.isCompleted
                    val rowViewBackground = if(task.isCompleted)R.drawable.list_completed_touch_feedback else R.drawable.touch_feedback
                    rowView.setBackgroundResource(rowViewBackground)
                    setOnClickListener{
                        if(task.isCompleted){
                            itemListener.onActivateTaskClick(task)
                        }else{
                            itemListener.onCompleteTaskClick(task)
                        }
                    }
                }
                setOnClickListener { itemListener.onTaskClick(task) }
            }
            return rowView
        }
    }

    interface TaskItemListener{
        fun onTaskClick(clickTask: Task)
        fun onCompleteTaskClick(completeTask: Task)
        fun onActivateTaskClick(activateTask: Task)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.result(requestCode, resultCode)
    }


}