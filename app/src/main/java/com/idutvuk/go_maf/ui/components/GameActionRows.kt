package com.idutvuk.go_maf.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.model.gamedata.EventImportance

@Preview(
    widthDp = 300,
    heightDp = 100
)
@Composable
fun GameActionRow(
    modifier: Modifier = Modifier,
    heading: String = "Don check",
    description: String = "Ok?",
    importance: EventImportance = EventImportance.REGULAR
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier.padding(
            vertical = 5.dp,
        ),
        colors =
        if (importance == EventImportance.IMPORTANT) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 4.dp,
                    top = 8.dp,
                    bottom = 10.dp
                )
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessHigh
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = heading)
                AnimatedVisibility(
                    visible = expanded,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = description
                    )
                }
            }
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription =
                    if (expanded) {
                        "show less"
                    } else {
                        "show more"
                    }
                )

            }
        }

    }
}