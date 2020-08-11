package com.hani.testing.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hani.testing.persistance.NoteDao
import com.hani.testing.persistance.NoteDatabase
import org.junit.After
import org.junit.Before

class DatabaseTest {

    lateinit var noteDatabase: NoteDatabase

    fun getNoteDae(): NoteDao = noteDatabase.getNoteDae()

    @Before
    fun init() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).build()
    }


    @After
    fun finish() {
        noteDatabase.close()
    }
}