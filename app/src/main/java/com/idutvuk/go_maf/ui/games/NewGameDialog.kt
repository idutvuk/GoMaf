package com.idutvuk.go_maf.ui.games

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun NewGameDialog(
    disableDialog: () -> Unit,
    startGame: (Int) -> Unit,
) {
    var sliderPosition by remember { mutableStateOf(10F) }
    AlertDialog(
        onDismissRequest = { disableDialog() },
        dismissButton = {
            TextButton(onClick = { disableDialog() }) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                startGame(sliderPosition.toInt())
            }) {
                Text("Start")
            }
        },
        title = { Text("New game") },
        icon = { Icon(Icons.Filled.Add, contentDescription = null) },
        text = {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Players amount")
                Text(text = sliderPosition.toInt().toString())
                Slider(
                    modifier = Modifier.semantics { contentDescription = "Localized Description" },
                    value = sliderPosition,
                    valueRange = 6f..12f, //TODO remove hardcoded maximum
                    steps = 5,
                    onValueChange = { sliderPosition = it }
                )
            }
        }

    )

}