package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Project
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.data.model.Todo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewProject : BottomSheetDialogFragment() {

    lateinit var homeViewModel: HomeViewModel

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
        val addProjectText = getView()?.findViewById<EditText>(R.id.addElementText)
        val addProjectButton = getView()?.findViewById<Button>(R.id.addElementButton)

        addProjectButton?.setOnClickListener {
            homeViewModel.addProject(Project(null, addProjectText?.text.toString(), null, null))
            dismiss()
        }
    }

    fun setViewModel(homeViewModel: HomeViewModel): AddNewProject {
        this.homeViewModel = homeViewModel
        return this
    }
}