package com.hani.testing.di

import com.hani.testing.NotesListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeNotesListActivity():NotesListActivity
}