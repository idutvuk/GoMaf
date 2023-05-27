package com.idutvuk.go_maf.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.idutvuk.go_maf.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    //TODO put here
    //--heading text
    //--onclick listener
) {
    TopAppBar(
        title = {
            Text(
                text = "Saved games",
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