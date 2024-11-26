package me.padamchopra.numbermatch.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import me.padamchopra.numbermatch.ui.appColors
import me.padamchopra.numbermatch.ui.dimensions
import numbermatch.composeapp.generated.resources.Res
import numbermatch.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

object SplashScreen {
    @Composable
    operator fun invoke() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.padding100, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .sizeIn(
                        maxWidth = 150.dp,
                    )
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.appColors.tileBackground),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "1",
                    color = MaterialTheme.appColors.blue,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
