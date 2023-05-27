package com.idutvuk.go_maf.ui

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.idutvuk.go_maf.ui.game.GameScreen
import com.idutvuk.go_maf.ui.games.GamesScreen
import com.idutvuk.go_maf.ui.games.NewGameDialog
import com.idutvuk.go_maf.ui.ScreenStatus.*

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            GoMafTheme {
                val owner = LocalViewModelStoreOwner.current

                owner?.let {
                    val viewModel: MainViewModel = viewModel(
                        it,
                        "MainViewModel",
                        MainViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                    ScreenSetup(viewModel)
                }
//            }
        }
    }
}


@Composable
fun ScreenSetup(viewModel: MainViewModel) {
    val allGames by viewModel.allGames.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    var newGameDialogVis by remember { mutableStateOf( false ) }
    var playerDialogVis by remember { mutableStateOf( false ) }
    var playersCount by remember { mutableStateOf( 10 ) }


    var screenState by remember { mutableStateOf(GAMES_VIEW) }
    when (screenState) {
        GAMES_VIEW -> {
            GamesScreen(
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
                        screenState = GAME
                    },
                )
            }
        }

        GAME_VIEW -> TODO()

        GAME -> GameScreen(
            playersCount,
            viewModel
        )
    }
}

