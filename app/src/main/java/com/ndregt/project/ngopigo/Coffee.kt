package com.ndregt.project.ngopigo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coffee(
    val name: String,
    val description: String,
    val category: String,
    val photo: Int,
    val ingredients: String,
    val tools: String,
    val servingSteps: String
) : Parcelable