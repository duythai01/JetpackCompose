package com.example.jetpackcomposeapp.presentation.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposeapp.R
import com.example.jetpackcomposeapp.ui.theme.Black40

@Composable
fun Categories() {
    val categories = listOf(
        R.string.explore to Icons.Outlined.Explore,
        R.string.all to null,
        R.string.gaming to null,
        R.string.sports to null,
        R.string.music to null,
        R.string.anime to null
    )

    var selectedIndex by remember { mutableIntStateOf(1) }

    LazyRow(
        contentPadding = PaddingValues(
            start = 15.dp,
            end = 15.dp,
            bottom = 6.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        itemsIndexed(categories) { index, (textRes, icon) ->
            val isSelected = index == selectedIndex
            ElevatedFilterChip(
                selected = isSelected,
                onClick = { selectedIndex = index },
                label = {
                    Text(text = stringResource(id = textRes))
                },
                leadingIcon = {
                    if (icon != null) {
                        Icon(
                            imageVector = Icons.Outlined.Explore,
                            contentDescription = null,
                            tint = if(isSelected) Black40 else Color.White
                        )
                    }
                },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    selectedContainerColor = if (isSelected) Color.White else Color.DarkGray,
                    selectedLabelColor = if (isSelected) Black40 else Color.White,
                    containerColor = Color.DarkGray
                ),
                shape = if(icon != null) RoundedCornerShape(8.dp) else CircleShape
            )
        }
    }
}