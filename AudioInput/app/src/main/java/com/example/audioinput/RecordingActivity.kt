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

        recordButton.setOnClickListener { startRecording() }
        stopButton.setOnClickListener { stopRecording() }
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

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
            java.util.Date()
        )
        audioFilePath = "${savedAudioDir.absolutePath}/recording_$timeStamp.wav"  // Fixed incorrect path
        Toast.makeText(this, "Saving file at: $audioFilePath", Toast.LENGTH_LONG).show()
        println("DEBUG: Audio file path -> $audioFilePath")
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFilePath)  // Ensure file path is used
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

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null

        if (audioFilePath.isNotEmpty()) {
            Toast.makeText(this, "Recording saved at: $audioFilePath", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TranscriptionActivity::class.java).apply {
                putExtra("audioFilePath", audioFilePath)  // Ensure correct file path is passed
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Recording file path is invalid", Toast.LENGTH_SHORT).show()
        }
    }
}
