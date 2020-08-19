package com.hani.testing.ui.note

import com.hani.testing.models.Note
import com.hani.testing.repository.NoteRepository
import com.hani.testing.ui.Resource
import com.hani.testing.utils.InstanceExecutorExtension
import com.hani.testing.utils.LiveDataTestUtil
import com.hani.testing.utils.MockitoUtil
import com.hani.testing.utils.TestUtil
import io.reactivex.internal.operators.single.SingleToFlowable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.*

@ExtendWith(InstanceExecutorExtension::class)
open class NoteVIewModelTest {

    //system Under Test

    lateinit var noteViewModel: NoteViewModel

    lateinit var repository: NoteRepository


    @BeforeEach
    fun init() {
        repository = mock(NoteRepository::class.java)
        noteViewModel = NoteViewModel(repository)
    }

    /* Cannot observe note that has not been set*/
    @Test
    internal fun observe_NoNote_noObserver() {

        val liveDataTestUtil: LiveDataTestUtil<Note> = LiveDataTestUtil()

        val note = liveDataTestUtil.getValue(noteViewModel.noteLiveData)

        assertNull(note)
    }

//    /*Observe a note that has been set and onChange will be trigger*/
    @Test
    @Throws(Exception::class)
    internal fun observeNote_whenSet() {

        val note = Note(TestUtil.TEST_NOTE_1)
        val liveDataTestUtil: LiveDataTestUtil<Note> = LiveDataTestUtil()

        noteViewModel.setNote(note)
        val observedNote = liveDataTestUtil.getValue(noteViewModel.noteLiveData)

        assertEquals(note, observedNote)

    }

    /*insert a note and observe row inserted*/

    @Test
    @Throws(Exception::class)
    internal fun insertNote_ReturnRaw() {

        // Arrange
        val note = Note(TestUtil.TEST_NOTE_1)
        val liveDataTestUtil: LiveDataTestUtil<Resource<Int?>> = LiveDataTestUtil()

        val insertedRawValue = 1
        val returnedData =
            SingleToFlowable.just(Resource.success<Int?>(insertedRawValue, NoteRepository.INSERT_SUCCESS))

        Mockito.`when`(repository.insertNote(com.nhaarman.mockitokotlin2.any()))
            .thenAnswer {
                return@thenAnswer returnedData
            }

        //Act
        noteViewModel.setNote(note)
        val observerRawValue = liveDataTestUtil.getValue(noteViewModel.insertNote())

        //Assert
        assertEquals(
            observerRawValue,
            Resource.success(insertedRawValue, NoteRepository.INSERT_SUCCESS)
        )
    }

//    /*insert : don't return a new row without observer*/
//    @Test
//    @Throws(Exception::class)
//    internal fun noNoteInserted_noObserver() {
//
//        //Arrange
//        val note = Note(TestUtil.TEST_NOTE_1)
//
//        //Act
//        noteViewModel.setNote(note)
//
//        // Assert
//        verify(repository, never())
//            .insertNote(MockitoUtil<Note>().anyObject())
//    }

    /*set note , null title , throw exception*/

    @Test
    @Throws(Exception::class)
    internal fun setNote_nullTitle_throwException() {

        //Arrange
        val note = Note(TestUtil.TEST_NOTE_1)
        note.title = null

        //Assert
        assertThrows(Exception::class.java, {
            //Act
            noteViewModel.setNote(note)
        })
    }
}