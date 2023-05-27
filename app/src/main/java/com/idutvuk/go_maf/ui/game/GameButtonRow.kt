package com.idutvuk.go_maf.ui.game

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun GameButtonRow(
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onPlayClick: () -> Unit,
    onAddTimeClick: () -> Unit,
    isTimerRunning: Boolean,
    isTimerActive: Boolean,
) {
    Row {
        IconButton(onClick = onUndoClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "back")
        }

        IconButton(onClick = onRedoClick) {
            Icon(Icons.Default.ArrowForward, contentDescription = "play/pause")
        }

        IconButton(
            onClick = onPlayClick,
            enabled = isTimerActive
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Add time")
        }

        IconButton(
            onClick = onAddTimeClick,
            enabled = isTimerActive
            ) {
            Icon(Icons.Default.Add, contentDescription = "forward")

        }
    }
}