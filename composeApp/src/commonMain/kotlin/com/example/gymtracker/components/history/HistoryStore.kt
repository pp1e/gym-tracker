package com.example.gymtracker.components.history

import com.arkivanov.mvikotlin.core.store.Store

internal interface HistoryStore : Store<HistoryStore.Intent, HistoryStore.State, Nothing> {
    sealed class Intent

    object State
}
