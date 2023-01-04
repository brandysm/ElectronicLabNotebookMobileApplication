package com.github.brandysm.electroniclabnotebook.mobileapplication.shareproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.ActivityProjectBinding
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.ActivityShareProjectBinding
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.ExperimentAdapter
import com.github.brandysm.electroniclabnotebook.mobileapplication.shareproject.data.ShareProjectDataSource
import com.github.brandysm.electroniclabnotebook.mobileapplication.shareproject.data.ShareProjectRepository

class ShareProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareProjectBinding
    lateinit var accountAdapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shareProjectViewModel =
            ViewModelProvider(this).get(ShareProjectViewModel::class.java)
        shareProjectViewModel.setShareProjectRepository(ShareProjectRepository(ShareProjectDataSource()))

        binding = ActivityShareProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accountRecyclerView: RecyclerView = binding.recyclerviewAccountList
        accountRecyclerView.layoutManager = LinearLayoutManager(this)
        accountAdapter = AccountAdapter(this, shareProjectViewModel)
        accountRecyclerView.adapter = accountAdapter

        binding.textAddUser.text =
            getString(R.string.add_user_to_project_semicolon, intent.getStringExtra("EXTRA_PROJECT_NAME"))
        shareProjectViewModel.accounts.observe(this@ShareProjectActivity) {
            val accounts = it
            accountAdapter.setOtherAccounts(accounts)
        }
        shareProjectViewModel.getOtherAccounts()
    }
}