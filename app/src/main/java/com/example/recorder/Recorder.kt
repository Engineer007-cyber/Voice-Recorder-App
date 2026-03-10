package com.example.recorder

import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import java.io.IOException

class Recorder : AppCompatActivity(){
    private var recorder: MediaRecorder? = null
    private var fileName: String = ""
    private var isRecording = false

    private lateinit var tvStatus: TextView
    private lateinit var tvTimer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        tvStatus = findViewById(R.id.tvStatus)
        tvTimer = findViewById(R.id.tvTimer)
        val start = findViewById<Button>(R.id.btnStart)
        val stop = findViewById<Button>(R.id.btnStop)

        fileName = "${externalCacheDir?.absolutePath}/recording.mp3"

        start.setOnClickListener {
            if (!isRecording) {
                startRecording()
            } else {
                Toast.makeText(this, "Already recording", Toast.LENGTH_SHORT).show()
            }
        }

        stop.setOnClickListener {
            if (isRecording) {
                stopRecording()
            } else {
                Toast.makeText(this, "Not recording", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startRecording(){
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            try {
                prepare()
                start()
                isRecording = true
                tvStatus.text = "Recording..."
                tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))
                Toast.makeText(this@Recorder, "Started recording", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(this@Recorder, "Preparation failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopRecording(){
        recorder?.apply {
            try {
                stop()
                release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        recorder = null
        isRecording = false
        tvStatus.text = "Recording Saved"
        tvStatus.setTextColor(getColor(R.color.black)) // Standard color

        Toast.makeText(this, "Recording Saved to $fileName", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        if (isRecording) {
            stopRecording()
        }
    }
}