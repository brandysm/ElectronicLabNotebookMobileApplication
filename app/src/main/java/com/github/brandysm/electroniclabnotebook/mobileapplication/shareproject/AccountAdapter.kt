package com.github.brandysm.electroniclabnotebook.mobileapplication.shareproject

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.home.data.model.Account

class AccountAdapter(activity: FragmentActivity, shareProjectViewModel: ShareProjectViewModel) :
    RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    var otherAccountList: List<Account> = listOf<Account>()
    val fragmentActivity: FragmentActivity = activity
    val shareProjectViewModel = shareProjectViewModel

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountAdapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.project_layout, parent, false)
        return AccountAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AccountAdapter.ViewHolder, position: Int) {
        val item: Account = otherAccountList[position]
        holder.account.setOnClickListener {
            shareProjectViewModel.shareProjectWithAccount(
                fragmentActivity.intent.getLongExtra("EXTRA_PROJECT_ID", -1),
                item.id!!
            )
            fragmentActivity.setResult(Activity.RESULT_OK)
            fragmentActivity.finish()
        }
        holder.account.text = item.username
    }

    override fun getItemCount(): Int {
        return otherAccountList.size
    }

    fun setOtherAccounts(otherAccounts: List<Account>) {
        otherAccountList = otherAccounts
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val account: TextView

        init {
            account = itemView.findViewById(R.id.project_name)
        }
    }
}