package com.my.composeapplication.enum

import com.my.composeapplication.R

/**
 * Created by YourName on 2022/08/18.
 */

enum class BMIResult(value : Int) {
    Level1(0),
    Level2(1),
    Level3(2);

    companion object {
        fun getResultStringId(level : BMIResult) : Int {
            return getResultStringId(level.ordinal)
        }

        fun getResultStringId(level : Int) = when (level) {
            Level1.ordinal -> {
                R.string.level1
            }
            Level2.ordinal -> {
                R.string.level2
            }
            Level3.ordinal -> {
                R.string.level3
            }
            else -> {
                R.string.level1
            }
        }

        fun getResultImageId(level : BMIResult) : Int {
            return getResultImageId(level.ordinal)
        }

        fun getResultImageId(level : Int) = when (level) {
            Level1.ordinal -> {
                R.drawable.ic_baseline_sentiment_satisfied_alt_24
            }
            Level2.ordinal -> {
                R.drawable.ic_baseline_sentiment_dissatisfied_24
            }
            Level3.ordinal -> {
                R.drawable.ic_baseline_sentiment_very_dissatisfied_24
            }
            else -> {
                R.drawable.ic_baseline_sentiment_satisfied_alt_24
            }
        }
    }
}