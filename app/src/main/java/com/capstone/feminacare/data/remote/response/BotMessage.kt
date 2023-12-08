package com.capstone.feminacare.data.remote.response

data class BotMessage(
    override val content: String, override val timeStamp: Long,
) : Message {
    override val type: MessageType = MessageType.BOT
}
