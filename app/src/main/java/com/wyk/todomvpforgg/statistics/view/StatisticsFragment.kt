package com.wyk.todomvpforgg.statistics.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wyk.todomvpforgg.R
import com.wyk.todomvpforgg.statistics.contract.StatisticsContract

class StatisticsFragment: Fragment(),StatisticsContract.View {

    override lateinit var presenter: StatisticsContract.Presenter
    lateinit var mTvStatistics: TextView

    override val isActive: Boolean
        get() = isAdded

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.statistics_frag, container, false).apply{
            mTvStatistics = findViewById<TextView>(R.id.statistics)
        }
        return root
    }

    override fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompletedTasks: Int) {
        if(numberOfIncompleteTasks == 0 && numberOfCompletedTasks == 0){
            mTvStatistics.text = getString(R.string.statistics_no_tasks)
        }else{
            val content = "${resources.getString(R.string.statistics_active_tasks)}" +
                    "$numberOfIncompleteTasks\n," +
                    "${getString(R.string.statistics_completed_tasks)}" +
                    "$numberOfCompletedTasks"
            mTvStatistics.text = content
        }
    }

    override fun showLoadingStatisticsError() {
        mTvStatistics.text = resources.getString(R.string.statistics_error)
    }

    override fun setProgressIndicator(active: Boolean) {
        if(active){
            mTvStatistics.text = getString(R.string.loading)
        }else{
            mTvStatistics.text = ""
        }
    }

     companion object {
         fun newInstance() = StatisticsFragment()
     }


}