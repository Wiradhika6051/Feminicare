package com.capstone.feminacare.data.response


interface Message {
    val content: String
    val timeStamp: Long
    val type: MessageType
// Enum indicating whether it's a user or bot message
}

enum class MessageType {
    USER,
    BOT
}