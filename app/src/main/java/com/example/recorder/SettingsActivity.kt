package com.example.recorder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val quality = findViewById<MaterialSwitch>(R.id.switchQuality)
        val autoSave = findViewById<MaterialSwitch>(R.id.switchAutoSave)
        val darkMode = findViewById<MaterialSwitch>(R.id.switchDarkMode)

        quality.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "Enabled" else "Disabled"
            Toast.makeText(this, "High Quality $status", Toast.LENGTH_SHORT).show()
        }

        autoSave.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "Enabled" else "Disabled"
            Toast.makeText(this, "Auto Save $status", Toast.LENGTH_SHORT).show()
        }

        darkMode.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "Enabled" else "Disabled"
            Toast.makeText(this, "Dark Mode $status", Toast.LENGTH_SHORT).show()
        }
    }
}