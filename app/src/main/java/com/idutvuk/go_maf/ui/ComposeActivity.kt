package com.idutvuk.go_maf.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.ui.component.ItemPreview
import com.idutvuk.go_maf.ui.ui.theme.GoMafTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoMafTheme {
                GameListViewer()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListViewer() {
    ItemPreview()
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
            Text("Saved games")
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
