package com.idutvuk.go_maf.ui.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun CircularButtonLayout(
    buttonCount: Int = 10,
    angles: ArrayList<Float>,
    onButtonClick: (Int) -> Unit,
    selectedPlayers: ArrayList<Int>,
    livingPlayers: List<Boolean>
) {
//    val angleOffset = Math.toRadians(60.0)
//    val angles: ArrayList<Float> = ArrayList(buttonCount)
//    for (i in 0 until buttonCount) {
//        angles.add(((2 * Math.PI - angleOffset) / (buttonCount - 1) * i + angleOffset / 2).toFloat())
//    }

    repeat(buttonCount) { index ->
        OutlinedButton(
            onClick = { onButtonClick(index) },
            border = if (selectedPlayers.contains(index)) {
                BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            } else {
                BorderStroke(0.dp, Color.Transparent)
            },
            modifier = Modifier
                .size(60.dp)
                .offset {
                    val radius = 140.dp.toPx()
                    val x = (-radius * sin(angles[index])).toInt()
                    val y = (radius * cos(angles[index])).toInt()
                    IntOffset(x, y)
                },
            enabled = livingPlayers[index]
        ) {
            Text(
                text = "$index",
                fontFamily = FontFamily.Cursive,
                fontSize = if (index < 10) 30.sp else 25.sp
            )
        }
    }
}



