package com.github.brandysm.electroniclabnotebook.mobileapplication.shareproject.data

import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Account

class ShareProjectRepository(val dataSource: ShareProjectDataSource) {
    var otherAccountList: MutableList<Account>? = null

    fun getOtherAccounts(): List<Account> {
        val otherAccounts = dataSource.getOtherAccounts()
        otherAccountList = otherAccounts
        return otherAccounts
    }

    fun shareProjectWithAccount(projectId: Long, accountId: Long) {
        dataSource.shareProjectWithAccount(projectId, accountId)
    }
}