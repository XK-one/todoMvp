package com.wyk.todomvpforgg.statistics.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.wyk.todomvpforgg.Injection
import com.wyk.todomvpforgg.R
import com.wyk.todomvpforgg.statistics.presenter.StatisticsPresenter
import com.wyk.todomvpforgg.util.replaceFragmentInActivity
import com.wyk.todomvpforgg.util.setupActionBar

class StatisticsActivity: AppCompatActivity() {

    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mStatisticsView: StatisticsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics_act)

        setupActionBar(R.id.toolbar){
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        mDrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout).apply {
            setStatusBarBackground(R.color.colorPrimaryDark)
        }
        setupDrawerContent(findViewById(R.id.nav_view))

        mStatisticsView = supportFragmentManager.findFragmentById(R.id.contentFrame) as StatisticsFragment?
                ?: StatisticsFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        StatisticsPresenter(Injection.provideTaskRepository(applicationContext), mStatisticsView)
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
            if(it.itemId == R.id.list_navigation_menu_item){
                NavUtils.navigateUpFromSameTask(this@StatisticsActivity)
            }
            it.isChecked = true
            mDrawerLayout.closeDrawers()
            true
        }
    }
}