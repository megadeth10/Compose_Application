package com.my.composeapplication.base.data

/**
 * Created by YourName on 2022/09/08.
 */
class RadioGroupState<T>(
    itemList : List<T> = listOf(),
    checkedItems : List<T> = listOf(),
    val isMulti : Boolean = false
) : BaseGroupState<T>(
    itemList = itemList,
    checkedItems = checkedItems
) {
    companion object {
        fun <T> copy(item : RadioGroupState<T>) : RadioGroupState<T> {
            return RadioGroupState(
                itemList = item.itemList,
                checkedItems = item.checkedItems,
                isMulti = item.isMulti
            )
        }
    }
}
