package com.example.recorder

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class EditAudioActivity : AppCompatActivity() {
    private var audioPath: String? = null
    private var audioName: String? = null

    private lateinit var tvCurrentName: TextView
    private lateinit var tvFilePath: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editaudio)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        audioPath = intent.getStringExtra("AUDIO_PATH")
        audioName = intent.getStringExtra("AUDIO_NAME")

        tvCurrentName = findViewById(R.id.tvCurrentName)
        tvFilePath = findViewById(R.id.tvFilePath)

        tvCurrentName.text = audioName ?: "Unknown"
        tvFilePath.text = audioPath ?: "No Path Found"

        val rename = findViewById<Button>(R.id.btnRename)
        val trim = findViewById<Button>(R.id.btnTrim)
        val delete = findViewById<Button>(R.id.btnDelete)

        rename.setOnClickListener {
            showRenameDialog()
        }

        trim.setOnClickListener {
            Toast.makeText(this, "Trim functionality coming soon!", Toast.LENGTH_SHORT).show()
        }

        delete.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showRenameDialog() {
        val input = EditText(this)
        // Show name without extension for easier editing
        val currentFile = File(audioPath ?: "")
        val nameWithoutExt = currentFile.nameWithoutExtension
        input.setText(nameWithoutExt)
        
        // Add some padding to the EditText
        val container = FrameLayout(this)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(48, 24, 48, 24)
        input.layoutParams = params
        container.addView(input)

        MaterialAlertDialogBuilder(this)
            .setTitle("Rename Recording")
            .setView(container)
            .setPositiveButton("Rename") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotBlank()) {
                    processRename(newName)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun processRename(newName: String) {
        val currentFile = File(audioPath ?: return)
        val parentDir = currentFile.parentFile
        val extension = currentFile.extension
        val newFile = File(parentDir, "$newName.$extension")

        if (currentFile.exists()) {
            if (currentFile.renameTo(newFile)) {
                // Update local state and UI
                audioName = newFile.name
                audioPath = newFile.absolutePath
                tvCurrentName.text = audioName
                tvFilePath.text = audioPath
                
                Toast.makeText(this, "File successfully renamed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Rename failed. Try a different name.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Original file no longer exists.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Recording")
            .setMessage("Are you sure you want to permanently delete this recording?")
            .setPositiveButton("Delete") { _, _ ->
                deleteAudio()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteAudio() {
        val file = File(audioPath ?: return)
        if (file.exists()) {
            if (file.delete()) {
                Toast.makeText(this, "Audio Deleted", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Could not delete file", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
