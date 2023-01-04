package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.FragmentTodolistBinding
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.TodoDataSource
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.TodoRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.model.Todo

class TodoListFragment : Fragment() {

    private var _binding: FragmentTodolistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val todoListViewModel =
            ViewModelProvider(this).get(TodoListViewModel::class.java)
        todoListViewModel.setTodoRepository(TodoRepository(TodoDataSource()))

        _binding = FragmentTodolistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val taskRecyclerView : RecyclerView = binding.recyclerviewTodoList
        taskRecyclerView.layoutManager = LinearLayoutManager(activity)
        todoAdapter = TodoAdapter(requireActivity(), todoListViewModel)
        taskRecyclerView.adapter = todoAdapter

        val itemTouchHelper = ItemTouchHelper(TodoItemTouchHelper(0, ItemTouchHelper.LEFT, todoAdapter).setViewModel(todoListViewModel))
        itemTouchHelper.attachToRecyclerView(taskRecyclerView)

        todoListViewModel.todos.observe(viewLifecycleOwner, Observer {
            val todos = it
            val newList = ArrayList<Todo>()
            for (todo in todos) {
                newList.add(Todo(todo.id,todo.title,todo.completed))
            }
            todoAdapter.setTodos(newList)
        })
        todoListViewModel.getTodos()

        val fab = binding.floatingbuttonTodoList
        fab.setOnClickListener(View.OnClickListener {
            AddNewTodo().setViewModel(todoListViewModel).show(activity?.supportFragmentManager!!, "ActionBottomDialog")
        })


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}