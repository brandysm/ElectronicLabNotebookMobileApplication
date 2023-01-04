package com.github.brandysm.electroniclabnotebook.mobileapplication.project

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class ExperimentItemTouchHelper(dragDirs: Int, swipeDirs: Int, adapter: ExperimentAdapter) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    val experimentAdapter: ExperimentAdapter = adapter
    lateinit var projectViewModel: ProjectViewModel
    var projectId: Long = -1

    fun setViewModel(projectViewModel: ProjectViewModel, projectId: Long): ExperimentItemTouchHelper {
        this.projectViewModel = projectViewModel
        this.projectId = projectId
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
            val builder = AlertDialog.Builder(experimentAdapter.fragmentActivity)
            builder.setTitle("Delete Experiment");
            builder.setMessage("Are you sure you want to delete this Experiment?");
            builder.setPositiveButton("Confirm") { _, which ->
                projectViewModel.removeExperiment(projectId, experimentAdapter.getExperiment(position).id!!)
            }
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> experimentAdapter.notifyDataSetChanged() })
            builder.setCancelable(false)
            builder.create().show()
        }
    }
}