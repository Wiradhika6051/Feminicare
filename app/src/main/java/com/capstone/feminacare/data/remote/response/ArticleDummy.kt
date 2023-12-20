package com.capstone.feminacare.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ArticleDummy(
    val title: String,
    val imageResources: Int,
    val description: String
) : Parcelable



