package com.github.brandysm.electroniclabnotebook.mobileapplication.experiment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.data.DocumentRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.data.model.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExperimentViewModel : ViewModel() {

    private lateinit var documentRepository: DocumentRepository

    private val _downloadedDocument = MutableLiveData<Document>()
    val downloadedDocument: LiveData<Document> = _downloadedDocument

    private val _documents = MutableLiveData<List<Document>>()
    val documents: LiveData<List<Document>> = _documents

    fun getDocuments(projectId: Long, experimentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val documentList: List<Document> =
                documentRepository.getDocuments(projectId, experimentId)
            _documents.postValue(documentList.toMutableList())
        }
    }

    fun addDocument(projectId: Long, experimentId: Long, document: Document) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseDocument: Document =
                documentRepository.addDocument(projectId, experimentId, document)
            val documentList: MutableList<Document> = _documents.value!!.toMutableList()
            documentList.add(
                Document(
                    responseDocument.id,
                    responseDocument.name,
                    responseDocument.creationDate,
                    responseDocument.data
                )
            )
            _documents.postValue(documentList)
        }
    }

    fun removeDocument(projectId: Long, experimentId: Long, documentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            documentRepository.removeDocument(projectId, experimentId, documentId)
            val documentList: MutableList<Document> = _documents.value!!.toMutableList()
            var documentToRemove: Document? = null
            for (document in documentList) {
                if (document.id == documentId)
                    documentToRemove = document
            }
            if (documentToRemove != null) {
                documentList.remove(documentToRemove)
            }
            _documents.postValue(documentList)
        }
    }

    fun getDocument(
        projectId: Long,
        experimentId: Long,
        documentId: Long
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val responseDocument = documentRepository.getDocument(projectId, experimentId, documentId)
            val documentList: MutableList<Document> = _documents.value!!.toMutableList()
            var indexToUpdate: Int? = null
            for ((index, document) in documentList.withIndex()) {
                if (document.id == documentId) {
                    indexToUpdate = index
                }
            }
            if (indexToUpdate != null) {
                documentList[indexToUpdate] = responseDocument
            }
            _documents.postValue(documentList)
            _downloadedDocument.postValue(responseDocument)
        }
    }

    fun setDocumentRepository(documentRepository: DocumentRepository) {
        this.documentRepository = documentRepository
    }
}