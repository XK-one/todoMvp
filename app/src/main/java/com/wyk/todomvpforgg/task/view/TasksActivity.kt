package com.wyk.todomvpforgg.task.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.wyk.test_handler.HandlerActivity
import com.wyk.todomvpforgg.Injection
import com.wyk.todomvpforgg.R
import com.wyk.todomvpforgg.task.data.model.TasksFilterType
import com.wyk.todomvpforgg.task.presenter.TasksPresenter
import com.wyk.todomvpforgg.util.replaceFragmentInActivity
import com.wyk.todomvpforgg.util.setupActionBar

class TasksActivity : AppCompatActivity() {

    private val CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY"

    private lateinit var mPresenter: TasksPresenter
    private lateinit var mView: TasksFragment
    private lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_act)

        setupActionBar(R.id.toolbar){
            this.setHomeAsUpIndicator(R.drawable.ic_menu)
            this.setDisplayHomeAsUpEnabled(true)
        }

        mDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout).apply {
           setStatusBarBackground(R.color.colorPrimaryDark)
        }

        mView = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as TasksFragment? ?: TasksFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        mPresenter = TasksPresenter(Injection.provideTaskRepository(applicationContext),
                mView).apply {
              if(savedInstanceState != null)
              savedInstanceState.getSerializable(CURRENT_FILTERING_KEY) as TasksFilterType
        }
        setupDrawerContent(findViewById(R.id.nav_view))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putSerializable(CURRENT_FILTERING_KEY, mPresenter.currentFiltering)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            if(it.itemId == R.id.statistics_navigation_menu_item){
                //val intent = Intent(this@TasksActivity, StatisticsActivity::class.java)
                //startActivity(intent)
                val intent = Intent(this@TasksActivity, HandlerActivity::class.java)
                startActivity(intent)
            }
            it.isChecked = true
            mDrawerLayout.closeDrawers()
            true
        }
    }

}
