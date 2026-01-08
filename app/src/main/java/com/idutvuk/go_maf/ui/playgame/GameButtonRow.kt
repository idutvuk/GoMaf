package com.idutvuk.go_maf.ui.playgame

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.gamedata.GameTime

@Composable
fun GameButtonRow(
    onPlayClick: () -> Unit,
    onAddTimeClick: () -> Unit,
    onNextPhaseClick: () -> Unit,
    onPressFoulClick: () -> Unit,
    onPeepClick: () -> Unit,
    isTimerActive: Boolean,
    isTimerRunning: Boolean,
    currentTime: GameTime,
) {
    Row (
    ) {
        val iconButtonSize = 60.dp

//        IconButton(onClick = onPrevPhaseClick, enabled = false) {
//            if (currentTime == GameTime.DAY) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_sun),
//                    contentDescription = "back to day"
//                )
//            }
//            else {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_moon),
//                    contentDescription = "back to night"
//                )
//            }
//        }

        IconButton(
            onClick = onPlayClick,
            enabled = isTimerActive,
            modifier = Modifier.size(iconButtonSize),
        ) {
            Icon(
                painter = painterResource(id = if (isTimerRunning) R.drawable.ic_play else R.drawable.ic_pause),
                contentDescription = null
            )
        }

        IconButton(
            onClick = onAddTimeClick,
            enabled = isTimerActive,
            modifier = Modifier.size(iconButtonSize),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_timer),
                contentDescription = "Add time"
            )
        }

        IconButton(
            onClick = onPeepClick,
            modifier = Modifier.size(iconButtonSize),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_eye),
                contentDescription = "Peep"
            )
        }

        IconButton(
            onClick = onPressFoulClick,
            modifier = Modifier.size(iconButtonSize),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_foul),
                contentDescription = "foul"
            )
        }

        IconButton(
            onClick = onNextPhaseClick,
            modifier = Modifier.size(iconButtonSize),
        ) {
            if (currentTime == GameTime.NIGHT) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sun),
                    contentDescription = "jump to day"
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_moon),
                    contentDescription = "jump to night"
                )
            }
        }
    }
}