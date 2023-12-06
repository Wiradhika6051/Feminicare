package com.capstone.feminacare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.capstone.feminacare.data.remote.response.BotMessage
import com.capstone.feminacare.data.remote.response.Message
import com.capstone.feminacare.data.remote.response.MessageType
import com.capstone.feminacare.data.remote.response.UserMessage
import com.capstone.feminacare.databinding.ItemMsgbotBinding
import com.capstone.feminacare.databinding.ItemMsguserBinding
import com.capstone.feminacare.utils.TimeUtils


class ChatBotMessageAdapter : ListAdapter<Message, BaseViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val VIEW_TYPE_USER_MESSAGE = 1
        private const val VIEW_TYPE_BOT_MESSAGE = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.timeStamp == newItem.timeStamp
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return if (oldItem.type == MessageType.BOT && newItem.type == MessageType.BOT) {
                    oldItem as BotMessage == newItem as BotMessage
                } else {
                    oldItem as UserMessage == newItem as UserMessage
                }

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)

        return when (message.type) {
            MessageType.USER -> VIEW_TYPE_USER_MESSAGE
            MessageType.BOT -> VIEW_TYPE_BOT_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_USER_MESSAGE -> {
                val userBinding = ItemMsguserBinding.inflate(inflater, parent, false)
                UserMessageViewHolder(userBinding)
            }

            VIEW_TYPE_BOT_MESSAGE -> {
                val botBinding = ItemMsgbotBinding.inflate(inflater, parent, false)
                BotMessageViewHolder(botBinding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }



    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val message = getItem(position)

        when (holder) {
            is UserMessageViewHolder -> {
                val userMsg = message as UserMessage
                holder.itemBinding.tvMessage.text = userMsg.content
                holder.itemBinding.tvTimestamp.text = TimeUtils.formatMillisToDateTime(userMsg.timeStamp)
            }
            is BotMessageViewHolder -> {
                val botMsg = message as BotMessage
                holder.itemBinding.tvMessage.text = botMsg.content
                holder.itemBinding.tvTimestamp.text = TimeUtils.formatMillisToDateTime(botMsg.timeStamp)
            }
        }
    }


}

open class BaseViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

class UserMessageViewHolder(val itemBinding: ItemMsguserBinding) : BaseViewHolder(itemBinding)
class BotMessageViewHolder(val itemBinding: ItemMsgbotBinding) : BaseViewHolder(itemBinding)