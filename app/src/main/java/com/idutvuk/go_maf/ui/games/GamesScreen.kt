package com.idutvuk.go_maf.ui.games

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.ui.components.DefaultTopAppBar
import com.idutvuk.go_maf.ui.MainViewModel

@Composable
fun GamesScreen(
    allGames: List<MafiaGame>,
    searchResults: List<MafiaGame>,
    viewModel: MainViewModel,
    onFabClick: () -> Unit
) {
    Scaffold(
        topBar = { DefaultTopAppBar("Saved Games") },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabClick) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "add"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {

            items(allGames) { game ->
                GameItemCard(game = game, onItemClicked = {})
            }
        }
    }
}