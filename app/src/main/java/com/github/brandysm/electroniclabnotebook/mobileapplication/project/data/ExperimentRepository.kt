package com.github.brandysm.electroniclabnotebook.mobileapplication.project.data

import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.ProjectDataSource
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.model.Experiment

class ExperimentRepository(val dataSource: ExperimentDataSource) {
    var experimentList: MutableList<Experiment>? = null

    fun getExperiments(projectId: Long): List<Experiment> {
        val experiments = dataSource.getExperiments(projectId)
        experimentList = experiments
        return experiments
    }

    fun addExperiment(projectId: Long, experiment: Experiment): Experiment {
        val responseExperiment = dataSource.addExperiment(projectId, experiment)
        experimentList!!.add(responseExperiment)
        return responseExperiment
    }

    fun removeExperiment(projectId: Long, experimentId: Long) {
        dataSource.removeExperiment(projectId, experimentId)
        var experimentToRemove: Experiment? = null
        for (experiment in experimentList!!) {
            if (experiment.id == experimentId)
                experimentToRemove = experiment
        }
        if (experimentToRemove != null) {
            experimentList!!.remove(experimentToRemove)
        }
    }

    fun modifyExperiment(projectId: Long, experimentId: Long, name: String) {
        val responseExperiment = dataSource.modifyExperiment(projectId, experimentId, Experiment(null, name, null))
        for (experiment in experimentList!!) {
            if (experiment.id == experimentId)
                experiment.name = name
        }
    }
}