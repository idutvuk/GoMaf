package com.idutvuk.go_maf.ui.playgame

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idutvuk.go_maf.model.gamedata.Role
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun CircularButtonLayout(
    buttonCount: Int = 10,
    angles: ArrayList<Float>,
    onButtonClick: (Int) -> Unit,
    selectedPlayers: ArrayList<Int>,
    livingPlayers: List<Boolean>,
    isPlayerRolesShown: Boolean,
    roles: List<Role>,
    isWaitingForClick: Boolean = false,
    fouls: List<Int> = emptyList(),
    firstSpokedPlayer: Int = 0,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isWaitingForClick) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(50, 0),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shakeOffset"
    )
//    val angleOffset = Math.toRadians(60.0)
//    val angles: ArrayList<Float> = ArrayList(buttonCount)
//    for (i in 0 until buttonCount) {
//        angles.add(((2 * Math.PI - angleOffset) / (buttonCount - 1) * i + angleOffset / 2).toFloat())
//    }

    repeat(buttonCount) { index ->
        val shouldShake = isWaitingForClick && livingPlayers[index]
        val phase = index * 100f
        val shakeValue = if (shouldShake) {
            (shakeOffset - 0.5f) * 2f + phase
        } else {
            0f
        }
        val shakeX = if (shouldShake) {
            sin(shakeValue * PI.toFloat()) * 6f
        } else {
            0f
        }
        val shakeY = if (shouldShake) {
            cos(shakeValue * PI.toFloat()) * 6f
        } else {
            0f
        }
        
        Box(
            modifier = Modifier
                .offset { // radial offset off center
                    val radius = 140.dp.toPx()
                    val baseX = (-radius * sin(angles[index])).toInt()
                    val baseY = (radius * cos(angles[index])).toInt()
                    IntOffset(
                        baseX + shakeX.toInt(),
                        baseY + shakeY.toInt()
                    )
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // first speaking player mark
                if (index == firstSpokedPlayer) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .offset(x = 0.dp, y = 12.dp)
                            .clip(CircleShape)
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                    )
                }
                OutlinedButton(
                    onClick = { onButtonClick(index) },
                    border = if (selectedPlayers.contains(index)) {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        BorderStroke(0.dp, Color.Transparent)
                    },
                    modifier = Modifier.size(65.dp),
                    enabled = livingPlayers[index],
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text(
                        text = (index+1).toString(),
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        softWrap = false,
                        fontSize = 24.sp,
                        modifier = Modifier.wrapContentSize()
                    )
                }
                
                // fouls
                if (index < fouls.size && fouls[index] > 0) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        repeat(fouls[index]) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = MaterialTheme.colorScheme.error,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }

        if (isPlayerRolesShown) {
            Text(
                modifier = Modifier
                    .size(20.dp)
                    .offset {
                        val radius = 110.dp.toPx()
                        val x = (-radius * sin(angles[index])).toInt()
                        val y = (radius * cos(angles[index])).toInt()
                        IntOffset(x, y)
                    },
                text = roles[index].emoji,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp,
            )
        }
    }
}



