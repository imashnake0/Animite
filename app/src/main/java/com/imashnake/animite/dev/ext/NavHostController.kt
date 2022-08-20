package com.imashnake.animite.dev.ext

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.imashnake.animite.features.appCurrentDestinationAsState
import com.imashnake.animite.features.navigationbar.NavigationBarPaths

@Composable
fun NavHostController.isPath(): Boolean {
    return NavigationBarPaths.values().map { it.direction }.any {
        this.appCurrentDestinationAsState().value == it
    }
}
