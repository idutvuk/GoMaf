package com.idutvuk.go_maf.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.idutvuk.go_maf.ui.game.GameScreen
import com.idutvuk.go_maf.ui.games.GamesScreen
import com.idutvuk.go_maf.ui.games.NewGameDialog


@Composable
fun ScreenSetup(viewModel: MainViewModel) {
    val allGames by viewModel.allGames.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    var newGameDialogVis by remember { mutableStateOf(false) }
    var playerDialogVis by remember { mutableStateOf(false) }
    var playersCount by remember { mutableStateOf(10) }


    var screenState by remember { mutableStateOf(ScreenStatus.GAMES_VIEW) }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "games") {
        composable("games") {
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
                        navController.navigate("game")
                        viewModel.startGame(it)
                    },
                )
            }
        }
        composable("game") {
            GameScreen(
                navController = navController,
                playerCount = playersCount,
                viewModel = viewModel
            )
        }
    }
//    when (screenState) {
//        ScreenStatus.GAMES_VIEW -> {
//            GamesScreen(
//                allGames = allGames,
//                searchResults = searchResults,
//                viewModel = viewModel,
//                onFabClick = { newGameDialogVis = true }
//            )
//            if (newGameDialogVis) {
//                NewGameDialog(
//                    disableDialog = { newGameDialogVis = false },
//                    startGame = {
//                        newGameDialogVis = false
//                        playersCount = it
//                        screenState = ScreenStatus.GAME
//                        viewModel.startGame(it)
//                    },
//                )
//            }
//        }
//
//        ScreenStatus.GAME_VIEW -> TODO()
//
//        ScreenStatus.GAME -> GameScreen(
//            playersCount,
//            viewModel
//        )
//    }
}

@Preview
@Composable
fun GoMafPreview() {
    ScreenSetup(viewModel = viewModel())
}