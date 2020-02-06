package com.worke.data.source

import androidx.lifecycle.LiveData
import com.worke.data.Workday
import com.worke.data.Result

interface WorkdayDataSource {

    fun observeWorkdays(): LiveData<Result<List<Workday>>>

    suspend fun getWorkdays(): Result<List<Workday>>

    suspend fun refreshWorkdays()

    suspend fun getWorkday(workId: Int): Result<Workday>

    suspend fun saveWorkday(work: Workday)

}

