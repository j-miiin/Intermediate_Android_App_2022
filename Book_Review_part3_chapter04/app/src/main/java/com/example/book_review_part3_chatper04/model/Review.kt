package com.example.book_review_part3_chatper04.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review(
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "review") val review: String?
)
