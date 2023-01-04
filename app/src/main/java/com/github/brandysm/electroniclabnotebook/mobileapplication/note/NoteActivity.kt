package com.github.brandysm.electroniclabnotebook.mobileapplication.note

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.ActivityNoteBinding
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding

    lateinit var currentFilePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val saveButton: Button = binding.buttonSave
        val exitButton: Button = binding.buttonCancel

        exitButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            val fileName: String = binding.editTextFileName.text.toString().trim() + ".txt"
            val fileContent: String = binding.editTextTextMultiLine.text.toString().trim()
            if (fileName.isEmpty() || fileContent.isEmpty()) {
                Toast.makeText(this, "Please fill file name and file contents", Toast.LENGTH_SHORT)
                    .show()
            } else {
                saveToTextFile(fileName, fileContent)
                documentsAddFile()
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

    }

    private fun saveToTextFile(fileName: String, fileContent: String) {
        val uri =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(uri, fileName)
        currentFilePath = file.absolutePath
        val fileWriter = FileWriter(file.absoluteFile)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(fileContent)
        bufferedWriter.close()
    }

    private fun documentsAddFile() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentFilePath)
            mediaScanIntent.data = Uri.fromFile(f)
            this.sendBroadcast(mediaScanIntent)
        }
    }
}