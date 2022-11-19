package com.imashnake.animite.features.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MediaStats(
    stats: List<Stat>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ) {
        stats.forEach { stat ->
            if (stat.score != null && stat.label != StatLabel.UNKNOWN)
                Stat(
                    label = stat.label.value,
                    score = stat.score
                ) {
                    when(stat.label) {
                        StatLabel.SCORE -> "$it%"
                        StatLabel.RATING, StatLabel.POPULARITY -> "#$it"
                        else -> ""
                    }
                }
        }
    }
}

@Composable
fun Stat(label: String, score: Int, format: (Int) -> String) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelSmall
        )

        Text(
            text = format(score),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.displaySmall
        )
    }
}
