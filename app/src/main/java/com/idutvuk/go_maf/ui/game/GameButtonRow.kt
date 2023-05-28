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
import androidx.compose.ui.res.painterResource
import com.idutvuk.go_maf.R

@Composable
fun GameButtonRow(
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onPlayClick: () -> Unit,
    onAddTimeClick: () -> Unit,
    isTimerActive: Boolean,
    isTimerRunning: Boolean,
    canUndo: Boolean,
) {
    Row {
        IconButton(
            onClick = onUndoClick,
            enabled = canUndo
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "back")
        }

        IconButton(
            onClick = onRedoClick,
            enabled = false //todo enable
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = "forward")
        }

        IconButton(
            onClick = onPlayClick,
            enabled = isTimerActive
        ) {
            if (isTimerRunning) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
            else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pause),
                    contentDescription = null
                )
            }
        }

        IconButton(
            onClick = onAddTimeClick,
            enabled = isTimerActive
            ) {
            Icon(Icons.Default.Add, contentDescription = "forward")
        }
    }
}