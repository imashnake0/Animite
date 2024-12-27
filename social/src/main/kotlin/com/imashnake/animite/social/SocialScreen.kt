package com.imashnake.animite.social

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.navigation.NavigationBarPaths
import com.imashnake.animite.navigation.NavigationScaffold
import com.imashnake.animite.core.R as coreR

@Composable
fun SocialScreen(
    navController: NavHostController
) {
    NavigationScaffold(
        navController = navController,
        selectedDestination = NavigationBarPaths.Social
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                LocalPaddings.current.tiny, Alignment.CenterVertically
            )
        ) {
            Text(
                text = stringResource(coreR.string.coming_soon),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
