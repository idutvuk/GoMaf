package com.idutvuk.go_maf.ui.playgame

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VoteListRow(
    voteList: List<Int>,
    modifier: Modifier = Modifier,
    selectedPlayers: List<Int> = emptyList(),
    onPlayerClick: ((Int) -> Unit)? = null
) {
    if (voteList.isEmpty()) {
        // Пустой ряд, если список пуст
        Row(
            modifier = modifier
                .fillMaxWidth()
//                .padding(vertical = 8.dp)
                ,
            horizontalArrangement = Arrangement.Center
        ) {
            // Пустое пространство
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 8.dp)
            ,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            voteList.forEach { playerIndex ->
                val isSelected = selectedPlayers.contains(playerIndex)
                OutlinedButton(
                    onClick = { onPlayerClick?.invoke(playerIndex) },
//                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    border = if (isSelected) {
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    }
                ) {
                    Text(
                        text = (playerIndex + 1).toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
