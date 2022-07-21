package com.example.book_review_part3_chatper04.model

import com.google.gson.annotations.SerializedName

data class BestSellerDto (
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String
)