package com.idutvuk.go_maf.ui

import android.app.Application
import android.os.Bundle
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
                        MainViewModelFactory(
                            LocalContext.current.applicationContext
                                    as Application
                        )
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

    var screenState by remember { mutableStateOf(GAMES_VIEW) }
    when (screenState) {
        GAMES_VIEW -> {
            HomeScreen(
                allGames = allGames,
                searchResults = searchResults,
                viewModel = viewModel,
                onFabClick = {
                    screenState = NEW_GAME
                }
            )
        }

        GAME_VIEW -> TODO()
        PLAYER_VIEW -> TODO()
        NEW_GAME -> TODO()
        GAME -> TODO()
        GAME_END -> TODO()
    }
}

@Composable
fun HomeScreen(
    allGames: List<MafiaGame>,
    searchResults: List<MafiaGame>,
    viewModel: MainViewModel,
    onFabClick: () -> Unit
) {
    Scaffold(
        topBar = { DefaultTopAppBar() },
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
@Composable
fun GameListViewer() {
    GameItemsPreview()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    //TODO put here
    //--heading text
    //--onclick listener
) {
    TopAppBar(
        title = {
            Text(text = "Saved games",
                style = Typography.headlineLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "back"
                )
            }
        })
}


@Preview(showBackground = true)
@Composable
fun MainPreview() {
    GoMafTheme {
        GameListViewer()
    }
}
