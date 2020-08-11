package com.hani.testing.di

import android.app.Application
import androidx.room.Room
import com.hani.testing.persistance.NoteDao
import com.hani.testing.persistance.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object AppModule {
    @JvmStatic
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
    @JvmStatic
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.getNoteDao()
    }
}