package com.example.gymtracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CARD_HEIGHT = 80.dp

private val ENTRY_PADDING =
    PaddingValues(
        vertical = 10.dp,
    )
private val CARD_ELEMENT_PADDING_VALUES =
    PaddingValues(
        horizontal = 10.dp,
    )

@Composable
fun CompletedTrainingEntry(
    modifier: Modifier = Modifier,
    onClicked: (Long) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .then(modifier)
                .padding(ENTRY_PADDING)
                .height(CARD_HEIGHT)
                .fillMaxWidth()
                .clickable { onClicked(1) },
        colors = CardDefaults.outlinedCardColors(),
    ) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.Start)
                    .weight(5f)
                    .padding(CARD_ELEMENT_PADDING_VALUES),
        ) {
            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = "Грудь + ноги",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.Start)
                    .weight(4f)
                    .padding(CARD_ELEMENT_PADDING_VALUES),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Понедельник, 17 февраля",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }
    }
}
