package com.example.gymtracker.ui.elements

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gymtracker.domain.CompletedTrainingShort

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
    completedTraining: CompletedTrainingShort,
    onClicked: (Long) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .then(modifier)
                .padding(ENTRY_PADDING)
                .height(CARD_HEIGHT)
                .fillMaxWidth()
                .clickable { onClicked(completedTraining.id) },
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
                text = completedTraining.name,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
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
                text = formatDatetime(completedTraining.startedAt),
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }
    }
}
