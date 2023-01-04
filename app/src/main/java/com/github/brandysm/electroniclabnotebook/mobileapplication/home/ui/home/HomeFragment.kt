package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.FragmentHomeBinding
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.AccountDataSource
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.AccountRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.ProjectDataSource
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.ProjectRepository

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var projectAdapter: ProjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.setProjectRepository(ProjectRepository(ProjectDataSource()))
        homeViewModel.setAccountRepository(AccountRepository(AccountDataSource()))

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val projectRecyclerView: RecyclerView = binding.recyclerviewProjectList
        projectRecyclerView.layoutManager = LinearLayoutManager(activity)
        projectAdapter = ProjectAdapter(this)
        projectRecyclerView.adapter = projectAdapter

        val itemTouchHelper = ItemTouchHelper(
            ProjectItemTouchHelper(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                projectAdapter
            ).setViewModel(homeViewModel)
        )
        itemTouchHelper.attachToRecyclerView(projectRecyclerView)

        homeViewModel.projects.observe(viewLifecycleOwner) {
            val projects = it
            projectAdapter.setProjects(projects)
        }
        binding.textName.text = getString(R.string.name_semicolon, "")
        binding.textEmail.text = getString(R.string.email_semicolon, "")
        binding.textOrganization.text = getString(R.string.organization_semicolon, "")
        homeViewModel.accounts.observe(viewLifecycleOwner) {
            val accounts = it
            binding.textName.text = getString(R.string.name_semicolon, accounts[0].username)
            binding.textEmail.text = getString(R.string.email_semicolon, accounts[0].email)
            binding.textOrganization.text = getString(
                R.string.organization_semicolon,
                accounts[0].organization?.name ?: ""
            )
        }
        homeViewModel.getProjects()
        homeViewModel.getAccounts()

        val fab = binding.floatingbuttonProjectList
        fab.setOnClickListener {
            AddNewProject().setViewModel(homeViewModel)
                .show(activity?.supportFragmentManager!!, "ActionBottomDialog")
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            projectAdapter.notifyDataSetChanged()
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this.activity, "Project shared successfully!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}