package com.example.carrot_market_part3_chapter06.chatlist

data class ChatListItem(
    val buyerId: String,
    val sellerId: String,
    val itemTitle: String,
    val key: Long
) {

    constructor(): this("", "", "", 0)
}
