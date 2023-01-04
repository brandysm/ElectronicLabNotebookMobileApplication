package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data;

import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Project

class ProjectRepository(val dataSource: ProjectDataSource) {
    var projectList: MutableList<Project>? = null

    fun getProjects(): List<Project> {
        val projects = dataSource.getProjects()
        projectList = projects
        return projects
    }

    fun addProject(project: Project): Project {
        val responseProject = dataSource.addProject(project)
        projectList!!.add(responseProject)
        return responseProject
    }

    fun removeProject(id: Long) {
        dataSource.removeProject(id)
        var projectToRemove: Project? = null
        for (project in projectList!!) {
            if (project.id == id)
                projectToRemove = project
        }
        if (projectToRemove != null) {
            projectList!!.remove(projectToRemove)
        }
    }

    fun modifyProject(id: Long, name: String) {
        val responseProject = dataSource.modifyProject(id, Project(null, name, null, null))
        for (project in projectList!!) {
            if (project.id == id)
                project.name = name
        }
    }
}
