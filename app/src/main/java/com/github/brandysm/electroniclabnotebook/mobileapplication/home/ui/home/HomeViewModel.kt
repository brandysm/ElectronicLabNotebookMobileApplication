package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.AccountRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.ProjectRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Account
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Project
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.TodoRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var accountRepository: AccountRepository

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> = _projects

    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _accounts

    fun getAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            val accountList: List<Account> = accountRepository.getAccounts()
            _accounts.postValue(accountList.toMutableList())
        }
    }

    fun getProjects() {
        viewModelScope.launch(Dispatchers.IO) {
            val projectList: List<Project> = projectRepository.getProjects()
            _projects.postValue(projectList.toMutableList())
        }
    }

    fun addProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseProject: Project = projectRepository.addProject(project)
            val projectList: MutableList<Project> = _projects.value!!.toMutableList()
            projectList.add(Project(responseProject.id, responseProject.name, responseProject.organizationId, responseProject.teamId))
            _projects.postValue(projectList)
        }
    }

    fun removeProject(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.removeProject(id)
            val projectList: MutableList<Project> = _projects.value!!.toMutableList()
            var projectToRemove: Project? = null
            for (project in projectList) {
                if (project.id == id)
                    projectToRemove = project
            }
            if (projectToRemove != null) {
                projectList.remove(projectToRemove)
            }
            _projects.postValue(projectList)
        }
    }

    fun modifyProject(id: Long, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.modifyProject(id, name)
            val projectList: MutableList<Project> = _projects.value!!.toMutableList()
            for (project in projectList) {
                if (project.id == id)
                    project.name = name
            }
            _projects.postValue(projectList)
        }
    }

    fun setProjectRepository(projectRepository: ProjectRepository) {
        this.projectRepository = projectRepository
    }

    fun setAccountRepository(accountRepository: AccountRepository) {
        this.accountRepository = accountRepository
    }
}