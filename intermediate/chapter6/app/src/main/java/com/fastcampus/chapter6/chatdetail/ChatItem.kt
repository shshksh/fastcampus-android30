package com.fastcampus.chapter6.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String
) {
    constructor() : this("", "")
}
