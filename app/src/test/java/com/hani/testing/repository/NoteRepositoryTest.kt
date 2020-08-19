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
    internal fun noteRepository_insertNote_returnTrue() {

        //Arrange
        val insertedRow = 1L
        val returnedData = Single.just(insertedRow)
        `when`<Any>(
            noteDao.insertNote(MockitoUtil<Note>().anyObject())
        ).thenReturn(returnedData)

        //Act
        val returnedValue = noteRepository.insertNote(TestUtil.TEST_NOTE_1).blockingFirst()

        // Assert
        verify(noteDao).insertNote(MockitoUtil<Note>().anyObject())
        verifyNoMoreInteractions(noteDao)

        System.out.println("Returned value: " + returnedValue.data)
        assertEquals(Resource.success(1, NoteRepository.INSERT_SUCCESS), returnedValue)

    }

    /*
    insert row
    return Failure (-1)
     */

    @Test
    internal fun noteRepository_insertNote_returnFailure() {
        //Arrange
        val failureInserted = -1L
        val returnedData = Single.just(failureInserted)
        `when`<Any>(
            noteDao.insertNote(MockitoUtil<Note>().anyObject())
        ).thenReturn(returnedData)

        //Act
        val returnedValue = noteRepository.insertNote(TestUtil.TEST_NOTE_1).blockingFirst()

        // Assert
        verify(noteDao).insertNote(MockitoUtil<Note>().anyObject())
        verifyNoMoreInteractions(noteDao)

        assertEquals(Resource.error(null, NoteRepository.INSERT_FAILURE), returnedValue)
    }

    /*
    insert note with null title
    return exception
     */

    @Test
    @Throws(Exception::class)
    internal fun noteRepository_insertNote_nullTitle_throwExpec() {
        Assertions.assertThrows(Exception::class.java, object : Executable {

            override fun execute() {
                val note = Note(TestUtil.TEST_NOTE_1)
                note.title = null
                noteRepository.insertNote(note)
            }
        })
    }


    /*
    Update note
    verify correct method is called
    confirm observer is trigger
    confirm number of  row inserted
     */


    @Test
    fun noteRepository_updateNote_returnUpdatedRow() {

        //Arrange
        val returnedRawValue = 1;
        `when`(noteDao.updateNote(MockitoUtil<Note>().anyObject())).thenReturn(
            Single.just(
                returnedRawValue
            )
        )

        //Act
        val returnedResult =
            noteRepository.updateNote(TestUtil.TEST_NOTE_1).blockingFirst()

        //Assert
        verify(noteDao).updateNote(MockitoUtil<Note>().anyObject())
        verifyNoMoreInteractions(noteDao)
        assertEquals(
            returnedResult,
            Resource.success(returnedRawValue, NoteRepository.UPDATE_SUCCESS)
        )
    }

    /*
    Update row
    return Failure (-1)
   */

    @Test
    internal fun noteRepository_updateNote_returnFailure() {
        //Arrange
        val failureInserted = -1
        val returnedData = Single.just(failureInserted)
        `when`<Any>(
            noteDao.updateNote(MockitoUtil<Note>().anyObject())
        ).thenReturn(returnedData)

        //Act
        val returnedValue = noteRepository.updateNote(TestUtil.TEST_NOTE_1).blockingFirst()

        // Assert
        verify(noteDao).updateNote(MockitoUtil<Note>().anyObject())
        verifyNoMoreInteractions(noteDao)

        assertEquals(Resource.error(null, NoteRepository.UPDATE_FAILURE), returnedValue)
    }

    /*
    insert note with null title
    return exception
     */

    @Test
    @Throws(Exception::class)
    internal fun noteRepository_updateNote_nullTitle_throwExpec() {
        Assertions.assertThrows(Exception::class.java, object : Executable {
            override fun execute() {
                val note = Note(TestUtil.TEST_NOTE_1)
                note.title = null
                noteRepository.updateNote(note)
            }
        })
    }

}