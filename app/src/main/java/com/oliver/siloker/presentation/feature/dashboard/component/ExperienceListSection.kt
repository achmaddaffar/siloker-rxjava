package com.oliver.siloker.presentation.feature.dashboard.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.oliver.siloker.rx.R

@Composable
fun ExperienceListSection(
    experiences: List<String>,
    onValueChange: (Int, String) -> Unit,
    onDeleteExperience: (Int) -> Unit,
    onAddExperience: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        Text(stringResource(R.string.experiences) + ":")
        Spacer(Modifier.height(4.dp))
        experiences.forEachIndexed { index, skill ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("- ")
                OutlinedTextField(
                    value = skill,
                    onValueChange = {
                        onValueChange(index, it)
                    },
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                IconButton(
                    onClick = {
                        onDeleteExperience(index)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            }
            Spacer(Modifier.height(2.dp))
        }
        Button(
            onClick = onAddExperience,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.add_experience))
        }
    }
}