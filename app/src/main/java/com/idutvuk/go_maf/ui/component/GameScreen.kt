package com.idutvuk.go_maf.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.ui.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    playerCount: Int,
    viewModel: MainViewModel
) {
//    Scaffold(
//        topBar = { DefaultTopAppBar() }
//    ) {
//        Box(modifier = Modifier.padding(it)) {
//
//        }
//    }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val bottomSheetHeight = 160.dp

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = bottomSheetHeight,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bottomSheetHeight),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Debug state")

                }
                Row {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "play/pause")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Add, contentDescription = "Add time")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "forward")
                        
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sheet content")
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                    }
                ) {
                    Text("Click to collapse sheet")
                }
            }
        }) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            Text("TODO players here")
        }
    }

}