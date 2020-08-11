package com.hani.testing.persistance

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hani.testing.models.Note
import io.reactivex.Single

@Dao
interface NoteDao {
    @Insert
    @Throws(Exception::class)
    fun insertNote(note: Note?): Single<Long>

    @Query("SELECT * FROM notes")
    fun notes(): LiveData<List<Note>>

    @Delete
    @Throws(Exception::class)
    fun deleteNote(note: Note?): Single<Int>

    @Update
    @Throws(Exception::class)
    fun updateNote(note: Note?): Single<Int>
}