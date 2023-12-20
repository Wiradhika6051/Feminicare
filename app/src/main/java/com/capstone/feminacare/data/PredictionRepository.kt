package com.capstone.feminacare.data

import android.content.Context
import android.content.SharedPreferences
import com.capstone.feminacare.data.remote.response.MenstrualCycleResponse
import com.capstone.feminacare.utils.DummyData
import java.time.LocalDate

class PredictionRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("prediction_prefs", Context.MODE_PRIVATE)

    fun savePredictions(predictions: List<MenstrualCycleResponse>) {
        val editor = sharedPreferences.edit()
        editor.putInt("prediction_size", predictions.size)

        predictions.forEachIndexed { index, prediction ->
            editor.putInt("cycleNo_$index", prediction.cycleNo)
            editor.putLong("startDate_$index", prediction.startDate.toEpochDay())
            editor.putLong("endDate_$index", prediction.endDate.toEpochDay())
            editor.putInt("cycleLength_$index", prediction.cycleLength)
        }

        editor.apply()
    }

    fun getPredictions(): List<MenstrualCycleResponse> {
        val size = sharedPreferences.getInt("prediction_size", 0)
        val predictions = mutableListOf<MenstrualCycleResponse>()

        for (index in 0 until size) {
            val cycleNo = sharedPreferences.getInt("cycleNo_$index", 0)
            val startDate = LocalDate.ofEpochDay(sharedPreferences.getLong("startDate_$index", 0))
            val endDate = LocalDate.ofEpochDay(sharedPreferences.getLong("endDate_$index", 0))
            val cycleLength = sharedPreferences.getInt("cycleLength_$index", 0)

            predictions.add(MenstrualCycleResponse(cycleNo, startDate, endDate, cycleLength))
        }

        return predictions
    }

        companion object {
            @Volatile
            private var instance: PredictionRepository? = null

            fun getInstance(context: Context): PredictionRepository {
                return instance ?: synchronized(this) {
                    instance ?: PredictionRepository(context).also {
                        instance = it
                        instance?.let { repo ->
                            if(repo.getPredictions().isEmpty()) {
                                repo.savePredictions(DummyData.dummyCycle)
                            }
                        }
                    }
                }
            }
        }

}