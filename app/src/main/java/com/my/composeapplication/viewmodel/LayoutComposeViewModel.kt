package com.my.composeapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.my.composeapplication.base.data.BaseScrollParam
import com.my.composeapplication.base.data.ScrollStateParam
import com.my.composeapplication.scene.health.data.PagerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by YourName on 2022/08/19.
 */
@HiltViewModel
class LayoutComposeViewModel @Inject constructor(): ViewModel() {
    private val scrollStateMap = HashMap<String, BaseScrollParam>()

    fun setScrollState(key:String, param : BaseScrollParam){
        this.scrollStateMap[key] = param
    }

    fun getScrollState(key:String) = this.scrollStateMap.remove(key)

    fun getInitPager() = listOf<PagerItem>(
        PagerItem("https://cdn.pixabay.com/photo/2020/07/14/16/18/snow-5404785_960_720.jpg", " 산이다."),
        PagerItem("https://cdn.pixabay.com/photo/2022/08/19/10/35/scooter-7396608_960_720.jpg", " 스쿠터 자전거"),
        PagerItem("https://cdn.pixabay.com/photo/2022/08/17/20/42/hot-air-balloon-7393437_960_720.jpg", " 열기구 카파도피아"),
        PagerItem("https://cdn.pixabay.com/photo/2022/07/30/04/46/sunrise-7353034_960_720.jpg", " 바다 석양"),
        PagerItem("https://cdn.pixabay.com/photo/2022/07/30/14/35/sunflowers-7353922_960_720.jpg", " 해바라기"),
        PagerItem("https://cdn.pixabay.com/photo/2022/08/18/11/29/wind-energy-7394705_960_720.jpg", " 풍력 발전기"),
    )
}