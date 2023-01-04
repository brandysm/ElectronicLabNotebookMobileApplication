package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.TodoRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.model.Todo
import com.github.brandysm.electroniclabnotebook.mobileapplication.login.data.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {

    private lateinit var todoRepository: TodoRepository

    private val _todos = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>> = _todos

    fun getTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoList: List<Todo> = todoRepository.getTodos()
            _todos.postValue(todoList.toMutableList())
        }
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseTodo = todoRepository.addTodo(todo)
            val todoList: MutableList<Todo> = _todos.value!!.toMutableList()
            todoList.add(Todo(responseTodo.id, responseTodo.title, responseTodo.completed))
            _todos.postValue(todoList)
        }
    }

    fun removeTodo(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.removeTodo(id)
            val todoList: MutableList<Todo> = _todos.value!!.toMutableList()
            var todoToRemove: Todo? = null
            for (todo in todoList) {
                if (todo.id == id)
                    todoToRemove = todo
            }
            if (todoToRemove != null) {
                todoList.remove(todoToRemove)
            }
            _todos.postValue(todoList)
        }
    }

    fun modifyTodo(id: Long, completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.modifyTodo(id, completed)
            val todoList: MutableList<Todo> = _todos.value!!.toMutableList()
            for (todo in todoList) {
                if (todo.id == id)
                    todo.completed = completed
            }
            _todos.postValue(todoList)
        }
    }

    fun setTodoRepository(todoRepository: TodoRepository) {
        this.todoRepository = todoRepository
    }
}