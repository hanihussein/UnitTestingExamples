package com.hani.testing.repository

import com.hani.testing.models.Note
import com.hani.testing.persistance.NoteDao
import com.hani.testing.ui.Resource
import com.hani.testing.utils.MockitoUtil
import com.hani.testing.utils.TestUtil
import io.reactivex.Single
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.mockito.Mockito.*

class NoteRepositoryTest {

    // System under test
    lateinit var noteRepository: NoteRepository

    lateinit var noteDao: NoteDao


    @BeforeEach
    fun initEach() {
        noteDao = mock(NoteDao::class.java)
        noteRepository = NoteRepository(noteDao)
    }
    /*
    insert note
    verify correct method is called
    confirm observer is trigger
    confirm new row inserted
     */

    @Test
    internal fun insertNote_returnTrue() {

        //Arrange
        val insertedRow = 1L
        val returnedData = Single.just(insertedRow)
        `when`<Any>(
            noteDao.insertNote(MockitoUtil<Note>().anyObject())
        ).thenReturn(returnedData)

        //Act
        val returnedValue = noteRepository.insertNotes(TestUtil.TEST_NOTE_1).blockingFirst()

        // Assert
        verify(noteDao).insertNote(MockitoUtil<Note>().anyObject())
        verifyNoMoreInteractions(noteDao)

        System.out.println("Returned value: " + returnedValue.data)
        assertEquals(Resource.success(1, noteRepository.INSERT_SUCCESS), returnedValue)

    }

    /*
    insert row
    return Failure (-1)
     */

    @Test
    internal fun insertNote_returnFailure() {
        //Arrange
        val failureInserted = -1L
        val returnedData = Single.just(failureInserted)
        `when`<Any>(
            noteDao.insertNote(MockitoUtil<Note>().anyObject())
        ).thenReturn(returnedData)

        //Act
        val returnedValue = noteRepository.insertNotes(TestUtil.TEST_NOTE_1).blockingFirst()

        // Assert
        verify(noteDao).insertNote(MockitoUtil<Note>().anyObject())
        verifyNoMoreInteractions(noteDao)

        assertEquals(Resource.error(null, noteRepository.INSERT_FAILURE), returnedValue)
    }

    /*
    insert note with null title
    return exception
     */

    @Test
    @Throws(Exception::class)
    internal fun insertNote_nullTitle_throwExpec() {
        Assertions.assertThrows(Exception::class.java, object : Executable {

            override fun execute() {
                val note = Note(TestUtil.TEST_NOTE_1)
                note.title = null
                noteRepository.insertNotes(note)
            }
        })

    }
}