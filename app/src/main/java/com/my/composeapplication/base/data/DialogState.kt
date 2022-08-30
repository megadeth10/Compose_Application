package com.my.composeapplication.base.data

import android.content.Context
import com.my.composeapplication.R

/**
 * Created by YourName on 2022/08/29.
 */

// 다이얼로그 종류
enum class DialogType(value : Int) {
    SimpleDialog(0),
    ChoiceDialog(1);
}

// 다이얼로그 기본형
open class DialogState(
    var isShow : Boolean = false,
    val useChildComposable : DialogType = DialogType.SimpleDialog,
    val title : String? = null,
    val message : String = "",
    val negativeButtonText : String? = null,
    val onNegativeClick : (() -> Unit)? = null,
    val positiveButtonText : String = "",
    val onPositiveClick : ((Any?) -> Unit)? = null,
)

// 선택형 다이얼로그
class ChoiceDialogState(
    context : Context,
    isShow : Boolean = false,
    title : String? = null,
    message : String = "",
    val list : List<Any>? = null,
    onNegativeClick : (() -> Unit)? = null,
    onPositiveClick : ((Any?) -> Unit)? = null,
) : DialogState(
    isShow = isShow,
    useChildComposable = DialogType.ChoiceDialog,
    title = title,
    message = message,
    negativeButtonText = context.getString(R.string.close),
    onNegativeClick = onNegativeClick,
    positiveButtonText = context.getString(R.string.confirm),
    onPositiveClick = onPositiveClick,
)
