package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.model.Todo

class TodoAdapter(activity: FragmentActivity, todoListViewModel: TodoListViewModel) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    var todoList: List<Todo> = listOf<Todo>()
    val fragmentActivity: FragmentActivity = activity
    val todoListViewModel = todoListViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Todo = todoList[position]
        holder.todo.setOnClickListener {
            todoListViewModel.modifyTodo(item.id!!, (it as CompoundButton).isChecked)
        }
        holder.todo.text = item.title
        holder.todo.isChecked = item.completed
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun setTodos(todos: List<Todo>) {
        todoList = todos
        notifyDataSetChanged()
    }

    fun getTodo(position: Int): Todo {
        return todoList[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todo: CheckBox

        init {
            todo = itemView.findViewById(R.id.todoCheckBox)
        }
    }
}