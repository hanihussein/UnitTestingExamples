package com.hani.testing.database

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hani.testing.models.Note
import com.hani.testing.utils.LiveDataTestUtil
import com.hani.testing.utils.TestUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

open class NoteDaoTest : NoteDatabaseTest() {

    val TEST_TITLE = "This is a test title"
    val TEST_CONTENT = "This is some test content"
    val TEST_TIMESTAMP = "08-2018"


    @Rule @JvmField
    public var rule = InstantTaskExecutorRule()


    /*
     insert , read , delete
     */
    @Test
    @Throws(Exception::class)
    fun insertReadDelete() {

        val note = Note(TestUtil.TEST_NOTE_1)

        // Insert
        getNoteDao().insertNote(note).blockingGet()

        // read
        val liveDataTestUtil: LiveDataTestUtil<List<Note>> = LiveDataTestUtil()
        var insertedNotes = liveDataTestUtil.getValue(getNoteDao().notes())

        assertNotNull(insertedNotes)
        assertEquals(note.content, insertedNotes[0].content)
        assertEquals(note.timestamp, insertedNotes[0].timestamp)
        assertEquals(note.title, insertedNotes[0].title)

        note.id = insertedNotes[0].id
        assertEquals(note, insertedNotes[0])

        getNoteDao().deleteNote(note).blockingGet()

        // confirm the database is empty
        insertedNotes = liveDataTestUtil.getValue(getNoteDao().notes())
        assertEquals(0, insertedNotes.size)
    }


    /* insert , read , update , read , delete
     */
    @Test
    @Throws(Exception::class)
    fun insertReadUpdateReadDelete() {

        val note = Note(TestUtil.TEST_NOTE_1)

        //Insert
        getNoteDao().insertNote(note).blockingGet()

        // read
        val liveDataTestUtil: LiveDataTestUtil<List<Note>> = LiveDataTestUtil()
        var insertedNotes = liveDataTestUtil.getValue(getNoteDao().notes())

        assertNotNull(insertedNotes)

        note.id = insertedNotes[0].id
        assertEquals(note, insertedNotes[0])
        assertEquals(note.title, insertedNotes[0].title)
        assertEquals(note.content, insertedNotes[0].content)

        //Update

        note.title = TEST_TITLE
        note.content = TEST_CONTENT
        note.timestamp = TEST_TIMESTAMP

        getNoteDao().updateNote(note).blockingGet()

        //Read
        insertedNotes = liveDataTestUtil.getValue(getNoteDao().notes())

        assertNotNull(insertedNotes)
        assertEquals(note, insertedNotes[0])
        assertEquals(TEST_TITLE, insertedNotes[0].title)
        assertEquals(TEST_CONTENT, insertedNotes[0].content)
        assertEquals(TEST_TIMESTAMP, insertedNotes[0].timestamp)


        //Delete
        getNoteDao().deleteNote(note).blockingGet()

        insertedNotes = liveDataTestUtil.getValue(getNoteDao().notes())
        assertEquals(0, insertedNotes.size.toLong())

    }


    /* insert Note with null title, throw exception
    */

    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun insert_nullTitle_throwSQLiteConstraintException() {

        val note = Note(TestUtil.TEST_NOTE_1)
        note.title = null

        // insert
        getNoteDao().insertNote(note).blockingGet()
    }


    /*
        Insert, Update with null title, throw exception
     */
    @Test(expected = SQLiteConstraintException::class)
    @Throws(Exception::class)
    fun updateNote_nullTitle_throwSQLiteConstraintException() {
        var note = Note(TestUtil.TEST_NOTE_1)

        // insert
        getNoteDao().insertNote(note).blockingGet()

        // read
        val liveDataTestUtil: LiveDataTestUtil<List<Note>> = LiveDataTestUtil()
        val insertedNotes =
            liveDataTestUtil.getValue(getNoteDao().notes())
        assertNotNull(insertedNotes)

        // update
        note = Note(insertedNotes[0])
        note.title = null
        getNoteDao().updateNote(note).blockingGet()
    }
}