package com.capstone.feminacare.ui.main.ui.checkuphistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.feminacare.data.local.BloodCheckup
import com.capstone.feminacare.databinding.ItemCheckupBinding
import com.capstone.feminacare.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CheckupHistoryAdapter :
    ListAdapter<BloodCheckup, CheckupHistoryAdapter.CheckupHistoryViewHolder>(
        DIFF_CALLBACK
    ) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BloodCheckup>() {
            override fun areItemsTheSame(oldItem: BloodCheckup, newItem: BloodCheckup): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BloodCheckup, newItem: BloodCheckup): Boolean {
                return oldItem == newItem

            }
        }
    }

    inner class CheckupHistoryViewHolder(val itemBinding: ItemCheckupBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(checkup: BloodCheckup) {
            val calendar = TimeUtils.timeMillisToCalendar(checkup.timeStamp)
            val month = SimpleDateFormat("MMM", Locale.getDefault())

            val resource = itemView.context.resources

            itemBinding.tvChkpDay.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            itemBinding.tvChkpMonth.text = month.format(calendar.timeInMillis)
            itemBinding.tvChkpYear.text = calendar.get(Calendar.YEAR).toString()
            itemBinding.tvChkpDescription.text = checkup.description.trimIndent()

//            itemBinding.tvCheckupCondition.apply {
//                when(this.text) {
//                    "Red" -> { background = ResourcesCompat.getDrawable(resource, ) }
//                }
//                background = ResourcesCompat.getDrawable(
//                    itemView.context.resources,
//                    R.drawable.background_healthy,
//                    null
//                )
//            }

//            when (checkup.healthInfo) {
//                "Healthy" -> itemBinding.tvCheckupCondition.apply {
//                    text = itemView.context.getString(R.string.healthy_check)
//                    background = ResourcesCompat.getDrawable(
//                        itemView.context.resources,
//                        R.drawable.background_healthy,
//                        null
//                    )
//                }
//            }
            itemBinding.tvCheckupCondition.text = checkup.healthInfo

        }

        fun toggleDescClick() {
            val layoutParams = itemBinding.tvChkpDescription.layoutParams
            if(layoutParams.height == 0) {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                layoutParams.height = 0
            }

            itemBinding.tvChkpDescription.layoutParams = layoutParams

//            itemBinding.showDesc.setOnClickListener {
//                itemBinding.tvChkpDescription.height =
//                    when (itemBinding.tvChkpDescription.layoutParams.height) {
//                        0 -> ViewGroup.LayoutParams.WRAP_CONTENT
//                        else -> 0
//                    }
//            }
        }
    }

    override fun onBindViewHolder(holder: CheckupHistoryViewHolder, position: Int) {
        val checkup = getItem(position) as BloodCheckup
        holder.itemBinding.showDesc.setOnClickListener {
            holder.toggleDescClick()
        }
        holder.bind(checkup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckupHistoryViewHolder {
        println()
        val binding = ItemCheckupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CheckupHistoryViewHolder(binding)
    }

}