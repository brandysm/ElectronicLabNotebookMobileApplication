package com.github.brandysm.electroniclabnotebook.mobileapplication.experiment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.ActivityExperimentBinding
import com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.data.DocumentDataSource
import com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.data.DocumentRepository
import com.github.brandysm.electroniclabnotebook.mobileapplication.experiment.data.model.Document
import java.io.File

class ExperimentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExperimentBinding

    lateinit var documentAdapter: DocumentAdapter
    lateinit var experimentViewModel: ExperimentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        experimentViewModel =
            ViewModelProvider(this).get(ExperimentViewModel::class.java)
        experimentViewModel.setDocumentRepository(DocumentRepository(DocumentDataSource()))

        binding = ActivityExperimentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val documentRecyclerView: RecyclerView = binding.recyclerviewDocumentList
        documentRecyclerView.layoutManager = LinearLayoutManager(this)
        documentAdapter = DocumentAdapter(
            this, experimentViewModel, intent.getLongExtra("EXTRA_PROJECT_ID", -1),
            intent.getLongExtra("EXTRA_EXPERIMENT_ID", -1)
        )
        documentRecyclerView.adapter = documentAdapter

        val itemTouchHelper = ItemTouchHelper(
            DocumentItemTouchHelper(
                0,
                ItemTouchHelper.LEFT,
                documentAdapter
            ).setViewModel(
                experimentViewModel,
                intent.getLongExtra("EXTRA_PROJECT_ID", -1),
                intent.getLongExtra("EXTRA_EXPERIMENT_ID", -1)
            )
        )
        itemTouchHelper.attachToRecyclerView(documentRecyclerView)

        binding.textExperiment.text =
            getString(R.string.experiment_semicolon, intent.getStringExtra("EXTRA_EXPERIMENT_NAME"))
        experimentViewModel.documents.observe(this@ExperimentActivity) {
            val documents = it
            documentAdapter.setDocuments(documents)
        }
        experimentViewModel.downloadedDocument.observe(this@ExperimentActivity) {
            val document: Document = it
            saveFile(document)
            Toast.makeText(this, "Document saved successfully!", Toast.LENGTH_SHORT).show()
        }
        experimentViewModel.getDocuments(
            intent.getLongExtra("EXTRA_PROJECT_ID", -1),
            intent.getLongExtra("EXTRA_EXPERIMENT_ID", -1)
        )

        val fab = binding.floatingbuttonDocumentList
        fab.setOnClickListener {
            val selectFileIntent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            selectFileIntent.type = "*/*";
            startActivityForResult(selectFileIntent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            uploadFile(data!!.data!!)
            Toast.makeText(this, "Document uploaded successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadFile(uri: Uri) {
        val file: File = File(uri.path!!)
        val byteArray: ByteArray = contentResolver.openInputStream(uri)!!.readBytes()
        val encodedFile: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        experimentViewModel.addDocument(
            intent.getLongExtra("EXTRA_PROJECT_ID", -1),
            intent.getLongExtra("EXTRA_EXPERIMENT_ID", -1),
            Document(null, file.name, null, encodedFile)
        )
    }

    private fun saveFile(document: Document) {
        val uri =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(uri, document.name)
        val byteArray: ByteArray = Base64.decode(document.data, Base64.DEFAULT)
        file.writeBytes(byteArray)
    }
}