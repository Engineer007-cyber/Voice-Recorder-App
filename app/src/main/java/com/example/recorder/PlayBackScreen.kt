package com.example.recorder

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PlayBackScreen : AppCompatActivity(){
    private var mediaPlayer: MediaPlayer? = null
    private var fileName = ""

    @SuppressLint("MissingSuperCall", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_playbackscreen)
            val play = findViewById<Button>(R.id.btnPlay)
            val pause = findViewById<Button>(R.id.btnPause)
            val stop = findViewById<Button>(R.id.btnStop)
        fileName = "${externalCacheDir?.absolutePath}/"

        play.setOnClickListener{
            playAudio()
        }

        pause.setOnClickListener{
            pauseAudio()
        }

        stop.setOnClickListener{
            stopAudio()
        }
    }

    private fun playAudio(){
        mediaPlayer = MediaPlayer().apply {
            setDataSource(fileName)
            prepare()
            start()
        }
        Toast.makeText(this, "Playing Audio", Toast.LENGTH_SHORT).show()
    }

    private fun pauseAudio(){
        mediaPlayer?.pause()
        Toast.makeText(this, "Audio Paused", Toast.LENGTH_SHORT).show()
    }

    private fun stopAudio(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        Toast.makeText(this, "Audio Stopped", Toast.LENGTH_SHORT).show()
    }
}