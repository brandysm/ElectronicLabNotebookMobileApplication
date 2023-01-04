package com.github.brandysm.electroniclabnotebook.mobileapplication.recorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import java.text.SimpleDateFormat
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.brandysm.electroniclabnotebook.mobileapplication.R
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.ActivityRecorderBinding
import java.io.File
import java.util.*

class RecorderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecorderBinding

    var recordingStarted: Boolean = false
    var playbackStarted: Boolean = false
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecorderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 200)
        }

        val recordButton: Button = binding.buttonRecord
        val playbackButton: Button = binding.buttonPlayback
        val exitButton: Button = binding.buttonCancel
        val saveButton: Button = binding.buttonSave

        exitButton.setOnClickListener {
            finish()
        }

        recordButton.setOnClickListener {
            if (recordingStarted == false) {
                recordingStarted = true
                playbackButton.isEnabled = false
                recordButton.text = "Stop recording"
                recordButton.setBackgroundColor(resources.getColor(R.color.grey))
                mediaRecorder = MediaRecorder()
                mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mediaRecorder!!.setOutputFile(getTempFilePath())
                mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                mediaRecorder!!.prepare()
                mediaRecorder!!.start()
            } else {
                recordingStarted = false
                playbackButton.isEnabled = true
                recordButton.text = "Record audio"
                recordButton.setBackgroundColor(resources.getColor(R.color.red))
                mediaRecorder!!.stop()
                mediaRecorder!!.release()
                mediaRecorder = null
            }
        }

        playbackButton.setOnClickListener {
            if (playbackStarted == false) {
                playbackStarted = true
                recordButton.isEnabled = false
                playbackButton.text = "Stop playing audio"
                playbackButton.setBackgroundColor(resources.getColor(R.color.grey))
                mediaPlayer = MediaPlayer()
                if (File(getTempFilePath()).exists()) {
                    mediaPlayer!!.setDataSource(getTempFilePath())
                    mediaPlayer!!.setOnCompletionListener {
                        playbackStarted = false
                        recordButton.isEnabled = true
                        playbackButton.text = "Play recorded audio"
                        playbackButton.setBackgroundColor(resources.getColor(R.color.purple_500))
                        mediaPlayer!!.release()
                        Toast.makeText(
                            this,
                            "Recording ended!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    mediaPlayer!!.prepare()
                    mediaPlayer!!.start()
                    Toast.makeText(this, "Playing audio!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please record audio first!", Toast.LENGTH_SHORT).show()
                }
            } else {
                playbackStarted = false
                recordButton.isEnabled = true
                playbackButton.text = "Play recorded audio"
                playbackButton.setBackgroundColor(resources.getColor(R.color.purple_500))
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                Toast.makeText(
                    this,
                    "Recording stopped!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        saveButton.setOnClickListener {
            val testRecording = File(getTempFilePath())
            if (testRecording.exists()) {
                val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val fileToSave = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "${dateFormat}.mp3"
                )
                testRecording.copyTo(fileToSave)
                documentsAddFile(fileToSave.absolutePath)
                Toast.makeText(this, "Recording saved successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please record audio first!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getTempFilePath(): String {
        val uri =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        return File(uri, "tempRecording.mp3").absolutePath
    }

    private fun documentsAddFile(filePath: String) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(filePath)
            mediaScanIntent.data = Uri.fromFile(f)
            this.sendBroadcast(mediaScanIntent)
        }
    }
}