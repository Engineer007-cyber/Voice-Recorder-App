package com.example.recorder

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import java.io.IOException

class PlayBackScreen : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    private var audioPath: String? = null
    private var audioName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playbackscreen)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        audioPath = intent.getStringExtra("AUDIO_PATH")
        audioName = intent.getStringExtra("AUDIO_NAME")


        val tvTitle = findViewById<TextView>(R.id.tvAudioTitle)
        tvTitle.text = audioName ?: "Unknown Recording"

        val play = findViewById<Button>(R.id.btnPlay)
        val pause = findViewById<Button>(R.id.btnPause)
        val stop = findViewById<Button>(R.id.btnStop)

        play.setOnClickListener {
            playAudio()
        }

        pause.setOnClickListener {
            pauseAudio()
        }

        stop.setOnClickListener {
            stopAudio()
        }
    }

    private fun playAudio() {
        if (audioPath == null) {
            Toast.makeText(this, "Audio file not found", Toast.LENGTH_SHORT).show()
            return
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                try {
                    setDataSource(audioPath)
                    prepare()
                    start()
                } catch (e: IOException) {
                    Toast.makeText(this@PlayBackScreen, "Error playing audio", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            mediaPlayer?.start()
        }
        Toast.makeText(this, "Playing Audio", Toast.LENGTH_SHORT).show()
    }

    private fun pauseAudio() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            Toast.makeText(this, "Audio Paused", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        Toast.makeText(this, "Audio Stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}