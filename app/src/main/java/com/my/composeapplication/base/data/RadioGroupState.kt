package com.my.composeapplication.base.data

/**
 * Created by YourName on 2022/09/08.
 */
data class RadioGroupState<T>(
    val menuList : List<T> = listOf(),
    var selected : List<T> = listOf(),
    val isMulti : Boolean = false
) {
    fun getSelect() : List<T> {
        if (isMulti) {
            return selected.toList()
        }
        return listOf(selected[0])
    }
}
