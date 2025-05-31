package com.oliver.siloker.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import com.oliver.siloker.presentation.ui.theme.AppTypography

@Composable
fun ResultDialog(
    title: String,
    image: Any,
    description: String,
    onDismissRequest: () -> Unit,
    primaryButtonText: String,
    onPrimaryButtonClick: () -> Unit,
    secondaryButtonText: String? = null,
    onSecondaryButtonClick: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false
    )
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = AppTypography.headlineMedium
                )
                Spacer(Modifier.height(8.dp))
                SubcomposeAsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .widthIn(150.dp, 300.dp)
                        .fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = description,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (secondaryButtonText != null && onSecondaryButtonClick != null) {
                        OutlinedButton(
                            onClick = onSecondaryButtonClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(secondaryButtonText)
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                    Button(
                        onClick = onPrimaryButtonClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(primaryButtonText)
                    }
                }
            }
        }
    }
}