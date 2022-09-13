package com.my.composeapplication.base.data

/**
 * Created by YourName on 2022/09/13.
 */
class CheckGroupState<T>(
    itemList : List<T>,
    checkedItems : List<T> = listOf()
) : BaseGroupState<T>(
    itemList = itemList,
    checkedItems = checkedItems
)