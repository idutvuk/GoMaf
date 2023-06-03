package com.idutvuk.go_maf.ui.gamesview

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.ui.components.DefaultTopAppBar
import com.idutvuk.go_maf.ui.MainViewModel
import java.sql.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    allGames: List<MafiaGame>,
    searchResults: List<MafiaGame>,
    viewModel: MainViewModel,
    onFabClick: () -> Unit,
    navController: NavHostController
) {
    Scaffold(
        topBar = { DefaultTopAppBar(
            title = "Saved Games",
            navController
        )
                 },
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
                val dismissState = rememberDismissState(
                    confirmValueChange = {
                        viewModel.deleteGame(game.id)
                        true
                    }
                )
                SwipeToDismiss(
                    state = dismissState,
                    background = { SwipeBackground(dismissState = dismissState) },
                    dismissContent = {
                        GameItemCard(
                            game = game,
                            onItemClicked = {gameId ->

                                navController.navigate("game_view/$gameId")
                            }
                        )
                    })

            }
        }
    }
}