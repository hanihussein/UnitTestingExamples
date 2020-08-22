package com.hani.testing.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hani.testing.ui.note.NoteViewModel
import com.hani.testing.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel::class)
    abstract fun bindNoteViewModel(noteViewModel: NoteViewModel): ViewModel
}
