package com.oliver.siloker.presentation.dashboard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oliver.siloker.R
import com.oliver.siloker.presentation.ui.theme.AppTypography

@Composable
fun ProfileJobSeekerSection(
    skills: List<String>,
    experiences: List<String>,
    onResumeUrlClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bullet = "\u2022"
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 12.sp))

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = stringResource(R.string.job_seeker_information),
            style = AppTypography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
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

        OutlinedButton(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.edit_job_seeker))
        }
    }
}