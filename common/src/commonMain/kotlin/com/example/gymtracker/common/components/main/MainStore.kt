package com.example.gymtracker.common.components.main

import com.arkivanov.mvikotlin.core.store.Store

internal interface MainStore : Store<MainStore.Intent, MainStore.State, Nothing> {
    sealed class Intent

    object State
}
