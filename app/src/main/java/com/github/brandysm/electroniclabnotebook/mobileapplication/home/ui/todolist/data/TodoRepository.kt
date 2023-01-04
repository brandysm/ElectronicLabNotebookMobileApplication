package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data

import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.model.Todo

class TodoRepository(val dataSource: TodoDataSource) {

    var todoList: MutableList<Todo>? = null

    fun getTodos(): List<Todo> {
        val todos = dataSource.getTodos()
        todoList = todos
        return todos
    }

    fun addTodo(todo: Todo): Todo {
        val responseTodo = dataSource.addTodo(todo)
        todoList!!.add(responseTodo)
        return responseTodo
    }

    fun removeTodo(id: Long) {
        dataSource.removeTodo(id)
        var todoToRemove: Todo? = null
        for (todo in todoList!!) {
            if (todo.id == id)
                todoToRemove = todo
        }
        if (todoToRemove != null) {
            todoList!!.remove(todoToRemove)
        }
    }

    fun modifyTodo(id: Long, completed: Boolean) {
        dataSource.modifyTodo(id, Todo(null,null, completed))
        for (todo in todoList!!) {
            if (todo.id == id)
                todo.completed = completed
        }
    }
}