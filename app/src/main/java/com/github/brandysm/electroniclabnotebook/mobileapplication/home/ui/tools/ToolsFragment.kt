package com.github.brandysm.electroniclabnotebook.mobileapplication.home.ui.tools

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.brandysm.electroniclabnotebook.mobileapplication.alarm.AlarmActivity
import com.github.brandysm.electroniclabnotebook.mobileapplication.databinding.FragmentToolsBinding
import com.github.brandysm.electroniclabnotebook.mobileapplication.note.NoteActivity
import com.github.brandysm.electroniclabnotebook.mobileapplication.recorder.RecorderActivity
import com.github.brandysm.electroniclabnotebook.mobileapplication.timer.TimerActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ToolsFragment : Fragment() {

    private var _binding: FragmentToolsBinding? = null

    lateinit var currentPhotoPath: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val toolsViewModel =
            ViewModelProvider(this).get(ToolsViewModel::class.java)
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val cameraButton: Button = binding.buttonCamera
        val voiceRecorderButton: Button = binding.buttonVoiceRecorder
        val notesButton: Button = binding.buttonNotes
        val alarmButton: Button = binding.buttonAlarm
        val timerButton: Button = binding.buttonTimer

        cameraButton.setOnClickListener {
            takePicture()
        }
        voiceRecorderButton.setOnClickListener {
            val recorderIntent: Intent = Intent(activity, RecorderActivity::class.java)
            startActivityForResult(recorderIntent, 4)
        }
        notesButton.setOnClickListener {
            val noteIntent: Intent = Intent(activity, NoteActivity::class.java)
            startActivityForResult(noteIntent, 2)
        }
        alarmButton.setOnClickListener {
            val alarmIntent: Intent = Intent(activity, AlarmActivity::class.java)
            startActivityForResult(alarmIntent, 3)
        }
        timerButton.setOnClickListener {
            val timerIntent: Intent = Intent(activity, TimerActivity::class.java)
            startActivity(timerIntent)
        }

        return root
    }

    private fun takePicture() {
        val uri = Uri.withAppendedPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toUri(), SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())+".jpg")
        currentPhotoPath = uri.path!!
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        startActivityForResult(takePictureIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 &&  resultCode == Activity.RESULT_OK) {
            galleryAddPic()
            Toast.makeText(activity, "Picture saved successfully!", Toast.LENGTH_SHORT).show()
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(activity, "Note saved successfully!", Toast.LENGTH_SHORT).show()
        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(activity, "Alarm set successfully!", Toast.LENGTH_SHORT).show()
        } else if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(activity, "Recording saved successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            requireActivity().sendBroadcast(mediaScanIntent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}