package com.example.recorder

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import java.io.File

class EditAudioActivity : AppCompatActivity(){
    private lateinit var fileName: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editaudio)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val rename = findViewById<Button>(R.id.btnRename)
        val trim = findViewById<Button>(R.id.btnTrim)
        val delete = findViewById<Button>(R.id.btnDelete)

        fileName = "${externalCacheDir?.absolutePath}/recording.mp3"

        rename.setOnClickListener{
            renameAudio()
        }

        trim.setOnClickListener{
            Toast.makeText(this, "Audio Trimmed", Toast.LENGTH_SHORT).show()
        }

        delete.setOnClickListener{
            deleteAudio()
        }
    }

    private fun  renameAudio(){
        val oldFile = File(fileName)
        val newFile = File("${externalCacheDir?.absolutePath}/renamed_recording.mp3")

        if (oldFile.exists()){
            if (oldFile.renameTo(newFile)) {
                Toast.makeText(this, "Audio Renamed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteAudio(){
        val file = File(fileName)

        if (file.exists()){
            file.delete()
            Toast.makeText(this, "Audio Deleted", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
        }
    }
}