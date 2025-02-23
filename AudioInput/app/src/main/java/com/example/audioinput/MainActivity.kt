package com.example.audioinput

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recordButton: Button = findViewById(R.id.btnRecord)
        val viewTranscriptionButton: Button = findViewById(R.id.btnViewTranscription)
        recordButton.setOnClickListener {
            val intent = Intent(this, RecordingActivity::class.java)
            startActivity(intent)
        }
        viewTranscriptionButton.setOnClickListener {
            val intent = Intent(this, TranscriptionActivity::class.java)
            startActivity(intent)
        }
    }


}



