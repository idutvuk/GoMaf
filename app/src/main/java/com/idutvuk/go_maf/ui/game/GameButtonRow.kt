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
import com.idutvuk.go_maf.model.gamedata.GameTime

@Composable
fun GameButtonRow(
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onPlayClick: () -> Unit,
    onAddTimeClick: () -> Unit,
    onPrevPhaseClick: () -> Unit,
    onNextPhaseClick: () -> Unit,
    isTimerActive: Boolean,
    isTimerRunning: Boolean,
    canUndo: Boolean,
    currentTime: GameTime,
) {
    Row {
        IconButton(onClick = { onPrevPhaseClick}) {
            if (currentTime == GameTime.DAY) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sun),
                    contentDescription = "back to day"
                )
            }
            else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_moon),
                    contentDescription = "back to night"
                )
            }
        }
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
                    painter = painterResource(id = R.drawable.ic_play),
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

        IconButton(onClick = { onNextPhaseClick}) {
            if (currentTime == GameTime.NIGHT) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sun),
                    contentDescription = "jump to day"
                )
            }
            else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_moon),
                    contentDescription = "jump to night"
                )
            }
        }
    }
}