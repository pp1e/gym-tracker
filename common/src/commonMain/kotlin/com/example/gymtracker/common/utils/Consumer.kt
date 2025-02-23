package com.example.gymtracker.common.utils

import com.badoo.reaktive.base.Consumer

// Factory function
inline fun <T> Consumer(crossinline block: (T) -> Unit): Consumer<T> =
    object : Consumer<T> {
        override fun onNext(value: T) {
            block(value)
        }
    }
