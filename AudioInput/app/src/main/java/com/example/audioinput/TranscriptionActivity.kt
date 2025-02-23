package com.example.audioinput
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException

class TranscriptionActivity : AppCompatActivity() {
    private lateinit var transcriptionTextView: TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transcription)
        val transcriptionTextView : TextView = findViewById(R.id.tvTranscription)
        val audioFilePath = intent.getStringExtra("audioFilePath")
        if (audioFilePath != null) {
            sendAudiotoServer(audioFilePath, transcriptionTextView)
        }
        else{
            transcriptionTextView.text = "No audio file path provided"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun sendAudiotoServer(audioFilePath: String, transcriptionTextView: TextView) {
        val file = File(audioFilePath)
        if (!file.exists()) {
            transcriptionTextView.text = "Audio file not found"

            return
        }
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.name,
                RequestBody.create("audio/wav".toMediaTypeOrNull(), file)
            )
            .build()
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/transcribe")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    transcriptionTextView.text = "Transcription failed: ${e.message}"
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                val transcription = response.body?.string()
                runOnUiThread {
                    transcriptionTextView.text = "Transcription: $transcription"
                }
            }
        })

    }
}
