package com.my.composeapplication.base.data

/**
 * Created by YourName on 2022/09/13.
 */
data class CheckGroupState<T>(
    val itemList: List<T>,
    val checkedItems: List<T> = listOf()
)