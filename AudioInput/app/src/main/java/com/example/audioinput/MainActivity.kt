package com.example.audioinput

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.audioinput.ui.theme.AudioInputTheme

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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AudioInputTheme {
        Greeting("Android")
    }
}