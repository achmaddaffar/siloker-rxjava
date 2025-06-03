package com.oliver.siloker.presentation.feature.dashboard.component

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.oliver.siloker.R

private fun getNavBarItems(context: Context) = listOf(
    NavBarItem(
        imageVector = Icons.Default.Home,
        label = context.getString(R.string.home)
    ),
    NavBarItem(
        imageVector = Icons.Default.History,
        label = context.getString(R.string.history)
    ),
    NavBarItem(
        imageVector = Icons.Default.AccountCircle,
        label = context.getString(R.string.profile)
    )
)

@Composable
fun SiLokerBottomNavBar(
    selectedContentIndex: Int,
    onSelectedContentIndexChange: (Int) -> Unit
) {
    val context = LocalContext.current

    NavigationBar {
        getNavBarItems(context).forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedContentIndex == index,
                onClick = {
                    onSelectedContentIndexChange(index)
                },
                icon = {
                    Icon(
                        imageVector = item.imageVector,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 14.sp
                    )
                }
            )
        }
    }
}