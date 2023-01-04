package com.github.brandysm.electroniclabnotebook.mobileapplication.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.ActivityProjectBinding
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.ExperimentDataSource
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.ExperimentRepository

class ProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectBinding

    lateinit var experimentAdapter: ExperimentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val projectViewModel =
            ViewModelProvider(this).get(ProjectViewModel::class.java)
        projectViewModel.setExperimentRepository(ExperimentRepository(ExperimentDataSource()))

        binding = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val experimentRecyclerView: RecyclerView = binding.recyclerviewExperimentList
        experimentRecyclerView.layoutManager = LinearLayoutManager(this)
        experimentAdapter = ExperimentAdapter(this, intent.getLongExtra("EXTRA_PROJECT_ID", -1))
        experimentRecyclerView.adapter = experimentAdapter

        val itemTouchHelper = ItemTouchHelper(
            ExperimentItemTouchHelper(
                0,
                ItemTouchHelper.LEFT,
                experimentAdapter
            ).setViewModel(projectViewModel, intent.getLongExtra("EXTRA_PROJECT_ID", -1))
        )
        itemTouchHelper.attachToRecyclerView(experimentRecyclerView)

        binding.textProject.text =
            getString(R.string.project_semicolon, intent.getStringExtra("EXTRA_PROJECT_NAME"))
        projectViewModel.experiments.observe(this@ProjectActivity) {
            val experiments = it
            experimentAdapter.setExperiments(experiments)
        }
        projectViewModel.getExperiments(intent.getLongExtra("EXTRA_PROJECT_ID", -1))

        val fab = binding.floatingbuttonExperimentList
        fab.setOnClickListener {
            AddNewExperiment().setViewModel(
                projectViewModel,
                intent.getLongExtra("EXTRA_PROJECT_ID", -1)
            )
                .show(supportFragmentManager, "ActionBottomDialog")
        }
    }
}