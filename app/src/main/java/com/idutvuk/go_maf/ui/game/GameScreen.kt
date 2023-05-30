package com.idutvuk.go_maf.ui.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarDuration
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
import androidx.navigation.NavHostController
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.gamedata.EventImportance
import com.idutvuk.go_maf.ui.MainViewModel
import com.idutvuk.go_maf.ui.components.DefaultTopAppBar
import com.idutvuk.go_maf.ui.components.GameActionRow
import kotlinx.coroutines.delay
import kotlin.math.PI

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GameScreen(
    playerCount: Int,
    viewModel: MainViewModel,
    navController: NavHostController,
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

    var isPlayerRolesShown by remember { mutableStateOf(false) }
    var isWaitingForFoul by remember { mutableStateOf(false) }

    val angles by remember { mutableStateOf(generateAngles(playerCount)) }

    val lazyListState by remember { mutableStateOf(LazyListState()) }

    var isVoteListShown by remember { mutableStateOf(false) }
    LaunchedEffect(gameUiState.voteList) {
        isVoteListShown = !(gameUiState.voteList.isNullOrEmpty())
    }

    val cursorAngle by animateFloatAsState(
        targetValue = 180F + angles[gameUiState.cursor] * (180F / PI.toFloat()),
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

    LaunchedEffect(gameUiState.snackbarMessage) {
        if (gameUiState.snackbarMessage != null && gameUiState.snackbarMessage != "")
            scaffoldState.snackbarHostState.showSnackbar(
                message = gameUiState.snackbarMessage!!,
                actionLabel = "Ok",
                duration = SnackbarDuration.Short
            )
    }

    LaunchedEffect(gameUiState) {
        lazyListState.scrollToItem(0)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = bottomSheetHeight,
        topBar = { DefaultTopAppBar(
            title = "Game",
            onNavButtonPress = { navController.popBackStack() }
        )
                 },
        sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "DEBUG: selected players: ${gameUiState.selectedPlayers}\n" +
                            "votelist: ${gameUiState.voteList}\n" +
                            "selmode:  ${gameUiState.selectionMode}\n" +
                            "main Btn state ${gameUiState.mainBtnState}\n"
                )
                Spacer(Modifier.height(20.dp))
                LazyColumn(
                    reverseLayout = true,
                    state = lazyListState
                ) {
                    items(gameUiState.snapshotHistory) { snapshot ->
                        if (snapshot.importance != EventImportance.SILENT)
                            GameActionRow(
                                modifier = Modifier.animateItemPlacement(),
                                heading = snapshot.heading,
                                description = snapshot.description,
                                importance = snapshot.importance
                            )
                    }
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
                            if(isWaitingForFoul) {
                                viewModel.foul(index)
                                isWaitingForFoul = false
                            } else {
                                viewModel.clickButton(index)
                            }
                        },
                        selectedPlayers = gameUiState.selectedPlayers,
                        livingPlayers = gameUiState.livingPlayers,
                        isPlayerRolesShown = isPlayerRolesShown,
                        roles = gameUiState.players.map {it.role},
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
                    onClick = { isWaitingForFoul = false; viewModel.commit() },
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
                    canUndo = gameUiState.canUndo,
                    onUndoClick = {
                        viewModel.onPressUndoBtn()
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
                    currentTime = gameUiState.time,
                    onPrevPhaseClick = {},
                    onNextPhaseClick = {
                        viewModel.nextPhase()
                    },
                    onPressFoulClick = {

                    },
                    onPeepClick = {
                        isPlayerRolesShown = !isPlayerRolesShown
                    }
                )
            }

        }
    }
}

const
val TIMER_RADIUS = 75

fun generateAngles(buttonCount: Int): ArrayList<Float> {
    val angleOffset = Math.toRadians(60.0)
    val angles: ArrayList<Float> = ArrayList(buttonCount)
    for (i in 0 until buttonCount) {
        angles.add(((2 * Math.PI - angleOffset) / (buttonCount - 1) * i + angleOffset / 2).toFloat())
    }
    return angles
}