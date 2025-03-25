package com.example.bhagwadgitachatbot

sealed class BottomNav(
    val activeIcon: Int,
    val disableIcon: Int,
    val title: String,
    val route: String
) {
    object Screen1 : BottomNav(
        activeIcon = R.drawable.img,
        disableIcon = R.drawable.img_1,
        title = "Home",
        route = "home"
    )

    object Screen2 : BottomNav(
        activeIcon = R.drawable.userfilled,
        disableIcon = R.drawable.user,
        title = "Settings",
        route = "location"
    )

}