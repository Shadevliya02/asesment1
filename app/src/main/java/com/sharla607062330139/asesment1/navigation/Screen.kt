package com.sharla607062330139.asesment1.navigation

sealed class Screen(val route: String) {
    object Home : Screen("mainScreen")
    object About : Screen("aboutScreen")
}
