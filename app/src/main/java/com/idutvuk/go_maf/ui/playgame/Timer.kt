package com.idutvuk.go_maf.ui.playgame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Timer(
    size: IntSize,
    value: Float,
    currentTime: Long,
    modifier: Modifier = Modifier,
    isTimerActive: Boolean,
) {
    val inactiveBarColor = MaterialTheme.colorScheme.primaryContainer
    val activeBarColor = MaterialTheme.colorScheme.primary

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = modifier.rotate(90f)) {
            drawArc(
                color = inactiveBarColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(3.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = activeBarColor,
                startAngle = 0f,
                sweepAngle = 360f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(3.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        AnimatedVisibility(
            visible = isTimerActive,
            enter = scaleIn() + fadeIn(),
            exit =  scaleOut() + fadeOut()
        ) {
            Text(
                text = (currentTime / 1000L).toString(),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
            )
        }

    }
}