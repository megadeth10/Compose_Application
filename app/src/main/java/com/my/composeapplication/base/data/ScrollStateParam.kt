package com.my.composeapplication.base.data

/**
 * Static field, contains all scroll values
 */

final class ScrollStateParam(
    param : String,
    val index : Int,
    val scrollOffset : Int
) : BaseScrollParam(param)

