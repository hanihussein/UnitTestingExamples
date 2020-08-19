package com.hani.testing.repository

import com.hani.testing.models.Note
import com.hani.testing.persistance.NoteDao
import com.hani.testing.ui.Resource
import io.reactivex.Flowable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NoteRepository @Inject constructor(var noteDao: NoteDao) {

    companion object {
        val NOTE_TITLE_NULL = "Note title cannot be null"
        val INVALID_NOTE_ID = "Invalid id. Can't delete note"
        val DELETE_SUCCESS = "Delete success"
        val DELETE_FAILURE = "Delete failure"
        val UPDATE_SUCCESS = "Update success"
        val UPDATE_FAILURE = "Update failure"
        val INSERT_SUCCESS = "Insert success"
        val INSERT_FAILURE = "Insert failure"

    }

    private val timeDelay = 0
    private val timeUnit = TimeUnit.SECONDS


    @Throws(Exception::class)
    fun insertNote(note: Note): Flowable<Resource<Int?>> {
        checkTitle(note)
        return noteDao.insertNote(note)
            .delaySubscription(timeDelay.toLong(), timeUnit)
            .map { aLong ->
                aLong.toInt()
            }.onErrorReturn(object : Function<Throwable, Int> {
                @Throws(java.lang.Exception::class)
                override fun apply(t: Throwable): Int {
                    return -1
                }
            })
            .map(object : Function<Int, Resource<Int?>> {
                override fun apply(t: Int): Resource<Int?> {
                    if (t > 0) {
                        return Resource.success(t, INSERT_SUCCESS)
                    } else
                        return Resource.error(0, INSERT_FAILURE)
                }
            })
            .subscribeOn(Schedulers.io())
            .toFlowable()
    }

    @Throws(Exception::class)
    fun updateNote(note: Note): Flowable<Resource<Int?>> {
        checkTitle(note)
        return noteDao.updateNote(note)
            .delaySubscription(timeDelay.toLong(), timeUnit)
            .onErrorReturn(object : Function<Throwable, Int> {
                @Throws(java.lang.Exception::class)
                override fun apply(t: Throwable): Int {
                    return -1
                }
            })
            .map(object : Function<Int, Resource<Int?>> {
                override fun apply(t: Int): Resource<Int?> {
                    if (t > 0) {
                        return Resource.success(t, UPDATE_SUCCESS)
                    } else
                        return Resource.error(0, UPDATE_FAILURE)
                }
            })
            .subscribeOn(Schedulers.io())
            .toFlowable()
    }


    @Throws(Exception::class)
    private fun checkTitle(note: Note) {
        if (note.title == null) {
            throw java.lang.Exception(NOTE_TITLE_NULL)
        }
    }
}