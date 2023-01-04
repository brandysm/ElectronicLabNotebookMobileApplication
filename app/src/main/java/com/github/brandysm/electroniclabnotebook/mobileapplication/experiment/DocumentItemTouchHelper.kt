package com.github.brandysm.electroniclabnotebook.mobileapplication.experiment

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class DocumentItemTouchHelper(dragDirs: Int, swipeDirs: Int, adapter: DocumentAdapter) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    val documentAdapter: DocumentAdapter = adapter
    lateinit var experimentViewModel: ExperimentViewModel
    var projectId: Long = -1
    var experimentId: Long = -1

    fun setViewModel(experimentViewModel: ExperimentViewModel, projectId: Long, experimentId: Long): DocumentItemTouchHelper {
        this.experimentViewModel = experimentViewModel
        this.projectId = projectId
        this.experimentId = experimentId
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
            val builder = AlertDialog.Builder(documentAdapter.fragmentActivity)
            builder.setTitle("Delete Document");
            builder.setMessage("Are you sure you want to delete this Document?");
            builder.setPositiveButton("Confirm") { _, which ->
                experimentViewModel.removeDocument(projectId, experimentId, documentAdapter.getDocument(position).id!!)
            }
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> documentAdapter.notifyDataSetChanged() })
            builder.setCancelable(false)
            builder.create().show()
        }
    }
}