package core.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator() {
    LinearProgressIndicator(
        modifier = Modifier
            .width(100.dp)
            .height(3.dp),
        strokeCap = StrokeCap.Round
    )
}
