package com.capstone.feminacare.ui.main.ui.perioddate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.capstone.feminacare.R
import com.capstone.feminacare.databinding.CalendarDayBinding
import com.capstone.feminacare.databinding.CalendarHeaderBinding
import com.capstone.feminacare.databinding.FragmentPeriodDateBinding
import com.capstone.feminacare.utils.CalendarUtils.displayText
import com.capstone.feminacare.utils.DummyData
import com.capstone.feminacare.utils.TimeUtils.convertDateToMillis
import com.capstone.feminacare.utils.TimeUtils.formatLocalDate
import com.capstone.feminacare.utils.TimeUtils.getDaysBetween
import com.capstone.feminacare.utils.TimeUtils.getMiddleDate
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class PeriodDateFragment : Fragment() {

    private var _binding: FragmentPeriodDateBinding? = null
    private val binding get() = _binding!!

//    we assume the cycle is more than 1
    private val periodCycle = DummyData.dummyCycle

    private val monthView: LocalDate? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeriodDateBinding.inflate(inflater, container, false)

        val daysOfWeek = daysOfWeek()
        setupCalendarBinder(daysOfWeek)

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        binding.periodCalendar.apply {
            setup(startMonth, endMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)
        }

        binding.periodCalendar.monthScrollListener = { month ->
            for (cycle in periodCycle) {
                val mid = getMiddleDate(cycle.startDate, cycle.endDate)
                if (mid.yearMonth == month.yearMonth) {
                    val daysBetween = getDaysBetween(cycle.startDate, cycle.endDate) + 1
                    val formattedStartDate = formatLocalDate(cycle.startDate)
                    val displayText = getString(R.string.totalMenstrualOrOvulationDay, daysBetween, formattedStartDate)
                    binding.tvTotalMenstrualDay.text = displayText
                }
            }

            binding.calendarMonthYearText.text = month.yearMonth.displayText()
        }

        binding.toNextMonth.setOnClickListener {
            binding.periodCalendar.findFirstVisibleMonth()?.let {
                binding.periodCalendar.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }

        binding.toPreviousMonth.setOnClickListener {
            binding.periodCalendar.findFirstVisibleMonth()?.let {
                binding.periodCalendar.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }


        return binding.root
    }


    private fun setupCalendarBinder(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val dayBinding = CalendarDayBinding.bind(view)
        }

        binding.periodCalendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                container.dayBinding.calendarDayText.text = data.date.dayOfMonth.toString()
//                For all day in current month (1-30/31)
                if (data.position == DayPosition.MonthDate) {

                    val cycles = periodCycle
                    for (cycle in cycles) {
                        val middle = getMiddleDate(cycle.startDate, cycle.endDate)

                        if (data.date.isAfter(cycle.startDate.minusDays(1)) && data.date.isBefore(cycle.endDate.plusDays(1))) {
                            container.dayBinding.ivIsPeriod.visibility = View.VISIBLE
                        }
                    }

                    if (data.date == LocalDate.now()) {
                        container.dayBinding.calendarDayText.setTextColor(resources.getColor(R.color.custom_pink_60))
                    }
                } else {
//                    Outline date that not included in current date
                    container.dayBinding.ivIsPeriod.visibility = View.GONE
                    container.dayBinding.calendarDayText.visibility = View.GONE
//                    container.dayBinding.calendarDayText.setTextColor(resources.getColor(R.color.text_grey)) //set text to grey
                }
            }

            override fun create(view: View): DayViewContainer  = DayViewContainer(view)
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val titlesContainer = CalendarHeaderBinding.bind(view).titleLayout.root
        }

        binding.periodCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.titlesContainer.tag == null) {
                        container.titlesContainer.tag = data.yearMonth
                        container.titlesContainer.children.map { it as TextView }.forEachIndexed { index , textView ->
//                            val dayOfWeek = data.yearMonth.month
//                            val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        }
                    }
                }

                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)

        }

    }

    private fun markDatesInRange(startDate: String, endDate: String) {
        val startDateMillis = convertDateToMillis(startDate)
        val endDateMillis = convertDateToMillis(endDate)

        // Iterate through the dates in the range and set a custom appearance
        val currentDate = Calendar.getInstance()
        currentDate.timeInMillis = startDateMillis

        while (currentDate.timeInMillis <= endDateMillis) {
            val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)
            val month = currentDate.get(Calendar.MONTH)
            val year = currentDate.get(Calendar.YEAR)

            // Customize the appearance for the date in the range

            // Move to the next day
            currentDate.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

