package com.hani.testing.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class NoteTest {

    companion object {

        val TIMESTAMP_1 = "05-2020"
        val TIMESTAMP_2 = "05-2020"

    }

    /*
    Compare two equal notes
     */

    /*
    Compare two equal notes
     */
    @Test
    fun isNotesEqual_identicalProperties_returnTrue() {
        val note1 = Note(1, "Note NO.1", "this is Note no. 1", TIMESTAMP_1)

        val note2 = Note(1, "Note NO.1", "this is Note no. 1", TIMESTAMP_1)

        assertEquals(note1, note2)

        System.out.println("The notes are equal!")
    }

    /*
    Compare two notes with 2 different ids
     */

    @Test
    fun isNotesEqual_differentIds_returnFalse() {

        val note1 = Note(1, "Note NO.1", "this is Note no. 1", TIMESTAMP_1)
        val note2 = Note(2, "Note NO.1", "this is Note no. 1", TIMESTAMP_1)

        assertNotEquals(note1, note2)

        System.out.println("The notes are not equal!")
    }

    /*
   Compare two notes with 2 different timestamps
    */
    @Test
     fun isNotesEqual_differentTimestamp_returnTrue() {

        val note1 = Note(1, "Note NO.1", "this is Note no. 1", TIMESTAMP_1)
        val note2 = Note(1, "Note NO.1", "this is Note no. 1", TIMESTAMP_2)

        assertEquals(note1, note2)
        System.out.println("The notes are equal!")
    }

    /*
    Compare two notes with 2 different titles
    */
    @Test
     fun isNotesEqual_differentTitles_returnFalse() {

        val note1 = Note(1, "Note NO.1", "this is Note no. 1", TIMESTAMP_1)
        val note2 = Note(1, "Note NO.2", "this is Note no. 1", TIMESTAMP_1)

        assertNotEquals(note1, note2)

        System.out.println("The notes are not equal!")
    }
    /*
    Compare two notes with 2 different content
    */

    @Test
    fun isNotesEqual_differentContent_returnFalse() {

        val note1 = Note(1, "Note NO.1", "this is Note no. 1", TIMESTAMP_1)
        val note2 = Note(1, "Note NO.1", "this is Note no. 2", TIMESTAMP_1)

        assertNotEquals(note1, note2)

        System.out.println("The notes are not equal!")
    }
}