package com.idutvuk.go_maf.ui.gamesview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.model.generateRoles
import com.idutvuk.go_maf.model.gamedata.Role

@Composable
fun NewGameDialog(
    disableDialog: () -> Unit,
    startGame: (Int, Array<Role>) -> Unit,
) {
    var sliderPosition by remember { mutableStateOf(10F) }
    var roles by remember { mutableStateOf(generateRoles(10)) }
    
    fun regenerateRoles() {
        roles = generateRoles(sliderPosition.toInt())
    }
    
    AlertDialog(
        onDismissRequest = { disableDialog() },
        dismissButton = {
            TextButton(onClick = { disableDialog() }) {
                Text("Отмена")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                startGame(sliderPosition.toInt(), roles)
            }) {
                Text("Начать")
            }
        },
        title = { Text("Новая игра") },
        icon = { Icon(Icons.Filled.Add, contentDescription = null) },
        text = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Количество игроков")
                Text(text = sliderPosition.toInt().toString())
                Slider(
                    modifier = Modifier.semantics { contentDescription = "Localized Description" },
                    value = sliderPosition,
                    valueRange = 6f..12f, //TODO remove hardcoded maximum
                    steps = 5,
                    onValueChange = { 
                        sliderPosition = it
                        regenerateRoles()
                    }
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Роли",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { regenerateRoles() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Перегенерировать роли")
                    }
                }
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    roles.forEachIndexed { index, role ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}. ",
                                modifier = Modifier.width(40.dp)
                            )
                            Text(
                                text = role.emoji,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                text = when(role) {
                                    Role.CIV -> "Мирный"
                                    Role.MAF -> "Мафия"
                                    Role.SHR -> "Шериф"
                                    Role.DON -> "Дон"
                                }
                            )
                        }
                    }
                }
            }
        }

    )

}