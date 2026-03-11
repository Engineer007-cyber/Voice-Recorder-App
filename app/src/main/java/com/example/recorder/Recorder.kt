package com.example.recorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.appbar.MaterialToolbar
import java.io.IOException

class Recorder : AppCompatActivity() {
    private var recorder: MediaRecorder? = null
    private var isRecording = false
    private var audioUri: Uri? = null

    private lateinit var tvStatus: TextView
    private lateinit var tvTimer: TextView

    private var seconds = 0
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        @SuppressLint("DefaultLocale")
        override fun run() {
            seconds++
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val secs = seconds % 60
            tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, secs)
            handler.postDelayed(this, 1000)
        }
    }

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

        start.setOnClickListener {
            if (!isRecording) {
                if (checkPermission()) {
                    startRecording()
                } else {
                    requestPermission()
                }
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

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 200)
    }

    private fun startRecording() {
        val fileName = "recording_${System.currentTimeMillis()}.mp3"
        
        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_MUSIC + "/Recordings")
                put(MediaStore.Audio.Media.IS_PENDING, 1)
            }
        }

        audioUri = contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)

        if (audioUri == null) {
            Toast.makeText(this, "Failed to create file in MediaStore", Toast.LENGTH_SHORT).show()
            return
        }

        val fileDescriptor = contentResolver.openFileDescriptor(audioUri!!, "w")?.fileDescriptor

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(fileDescriptor)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            try {
                prepare()
                start()
                isRecording = true
                tvStatus.text = "Recording..."
                tvStatus.setTextColor(getColor(android.R.color.holo_red_dark))

                seconds = 0
                tvTimer.text = "00:00:00"
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 1000)

                Toast.makeText(this@Recorder, "Recording started", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(this@Recorder, "Recorder failed: ${e.message}", Toast.LENGTH_SHORT).show()
                isRecording = false
            }
        }
    }

    private fun stopRecording() {
        try {
            recorder?.stop()
            recorder?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        recorder = null
        isRecording = false
        tvStatus.text = "Recording Saved"
        tvStatus.setTextColor(getColor(R.color.black))

        handler.removeCallbacks(runnable)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && audioUri != null) {
            val values = ContentValues().apply {
                put(MediaStore.Audio.Media.IS_PENDING, 0)
            }
            contentResolver.update(audioUri!!, values, null, null)
        }

        Toast.makeText(this, "Recording saved to your music library", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        if (isRecording) {
            stopRecording()
        }
    }
}
