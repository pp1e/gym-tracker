package com.example.gymtracker.utils

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.subscribe

fun <STATE : Any, MODEL : Any> Store<*, STATE, *>.asValue(mapper: (STATE) -> MODEL): Value<MODEL> {
    val mutableValue = MutableValue(mapper(state))
    states.subscribe { newState ->
        mutableValue.value = mapper(newState)
    }
    return mutableValue
}
