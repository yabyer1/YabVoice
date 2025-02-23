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

class RecordingActivity : AppCompatActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String = ""
    fun OnCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)
        val recordButton : Button = findViewById(R.id.btnStartRecording)
        recordButton.setOnClickListener{
            if(mediaRecorder == null){
                startRecording()
            }else{
                stopRecording()
                val intent = Intent(this, TranscriptionActivity::class.java).apply{
                    putExtra("audioFilePath", audioFilePath)
                }
                startActivity(intent)
            }
        }
    }



    private fun stopRecording() {
        mediaRecorder?.apply{
            stop()
            release()

        }
        mediaRecorder = null
        Toast.makeText(this, "Recording saved!...", Toast.LENGTH_SHORT).show()
    }

    private fun startRecording() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO) , 200)
            return
        }
        audioFilePath = "${externalCacheDir?.absolutePath}/recorded_audio.wav"
        mediaRecorder = MediaRecorder().apply{
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFilePath)
            try{
                prepare()
            }catch(e: Exception){

            }
            start()
        }
        Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show()

    }
}