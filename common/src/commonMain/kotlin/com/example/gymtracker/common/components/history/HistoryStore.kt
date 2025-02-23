package com.example.gymtracker.common.components.history

import com.arkivanov.mvikotlin.core.store.Store

internal interface HistoryStore : Store<HistoryStore.Intent, HistoryStore.State, Nothing> {
    sealed class Intent

    object State
}
