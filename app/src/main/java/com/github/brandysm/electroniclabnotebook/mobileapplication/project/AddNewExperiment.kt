package com.github.brandysm.electroniclabnotebook.mobileapplication.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.model.Experiment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewExperiment : BottomSheetDialogFragment() {

    lateinit var projectViewModel: ProjectViewModel
    var projectId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_element_layout, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addExperimentText = getView()?.findViewById<EditText>(R.id.addElementText)
        val addExperimentButton = getView()?.findViewById<Button>(R.id.addElementButton)

        addExperimentButton?.setOnClickListener {
            projectViewModel.addExperiment(
                projectId,
                Experiment(null, addExperimentText?.text.toString(), null)
            )
            dismiss()
        }
    }

    fun setViewModel(projectViewModel: ProjectViewModel, projectId: Long): AddNewExperiment {
        this.projectViewModel = projectViewModel
        this.projectId = projectId
        return this
    }
}