package com.example.recorder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class RecordingListActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recordinglist)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        val recordings = listOf("Meeting Oct 27", "Lecture Notes", "Song Idea")
        recyclerView.adapter = RecordingAdapter(recordings) { _ ->
            val intent = Intent(this, PlayBackScreen::class.java)
            startActivity(intent)
        }
    }
}

class RecordingAdapter(
    private val recordings: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<RecordingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFileName: TextView = view.findViewById(R.id.tvFileName)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val root: View = view.findViewById(R.id.cardRecording)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recording, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = recordings[position]
        holder.tvFileName.text = name
        holder.tvDetails.text = " "
        holder.root.setOnClickListener { onItemClick(name) }
    }

    override fun getItemCount() = recordings.size
}