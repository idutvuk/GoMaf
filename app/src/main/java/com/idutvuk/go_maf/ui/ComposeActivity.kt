package com.idutvuk.go_maf.ui

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.idutvuk.go_maf.ui.component.GameItemsPreview
import com.idutvuk.go_maf.ui.ui.theme.GoMafTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.ui.component.GameItemCard
import com.idutvuk.go_maf.ui.component.GameScreen
import com.idutvuk.go_maf.ui.component.GamesScreen
import com.idutvuk.go_maf.ui.component.NewGameDialog
import com.idutvuk.go_maf.ui.component.ScreenStatus.*
import com.idutvuk.go_maf.ui.ui.theme.Typography

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoMafTheme {
                val owner = LocalViewModelStoreOwner.current

                owner?.let {
                    val viewModel: MainViewModel = viewModel(
                        it,
                        "MainViewModel",
                        MainViewModelFactory(LocalContext.current.applicationContext as Application)
                    )
                    ScreenSetup(viewModel)
                }
            }
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

