package com.hani.testing.di

import com.hani.testing.ui.note.NoteActivity
import com.hani.testing.ui.notelist.NotesListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeNotesListActivity(): NotesListActivity

    @ContributesAndroidInjector
    abstract fun contributeNoteActivity(): NoteActivity

}