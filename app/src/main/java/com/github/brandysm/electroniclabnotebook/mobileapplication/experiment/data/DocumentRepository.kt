package com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.data

import com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.data.model.Document

class DocumentRepository(val dataSource: DocumentDataSource) {
    var documentList: MutableList<Document>? = null

    fun getDocuments(projectId: Long, experimentId: Long): List<Document> {
        val documents = dataSource.getDocuments(projectId, experimentId)
        documentList = documents
        return documents
    }

    fun addDocument(projectId: Long, experimentId: Long, document: Document): Document {
        val responseDocument = dataSource.addDocument(projectId, experimentId, document)
        documentList!!.add(responseDocument)
        return responseDocument
    }

    fun removeDocument(projectId: Long, experimentId: Long, documentId: Long) {
        dataSource.removeDocument(projectId, experimentId, documentId)
        var documentToRemove: Document? = null
        for (document in documentList!!) {
            if (document.id == documentId)
                documentToRemove = document
        }
        if (documentToRemove != null) {
            documentList!!.remove(documentToRemove)
        }
    }

    fun getDocument(projectId: Long, experimentId: Long, documentId: Long): Document {
        val responseDocument =
            dataSource.getDocument(projectId, experimentId, documentId)
        var indexToUpdate: Int? = null
        for ((index, document) in documentList!!.withIndex()) {
            if (document.id == documentId) {
                indexToUpdate = index
            }
        }
        if (indexToUpdate != null) {
            documentList!![indexToUpdate] = responseDocument
        }
        return responseDocument
    }
}