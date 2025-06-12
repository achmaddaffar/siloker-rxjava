package com.oliver.siloker.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.oliver.siloker.R
import com.oliver.siloker.presentation.ui.theme.AppTypography

@Composable
fun JobApplicationCard(
    image: Any?,
    title: String,
    description: String,
    status: String,
    onClick: () -> Unit,
    onContactEmployerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SubcomposeAsyncImage(
                model = image,
                contentDescription = stringResource(R.string.job),
                loading = {
                    Box(
                        modifier = Modifier
                            .aspectRatio(16f / 9f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (status == "ACCEPTED")
                        OutlinedButton(
                            onClick = onContactEmployerClick
                        ) {
                            Text(stringResource(R.string.chat_employer))
                        }
                    else Box {}
                    Row {
                        Text(
                            text = stringResource(R.string.status_sc),
                            style = AppTypography.bodyMedium
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = status,
                            color = when (status) {
                                "PENDING" -> Color.Yellow
                                "ACCEPTED" -> Color.Green
                                "REJECTED" -> Color.Red
                                else -> Color.White
                            },
                            style = AppTypography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}