package com.idutvuk.go_maf.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun NewGameDialog(
    disableDialog: () -> Unit,
    startGame: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { disableDialog() },
        dismissButton = {
            TextButton(onClick = { disableDialog() }) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = { startGame() }) {
                Text("Start")
            }
        },
        title = { Text("New game") },
        icon = { Icon(Icons.Filled.Add, contentDescription = null) }

    )

}