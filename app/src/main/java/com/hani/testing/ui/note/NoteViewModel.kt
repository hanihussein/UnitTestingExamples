package com.hani.testing.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hani.testing.models.Note
import com.hani.testing.repository.NoteRepository
import com.hani.testing.ui.Resource
import javax.inject.Inject

class NoteViewModel @Inject constructor(var repository: NoteRepository) : ViewModel() {

    private val _noteLiveData = MutableLiveData<Note>()

    var noteLiveData: LiveData<Note> = _noteLiveData

    fun insertNote(): LiveData<Resource<Int?>> {
        return LiveDataReactiveStreams.fromPublisher(repository.insertNote(_noteLiveData.value!!))
    }


    @Throws(Exception::class)
    fun setNote(note: Note) {
        if (note.title == null || note.title.equals(""))
            throw Exception(NoteRepository.INSERT_FAILURE)
        _noteLiveData.value = note
    }
}