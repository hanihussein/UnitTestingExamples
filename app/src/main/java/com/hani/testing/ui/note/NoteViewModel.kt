package com.hani.testing.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hani.testing.models.Note
import com.hani.testing.repository.NoteRepository
import com.hani.testing.ui.Resource
import com.hani.testing.utils.DateUtil
import org.reactivestreams.Subscription
import javax.inject.Inject

class NoteViewModel @Inject constructor(var noteRepository: NoteRepository) : ViewModel() {
    enum class ViewState {
        VIEW, EDIT
    }

    companion object {
        val NO_CONTENT_ERROR = "Can't save note with no content"
    }

    private val _noteLiveData = MutableLiveData<Note>()
    var noteLiveData: LiveData<Note> = _noteLiveData

    private val viewState = MutableLiveData<ViewState>()
    private var isNewNote = false
    private var updateSubscription: Subscription? = null
    private var insertSubscription: Subscription? = null


    fun insertNote(): LiveData<Resource<Int?>> {
        return LiveDataReactiveStreams.fromPublisher(
            noteRepository.insertNote(_noteLiveData.value!!)
                .doOnSubscribe({ subscription ->
                    insertSubscription = subscription
                })
        )
    }

    @Throws(java.lang.Exception::class)
    fun updateNote(): LiveData<Resource<Int?>> {
        return LiveDataReactiveStreams.fromPublisher(
            noteRepository.updateNote(_noteLiveData.value!!)
                .doOnSubscribe({ subscription ->
                    updateSubscription = subscription
                })
        )
    }

    @Throws(Exception::class)
    public fun saveNote(): LiveData<Resource<Int?>> {

        if (shouldAllowSave()) {
            throw Exception(NO_CONTENT_ERROR)
        }
        cancelPendingTransactions()

        return object : NoteInsertUpdateHelper<Int>() {

            override fun setNoteId(noteId: Int) {
                val note = _noteLiveData.value
                note?.id = noteId
                isNewNote = false
                _noteLiveData.value = note
            }

            override fun getAction(): LiveData<Resource<Int?>> {
                if (isNewNote) {
                    return insertNote();
                } else {
                    return updateNote()
                }
            }

            override fun defineAction(): String {
                if (isNewNote) {
                    return ACTION_INSERT;
                } else {
                    return ACTION_UPDATE
                }
            }

            override fun onTransactionComplete() {
                updateSubscription = null
                insertSubscription = null
            }

        }.asLiveData
    }

    private fun cancelPendingTransactions() {
        if (insertSubscription != null) {
            cancelInsertTransaction()
        }
        if (updateSubscription != null) {
            cancelUpdateTransaction()
        }
    }

    @Throws(java.lang.Exception::class)
    private fun shouldAllowSave(): Boolean {
        return try {
            removeWhiteSpace(_noteLiveData.getValue()!!.content!!).length > 0
        } catch (e: java.lang.NullPointerException) {
            throw java.lang.Exception(NoteViewModel.NO_CONTENT_ERROR)
        }
    }

    private fun cancelUpdateTransaction() {
        updateSubscription?.cancel()
        updateSubscription = null
    }

    private fun cancelInsertTransaction() {
        insertSubscription?.cancel()
        insertSubscription = null
    }


    fun observeViewState(): LiveData<ViewState> {
        return viewState
    }

    fun setViewState(viewState: ViewState) {
        this.viewState.value = viewState
    }

    fun setIsNewNote(isNewNote: Boolean) {
        this.isNewNote = isNewNote
    }

    @Throws(java.lang.Exception::class)
    fun updateNote(title: String, content: String) {
        if (title == null || title == "") {
            throw NullPointerException("Title can't be null")
        }
        val temp: String = removeWhiteSpace(content)
        if (temp.length > 0) {
            val updatedNote = Note(_noteLiveData.value!!)
            updatedNote.title = title
            updatedNote.content = content
            updatedNote.timestamp = DateUtil.currentTimeStamp
            _noteLiveData.value = (updatedNote)
        }
    }

    @Throws(Exception::class)
    fun setNote(note: Note) {
        if (note.title == null || note.title.equals(""))
            throw Exception(NoteRepository.INSERT_FAILURE)
        _noteLiveData.value = note
    }

    private fun removeWhiteSpace(string: String): String {
        var string = string
        string = string.replace("\n", "")
        string = string.replace(" ", "")
        return string
    }

    fun shouldNavigateBack(): Boolean {
        return if (viewState.value == ViewState.VIEW) {
            true
        } else {
            false
        }
    }
}