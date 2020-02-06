package com.worke.data.source.local

import androidx.lifecycle.LiveData
import com.worke.data.Result
import com.worke.data.Workday
import com.worke.data.source.WorkdayDataSource

class WorkdayLocalDataSource : WorkdayDataSource {

    override fun observeWorkdays(): LiveData<Result<List<Workday>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getWorkdays(): Result<List<Workday>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun refreshWorkdays() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getWorkday(workId: Int): Result<Workday> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveWorkday(work: Workday) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
