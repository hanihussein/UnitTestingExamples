package com.hani.testing.ui.notelist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.hani.testing.R
import com.hani.testing.repository.NoteRepository
import com.hani.testing.ui.note.NoteActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class NotesListActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var noteRepository: NoteRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)
        startActivity(Intent(this , NoteActivity::class.java))
    }


}