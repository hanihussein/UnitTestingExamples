package com.hani.testing.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hani.testing.persistance.NoteDao
import com.hani.testing.persistance.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {


    @Singleton
    @Provides
    fun provideNoteDatabase(application: Application): NoteDatabase {

        return Room.databaseBuilder(
            application,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }


    @Singleton
    @Provides
    fun provideNoteDae(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.getNoteDae()
    }
}
