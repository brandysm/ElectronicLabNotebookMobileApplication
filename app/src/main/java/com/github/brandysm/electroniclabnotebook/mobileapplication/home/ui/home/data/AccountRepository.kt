package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data

import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Account

class AccountRepository(val dataSource: AccountDataSource) {
    var accountList: MutableList<Account>? = null

    fun getAccounts(): List<Account> {
        val accounts = dataSource.getAccounts()
        accountList = accounts
        return accounts
    }
}