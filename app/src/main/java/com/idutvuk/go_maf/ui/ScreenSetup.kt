package com.idutvuk.go_maf.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.ui.playgame.GameScreen
import com.idutvuk.go_maf.ui.gamesview.GamesScreen
import com.idutvuk.go_maf.ui.gamesview.NewGameDialog
import com.idutvuk.go_maf.ui.gameview.GameViewScreen
import java.time.LocalDate

import java.util.Calendar
import java.util.Date


@Composable
fun ScreenSetup(viewModel: MainViewModel) {
    val allGames by viewModel.allGames.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    var newGameDialogVis by remember { mutableStateOf(false) }
    var playerDialogVis by remember { mutableStateOf(false) }
    var playersCount by remember { mutableStateOf(10) }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "games_view") {
        composable("games_view") {
            GamesScreen(
                navController = navController,
                allGames = allGames,
                searchResults = searchResults,
                viewModel = viewModel,
                onFabClick = { newGameDialogVis = true }
            )
            if (newGameDialogVis) {
                NewGameDialog(
                    disableDialog = { newGameDialogVis = false },
                    startGame = {
                        newGameDialogVis = false
                        playersCount = it
                        navController.navigate("play_game")
                        viewModel.insertGame(
                            MafiaGame(
                                startDate = System.currentTimeMillis(),
                                duration = 0,
                                isOver = false,
                                numPlayers = it,
                                hostUserId = 0
                            )
                        )
                        viewModel.startGame(it)
                    },
                )
            }
        }

        composable("play_game") {
            GameScreen(
                navController = navController,
                playerCount = playersCount,
                viewModel = viewModel
            )
        }

        composable(
            route = "game_view/{gameId}",
            arguments = listOf(navArgument("gameId") {type = NavType.LongType})
        ) { backStackEntry ->

            GameViewScreen(
                gameId = backStackEntry.arguments?.getLong("gameId") ?: 0,
                viewModel = viewModel,
                onBackClicked = {navController.popBackStack()}
            )
        }
    }
}

@Preview
@Composable
fun GoMafPreview() {
    ScreenSetup(viewModel = viewModel())
}