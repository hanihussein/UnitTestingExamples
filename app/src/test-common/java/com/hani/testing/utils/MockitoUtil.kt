package com.hani.testing.utils

import org.mockito.Mockito

class MockitoUtil<T> {
    fun <T> anyObject(): T {
        return Mockito.any<T>()
    }
}