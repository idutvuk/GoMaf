package com.idutvuk.go_maf.ui.game

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.ui.MainViewModel
import com.idutvuk.go_maf.ui.components.DefaultTopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI

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
    var timerCircularBarValue by remember { mutableStateOf(1f) }
    var currentTime by remember { mutableStateOf(totalTime) }

    val gameUiState by viewModel.uiState.collectAsState()

    var isTimerRunning by remember { mutableStateOf(true) }

    val angles by remember { mutableStateOf(generateAngles(playerCount)) }

    val cursorAngle by animateFloatAsState(
        targetValue = 180F + angles[gameUiState.cursor ?: 0] * (180F/ PI.toFloat()),
        label = "cursorAnim"
    )

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning, key3 = gameUiState.isTimerActive) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            timerCircularBarValue = currentTime / totalTime.toFloat()
        }
    }

    LaunchedEffect(gameUiState.isTimerActive) {
        timerCircularBarValue = 1F
        isTimerRunning = gameUiState.isTimerActive
        if (!gameUiState.isTimerActive) {
            delay(200L)
            currentTime = 60L * 1000L
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = bottomSheetHeight,
        topBar = { DefaultTopAppBar("Game") },
        sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "DEBUG: selected players: ${gameUiState.selectedPlayers}\n" +
                            "selmode:  ${gameUiState.selectionMode}\n" +
                            "main Btn state ${gameUiState.mainBtnState}\n"
                )
                Spacer(Modifier.height(20.dp))
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

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(140.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularButtonLayout(
                        buttonCount = playerCount,
                        angles = angles,
                        onButtonClick = { index ->
                            viewModel.clickButton(index)
                        },
                        selectedPlayers = gameUiState.selectedPlayers,
                        livingPlayers = gameUiState.livingPlayers
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow),
                        modifier = Modifier
                            .size(173.dp)
                            .rotate(cursorAngle),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Timer(
                        size = size,
                        currentTime = currentTime,
                        value = timerCircularBarValue,
                        modifier = Modifier
                            .size((TIMER_RADIUS * 2).dp)
                            .onSizeChanged { size = it },
                        isTimerActive = gameUiState.isTimerActive
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
                    Text(text = gameUiState.mainBtnState.text)

                }
                GameButtonRow(
                    onUndoClick = {
                        viewModel.undo()
                    },
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
                    isTimerActive = gameUiState.isTimerActive,
                    isTimerRunning = isTimerRunning,
                    canUndo = gameUiState.canUndo
                )
            }

        }
    }
}

const val TIMER_RADIUS = 75

fun generateAngles(buttonCount: Int): ArrayList<Float> {
    val angleOffset = Math.toRadians(60.0)
    val angles: ArrayList<Float> = ArrayList(buttonCount)
    for (i in 0 until buttonCount) {
        angles.add(((2 * Math.PI - angleOffset) / (buttonCount - 1) * i + angleOffset / 2).toFloat())
    }
    return angles
}

const val REVOLVER_SCRIPT =
        "Ави: Как ты выигрываешь?\n" +
        "Джейк: Всё очень просто. Ты делаешь основную работу, а я тебе лишь помогаю. Я должен скармливать тебе пешки, заставляя поверить, что ты сам их выиграл. Потому что ты — умён, а я, стало быть, глуп. В каждой игре всегда есть тот, кто ведёт партию и тот, кого разводят. Чем больше жертве кажется, что она ведёт игру, тем меньше она её в действительности контролирует. Так жертва затягивает на своей шее петлю, а я, как ведущий игру, ей помогаю.\n" +
        "Ави: Так что, это и есть твоя хвалёная формула?.\n" +
        "Джейк: Формула необычайно глубока по своей эффективности и области применения, но в то же время ужасно проста и абсолютно логична.\n"