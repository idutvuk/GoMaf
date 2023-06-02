package com.idutvuk.go_maf.ui.gameview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.idutvuk.go_maf.ui.MainViewModel
import com.idutvuk.go_maf.ui.components.DefaultTopAppBar

@Composable
fun GameViewScreen(
    gameId: Long,
    viewModel: MainViewModel,
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Game view",
                onNavButtonPress = onBackClicked
            )
        }
    ) {
        val gameWithPlayers by viewModel.getGameWithPlayers(gameId).observeAsState()
        gameWithPlayers?.let { game ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                // Display game stats
                Text("Start Date: ${game.mafiaGame.startDate}")
                Text("Duration: ${game.mafiaGame.duration}")
                Text("Number of Players: ${game.mafiaGame.numPlayers}")

                // Display list of players
                Text("Players:")
                val users by remember { mutableStateOf(game.userIdList) }
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {

                    items(users) {userId ->
                        val user = viewModel.getUser(userId)
                        Text(user.username)
                    }
                }
            }
        }
    }
}