package com.github.brandysm.electroniclabnotebook.mobileapplication.shareproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Account
import com.github.brandysm.electroniclabnotebook.mobileapplication.shareproject.data.ShareProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShareProjectViewModel : ViewModel() {

    private lateinit var shareProjectRepository: ShareProjectRepository

    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _accounts

    fun getOtherAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            val otherAccountList: List<Account> = shareProjectRepository.getOtherAccounts()
            _accounts.postValue(otherAccountList.toMutableList())
        }
    }

    fun shareProjectWithAccount(projectId: Long, accountId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            shareProjectRepository.shareProjectWithAccount(projectId, accountId)
        }
    }

    fun setShareProjectRepository(shareProjectRepository: ShareProjectRepository) {
        this.shareProjectRepository = shareProjectRepository
    }
}