package com.example.gymtracker.utils

import androidx.lifecycle.AtomicReference
import com.badoo.reaktive.disposable.Disposable

fun AtomicReference<Map<Long, Disposable>>.add(
    key: Long,
    value: Disposable,
) {
    while (true) {
        val current = this.get()
        val updated =
            current.toMutableMap().apply {
                this[key]?.dispose()
                this[key] = value
            }
        if (this.compareAndSet(current, updated)) break
    }
}

fun AtomicReference<Map<Long, Disposable>>.remove(key: Long) {
    while (true) {
        val current = this.get()
        current[key]?.dispose()
        val updated = current.minus(key)
        if (this.compareAndSet(current, updated)) break
    }
}
