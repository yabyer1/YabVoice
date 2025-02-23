package com.example.audioinput

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class RecordingActivity : AppCompatActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)
        val recordButton: Button = findViewById(R.id.btnStartRecording)
        val stopButton: Button = findViewById(R.id.btnStopRecording)
        recordButton.setOnClickListener {
                startRecording()
        }
        stopButton.setOnClickListener {
            stopRecording()
        }
    }


    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()

        }
        mediaRecorder = null
        Toast.makeText(this, "Recording saved!...", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, TranscriptionActivity::class.java)
        intent.putExtra("audioFilePath", audioFilePath)
        startActivity(intent)
    }

    private fun startRecording() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 200)
            return
        }
        val savedAudioDir = File(getExternalFilesDir(null), "saved_audio")
        if (!savedAudioDir.exists()) {
            savedAudioDir.mkdirs()
        }
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
            java.util.Date()
        )
        audioFilePath = "${savedAudioDir.absolutePath}/recording_$timeStamp.wav}"
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFilePath)
            try {
                prepare()
                start()
                Toast.makeText(this@RecordingActivity, "Recording started...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@RecordingActivity, "Failed to start recording", Toast.LENGTH_SHORT).show()
            }

        }

    }
}