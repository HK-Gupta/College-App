package com.example.collegeapp.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.collegeapp.navigation.Routes

data class NavItem(
    val title: String,
    val icon: ImageVector
)

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val routes: String
)
