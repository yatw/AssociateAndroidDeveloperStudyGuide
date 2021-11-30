package com.example.androidteststudyguide.features.api

import com.google.gson.annotations.SerializedName

data class CatBreedResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    val image: CatImage?
)