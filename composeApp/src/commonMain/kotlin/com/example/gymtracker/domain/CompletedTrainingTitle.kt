package com.example.gymtracker.domain

import androidx.compose.ui.graphics.Color

data class CompletedTrainingTitle(
    val name: String,
    val color: Color,
) {
    constructor(
        name: String,
        color: Color?,
    ) : this(
        name = name,
        // Brown
        color = color ?: Color(4281348142),
    )
}
