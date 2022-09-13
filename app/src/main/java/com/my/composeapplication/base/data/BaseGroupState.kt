package com.my.composeapplication.base.data

/**
 * Created by YourName on 2022/09/13.
 */
open class BaseGroupState<T>(
    val itemList: List<T>,
    var checkedItems: List<T> = listOf()
)
