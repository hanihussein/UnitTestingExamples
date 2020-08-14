package com.hani.testing.di

import android.app.Application
import androidx.room.Room
import com.hani.testing.repository.NoteRepository
import com.hani.testing.persistance.NoteDao
import com.hani.testing.persistance.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideNoteDatabase(application: Application?): NoteDatabase {
        return Room.databaseBuilder(
            application!!,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.getNoteDao()
    }

    @Singleton
    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepository(noteDao)
    }
}