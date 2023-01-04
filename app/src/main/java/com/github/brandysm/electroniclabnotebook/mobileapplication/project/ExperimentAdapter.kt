package com.github.brandysm.electroniclabnotebook.mobileapplication.project

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.ExperimentActivity
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Project
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.model.Experiment

class ExperimentAdapter(activity: FragmentActivity, private val projectId: Long) :
    RecyclerView.Adapter<ExperimentAdapter.ViewHolder>() {

    var experimentList: List<Experiment> = listOf<Experiment>()
    val fragmentActivity: FragmentActivity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.project_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Experiment = experimentList[position]
        holder.experiment.setOnClickListener {
            val experimentIntent: Intent = Intent(fragmentActivity, ExperimentActivity::class.java)
            experimentIntent.putExtra("EXTRA_PROJECT_ID", projectId)
            experimentIntent.putExtra("EXTRA_EXPERIMENT_ID", item.id)
            experimentIntent.putExtra("EXTRA_EXPERIMENT_NAME", item.name)
            fragmentActivity.startActivity(experimentIntent)
        }
        holder.experiment.text = item.name
    }

    override fun getItemCount(): Int {
        return experimentList.size
    }

    fun setExperiments(experiments: List<Experiment>) {
        experimentList = experiments
        notifyDataSetChanged()
    }

    fun getExperiment(position: Int): Experiment {
        return experimentList[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val experiment: TextView

        init {
            experiment = itemView.findViewById(R.id.project_name)
        }
    }
}