package com.stratagile.qlink.ui.activity.defi

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.stratagile.qlink.entity.defi.DefiDetail
import com.stratagile.qlink.entity.defi.DefiStateList

class DefiViewModel : ViewModel() {
    var defiDetailLiveData = MutableLiveData<DefiDetail>()

    var defiStateListLiveData = MutableLiveData<DefiStateList>()
}