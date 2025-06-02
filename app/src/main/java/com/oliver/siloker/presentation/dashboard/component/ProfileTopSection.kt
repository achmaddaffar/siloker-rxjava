package com.oliver.siloker.presentation.dashboard.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.oliver.siloker.R
import com.oliver.siloker.presentation.ui.theme.AppTypography

@Composable
fun ProfileTopSection(
    profilePictureUrl: String?,
    fullName: String,
    bio: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SubcomposeAsyncImage(
            model = profilePictureUrl,
            contentDescription = stringResource(R.string.profile_picture),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(125.dp)
                        .clip(CircleShape)
                ) {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
            },
            error = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(125.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.profile_picture),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(50.dp)
                    )
                }
            },
            modifier = Modifier
                .size(125.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = fullName,
                style = AppTypography.headlineLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = bio,
                style = AppTypography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}