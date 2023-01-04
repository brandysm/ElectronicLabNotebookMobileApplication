package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.ProjectActivity
import com.github.brandysm.electroniclabnotebook.mobileapplication.shareproject.ShareProjectActivity


class ProjectItemTouchHelper(dragDirs: Int, swipeDirs: Int, adapter: ProjectAdapter) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    val projectAdapter: ProjectAdapter = adapter
    lateinit var homeViewModel: HomeViewModel

    fun setViewModel(homeViewModel: HomeViewModel): ProjectItemTouchHelper {
        this.homeViewModel = homeViewModel
        return this
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            val builder = AlertDialog.Builder(projectAdapter.fragmentActivity.context)
            builder.setTitle("Delete Project");
            builder.setMessage("Are you sure you want to delete this Project?");
            builder.setPositiveButton("Confirm") { _, which ->
                homeViewModel.removeProject(projectAdapter.getProject(position).id!!)
            }
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> projectAdapter.notifyDataSetChanged() })
            builder.setCancelable(false)
            builder.create().show()
        } else if (direction == ItemTouchHelper.RIGHT) {
            val otherAccountIntent: Intent = Intent(this.projectAdapter.fragmentActivity.context, ShareProjectActivity::class.java)
            otherAccountIntent.putExtra("EXTRA_PROJECT_ID", projectAdapter.getProject(position).id!!)
            otherAccountIntent.putExtra("EXTRA_PROJECT_NAME", projectAdapter.getProject(position).name)
            this.projectAdapter.fragmentActivity.startActivityForResult(otherAccountIntent, 1)
        }
    }
}