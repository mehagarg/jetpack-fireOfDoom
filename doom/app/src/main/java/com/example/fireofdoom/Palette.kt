package com.example.fireofdoom

import androidx.ui.graphics.Color

/*
This seems pretty simple - we need to create an Array of values representing every pixel on screen,
where the value represents the intensity of the fire in that instant.
In every loop, we decrement this number to simulate the fire decaying as it rises.
 So for a starting point, let’s create another Array which we’ll use to map the fire intensity
  to actual colours.
 */
val fireColors = arrayOf(
    Color(7, 7, 7),
    Color(31, 7, 7),
    Color(47, 15, 7),
    Color(71, 15, 7),
    Color(87, 23, 7),
    Color(103, 31, 7),
    Color(119, 31, 7),
    Color(143, 39, 7),
    Color(159, 47, 7),
    Color(175, 63, 7),
    Color(191, 71, 7),
    Color(199, 71, 7),
    Color(223, 79, 7),
    Color(223, 87, 7),
    Color(223, 87, 7),
    Color(215, 95, 7),
    Color(215, 95, 7),
    Color(215, 95, 7),
    Color(215, 103, 15),
    Color(207, 111, 15),
    Color(207, 119, 15),
    Color(207, 127, 15),
    Color(207, 135, 23),
    Color(199, 135, 23),
    Color(199, 143, 23),
    Color(199, 151, 31),
    Color(191, 159, 31),
    Color(191, 159, 31),
    Color(191, 167, 39),
    Color(191, 167, 39),
    Color(191, 175, 47),
    Color(183, 175, 47),
    Color(183, 183, 47),
    Color(183, 183, 55),
    Color(207, 207, 111),
    Color(223, 223, 159),
    Color(239, 239, 199),
    Color(255, 255, 255)
)