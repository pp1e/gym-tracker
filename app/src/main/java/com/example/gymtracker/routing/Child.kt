package com.example.gymtracker.routing

import com.example.gymtracker.components.main.MainComponent

sealed class Child {
    data class Training(val component: MainComponent) : Child()

    data object Schedule : Child()

    data object History : Child()
}
