package com.depromeet.housekeeper.model.response

import android.os.Parcelable
import com.depromeet.housekeeper.model.HouseWork
import kotlinx.parcelize.Parcelize

@Parcelize
data class HouseWorkCreateResponse(
    val houseWorks: List<HouseWork>
) : Parcelable
