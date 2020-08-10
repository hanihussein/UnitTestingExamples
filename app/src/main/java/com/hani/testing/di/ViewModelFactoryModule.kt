package com.hani.testing.di

import androidx.lifecycle.ViewModelProvider
import com.hani.testing.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(providerFactory: ViewModelProviderFactory) : ViewModelProvider.Factory
}
