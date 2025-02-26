package com.example.gymtracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.UiConstants

private val TRAINING_TITLE_PADDING_VALUES =
    PaddingValues(
        vertical = 20.dp,
    )

@Composable
fun ColumnScope.TrainingTitle(title: String) {
    Column(
        modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
//            .padding(TRAINING_TITLE_PADDING_VALUES)
                .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
    ) {
        BasicTextField(
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(TRAINING_TITLE_PADDING_VALUES),
            //                .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
            value = title,
            onValueChange = {},
            textStyle =
                TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        )

//        Text(
//            text = "Понедельник, 17 февраля",
//            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
//        )

//        HorizontalDivider()
    }
}
