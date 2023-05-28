package com.idutvuk.go_maf.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.ui.MainViewModel
import com.idutvuk.go_maf.ui.components.DefaultTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    playerCount: Int,
    viewModel: MainViewModel,
) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val bottomSheetHeight = 230.dp

    val totalTime by remember { mutableStateOf(60L * 1000L) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableStateOf(1f) }
    var currentTime by remember { mutableStateOf(totalTime) }
    var isTimerRunning by remember { mutableStateOf(false) }

    val gameUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = bottomSheetHeight,
        topBar = { DefaultTopAppBar() },
        sheetContent = {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = REVOLVER_SCRIPT)
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                    }
                ) {
                    Text("Click to collapse sheet")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(120.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularButtonLayout(playerCount)
                    Timer(
                        size = size,
                        currentTime = currentTime,
                        value = value,
                        modifier = Modifier
                            .size(150.dp)
                            .onSizeChanged { size = it }
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bottomSheetHeight)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    onClick = { viewModel.commit() },
                ) {
                    Icon(
                        modifier = Modifier.padding(vertical = 10.dp),
                        painter = painterResource(id = gameUiState.mainBtnState.icon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(gameUiState.mainBtnState.text)

                }
                GameButtonRow(
                    onUndoClick = {},
                    onRedoClick = {},
                    onPlayClick = {
                        if (currentTime <= 0L) {
                            currentTime = totalTime
                            isTimerRunning = true
                        } else {
                            isTimerRunning = !isTimerRunning
                        }
                    },
                    onAddTimeClick = {
                        currentTime += 5L * 1000L
                    },
                    isTimerRunning = isTimerRunning,
                    isTimerActive = true
                )
            }

        }
    }
}


const val REVOLVER_SCRIPT =
        "Ави: Как ты выигрываешь?\n" +
        "Джейк: Всё очень просто. Ты делаешь основную работу, а я тебе лишь помогаю. Я должен скармливать тебе пешки, заставляя поверить, что ты сам их выиграл. Потому что ты — умён, а я, стало быть, глуп. В каждой игре всегда есть тот, кто ведёт партию и тот, кого разводят. Чем больше жертве кажется, что она ведёт игру, тем меньше она её в действительности контролирует. Так жертва затягивает на своей шее петлю, а я, как ведущий игру, ей помогаю.\n" +
        "Ави: Так что, это и есть твоя хвалёная формула?.\n" +
        "Джейк: Формула необычайно глубока по своей эффективности и области применения, но в то же время ужасно проста и абсолютно логична.\n"