package com.github.brandysm.electroniclabnotebook.mobileapplication.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.ProjectRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Account
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Project
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.ExperimentRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.model.Experiment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProjectViewModel : ViewModel() {

    private lateinit var experimentRepository: ExperimentRepository

    private val _experiments = MutableLiveData<List<Experiment>>()
    val experiments: LiveData<List<Experiment>> = _experiments

    fun getExperiments(projectId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val experimentList: List<Experiment> = experimentRepository.getExperiments(projectId)
            _experiments.postValue(experimentList.toMutableList())
        }
    }

    fun addExperiment(projectId: Long, experiment: Experiment) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseExperiment: Experiment = experimentRepository.addExperiment(projectId, experiment)
            val experimentList: MutableList<Experiment> = _experiments.value!!.toMutableList()
            experimentList.add(Experiment(responseExperiment.id, responseExperiment.name, responseExperiment.creationDate))
            _experiments.postValue(experimentList)
        }
    }

    fun removeExperiment(projectId: Long, experimentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            experimentRepository.removeExperiment(projectId, experimentId)
            val experimentList: MutableList<Experiment> = _experiments.value!!.toMutableList()
            var experimentToRemove: Experiment? = null
            for (experiment in experimentList) {
                if (experiment.id == experimentId)
                    experimentToRemove = experiment
            }
            if (experimentToRemove != null) {
                experimentList.remove(experimentToRemove)
            }
            _experiments.postValue(experimentList)
        }
    }

    fun modifyExperiment(projectId: Long, experimentId: Long, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            experimentRepository.modifyExperiment(projectId, experimentId, name)
            val experimentList: MutableList<Experiment> = _experiments.value!!.toMutableList()
            for (experiment in experimentList) {
                if (experiment.id == experimentId)
                    experiment.name = name
            }
            _experiments.postValue(experimentList)
        }
    }

    fun setExperimentRepository(experimentRepository: ExperimentRepository) {
        this.experimentRepository = experimentRepository
    }
}