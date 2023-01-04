package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Project
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.model.Todo
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.ProjectActivity

class ProjectAdapter(activity: Fragment) :
    RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    var projectList: List<Project> = listOf<Project>()
    val fragmentActivity: Fragment = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.project_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Project = projectList[position]
        holder.project.setOnClickListener {
            val projectIntent: Intent = Intent(fragmentActivity.activity, ProjectActivity::class.java)
            projectIntent.putExtra("EXTRA_PROJECT_ID", item.id)
            projectIntent.putExtra("EXTRA_PROJECT_NAME", item.name)
            fragmentActivity.startActivity(projectIntent)
        }
        holder.project.text = item.name
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    fun setProjects(projects: List<Project>) {
        projectList = projects
        notifyDataSetChanged()
    }

    fun getProject(position: Int): Project {
        return projectList[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val project: TextView

        init {
            project = itemView.findViewById(R.id.project_name)
        }
    }
}