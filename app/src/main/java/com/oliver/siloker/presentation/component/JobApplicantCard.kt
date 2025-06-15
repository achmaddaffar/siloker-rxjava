package com.oliver.siloker.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.oliver.siloker.presentation.ui.theme.AppTypography
import com.oliver.siloker.rx.R

@Composable
fun JobApplicantCard(
    fullName: String,
    profilePictureUrl: String,
    status: String,
    skills: List<String>,
    experiences: List<String>,
    onResumeUrlClick: () -> Unit,
    onCvUrlClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bullet = "\u2022"
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))

    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = profilePictureUrl,
                contentDescription = stringResource(R.string.profile_picture),
                loading = {
                    Box(
                        modifier = Modifier.size(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = fullName,
                style = AppTypography.bodyLarge
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.resume),
                    style = AppTypography.bodyLarge
                )

                Text(
                    text = stringResource(R.string.link),
                    style = AppTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onResumeUrlClick()
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.cv),
                    style = AppTypography.bodyLarge
                )

                Text(
                    text = stringResource(R.string.link),
                    style = AppTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onCvUrlClick()
                    }
                )
            }

            Text(
                text = stringResource(R.string.skills),
                style = AppTypography.bodyLarge
            )
            Text(
                text = buildAnnotatedString {
                    skills.forEach {
                        withStyle(style = paragraphStyle) {
                            append(bullet)
                            append("\t\t")
                            append(it)
                        }
                    }
                },
                style = AppTypography.bodyMedium
            )

            Text(
                text = stringResource(R.string.experiences),
                style = AppTypography.bodyLarge
            )
            Text(
                text = buildAnnotatedString {
                    experiences.forEach {
                        withStyle(style = paragraphStyle) {
                            append(bullet)
                            append("\t\t")
                            append(it)
                        }
                    }
                },
                style = AppTypography.bodyMedium
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
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