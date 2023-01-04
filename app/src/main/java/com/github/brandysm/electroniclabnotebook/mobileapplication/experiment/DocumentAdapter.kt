package com.github.brandysm.electroniclabnotebook.mobileapplication.experiment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.data.model.Document
import com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.todolist.TodoListViewModel
import com.github.brandysm.electroniclabnotebook.mobileapplication.project.data.model.Experiment

class DocumentAdapter(activity: FragmentActivity, experimentViewModel: ExperimentViewModel, private val projectId: Long, private val experimentId: Long) :
    RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {

    var documentList: List<Document> = listOf<Document>()
    val fragmentActivity: FragmentActivity = activity
    val experimentViewModel = experimentViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.document_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Document = documentList[position]
        holder.document.setOnClickListener {
            experimentViewModel.getDocument(projectId, experimentId, item.id!!)
        }
        holder.document.text = item.name
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    fun setDocuments(documents: List<Document>) {
        documentList = documents
        notifyDataSetChanged()
    }

    fun getDocument(position: Int): Document {
        return documentList[position]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val document: TextView

        init {
            document = itemView.findViewById(R.id.document_name)
        }
    }
}