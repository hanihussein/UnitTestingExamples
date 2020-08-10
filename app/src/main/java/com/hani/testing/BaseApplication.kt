package com.hani.testing

import com.hani.testing.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class BaseApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}