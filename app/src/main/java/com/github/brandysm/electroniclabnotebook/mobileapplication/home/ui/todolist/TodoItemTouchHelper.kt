package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class TodoItemTouchHelper(dragDirs: Int, swipeDirs: Int, adapter: TodoAdapter) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    val todoAdapter: TodoAdapter = adapter
    lateinit var todoListViewModel: TodoListViewModel

    fun setViewModel(todoListViewModel: TodoListViewModel): TodoItemTouchHelper {
        this.todoListViewModel = todoListViewModel
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
            val builder = AlertDialog.Builder(todoAdapter.fragmentActivity)
            builder.setTitle("Delete Todo");
            builder.setMessage("Are you sure you want to delete this Task?");
            builder.setPositiveButton("Confirm") { _, which ->
                todoListViewModel.removeTodo(todoAdapter.getTodo(position).id!!)
            }
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> todoAdapter.notifyDataSetChanged() })
            builder.setCancelable(false)
            builder.create().show()
        }
    }
}