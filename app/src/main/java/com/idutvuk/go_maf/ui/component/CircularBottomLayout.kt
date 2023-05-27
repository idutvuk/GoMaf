package com.idutvuk.go_maf.ui.component

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Preview(
widthDp = 800,
heightDp = 800
)
@Composable
fun CircularButtonLayout(buttonCount: Int = 10) {
    val angleOffset = Math.toRadians(60.0)
    val angles: ArrayList<Float> = ArrayList(buttonCount)
    for (i in 0 until buttonCount) {
        angles.add(((2 * Math.PI - angleOffset) / (buttonCount - 1) * i + angleOffset / 2).toFloat())
    }

    repeat(buttonCount) { index ->
        TextButton(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .size(60.dp)
                .offset {
                    val radius = 140.dp.toPx()
                    val x = (-radius * sin(angles[index])).toInt()
                    val y = (radius * cos(angles[index])).toInt()
                    IntOffset(x, y)
                }
        ) {
            Text(
                text = "$index",
                fontSize = if (index < 10) 30.sp else 25.sp
            )
        }
    }
}



