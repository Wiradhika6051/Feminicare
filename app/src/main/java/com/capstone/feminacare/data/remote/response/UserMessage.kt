package com.capstone.feminacare.data.remote.response

data class UserMessage(
    override val content: String, override val timeStamp: Long,
) : Message {
    override val type: MessageType = MessageType.USER
}
