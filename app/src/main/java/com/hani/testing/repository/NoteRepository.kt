package com.hani.testing.repository

import com.hani.testing.persistance.NoteDao
import javax.inject.Inject


class NoteRepository @Inject constructor(noteDao: NoteDao) {
}