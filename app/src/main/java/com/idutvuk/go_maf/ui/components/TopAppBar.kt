package com.idutvuk.go_maf.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    //TODO put here
    //--heading text
    //--onclick listener
    title: String,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = Typography.headlineLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = {navController.popBackStack()} ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {navController.navigate("rules")}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_unknown),
                    contentDescription = "rules"
                )
            }
        }
    )
}