package com.worke.data.source

import android.app.Application
import androidx.lifecycle.LiveData
import com.worke.data.Result
import com.worke.data.Workday
import com.worke.data.source.local.WorkdayLocalDataSource
import com.worke.data.source.remote.WorkdayRemoteDataSource
import java.lang.Exception
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DefaultWorkdayRepository private constructor(application: Application) {

    private val workdayRemoteDataSource: WorkdayDataSource
    private val workdayLocalDataSource: WorkdayDataSource

    companion object {

        @Volatile
        private var INSTANCE: DefaultWorkdayRepository? = null

        fun getRepository(app: Application): DefaultWorkdayRepository {
            return INSTANCE ?: synchronized(this) {
                DefaultWorkdayRepository(app).also {
                    INSTANCE = it
                }
            }
        }

    }

    init {
        workdayLocalDataSource = WorkdayLocalDataSource()
        workdayRemoteDataSource = WorkdayRemoteDataSource()
    }

    suspend fun getWorkdays(forceUpdate: Boolean = false): Result<List<Workday>> {
        if (forceUpdate) {
            try {
                updateWorkdayFromRemoteDataSource()
            }
            catch (ex: Exception) {
                return Result.Error(ex)
            }
        }

        return workdayLocalDataSource.getWorkdays()
    }

    suspend fun refreshWorkdays() {
        updateWorkdayFromRemoteDataSource()
    }

    fun observeWorkdays(): LiveData<Result<List<Workday>>> {
        return workdayLocalDataSource.observeWorkdays()
    }

    private suspend fun updateWorkdayFromRemoteDataSource() {
        val remoteWorkdays = workdayRemoteDataSource.getWorkdays()
        if (remoteWorkdays is Result.Success) {
            remoteWorkdays.data.forEach { workday ->
                workdayLocalDataSource.saveWorkday(workday)
            }
        }
        else if (remoteWorkdays is Result.Error) {
            throw remoteWorkdays.exception
        }
    }

    suspend fun getWorkday(workId: Int, forceUpdate: Boolean = false): Result<Workday> {
        if (forceUpdate) {
            updateWorkdayFromRemoteDataSource(workId)
        }

        return workdayLocalDataSource.getWorkday(workId)
    }

    private suspend fun updateWorkdayFromRemoteDataSource(workId: Int) {
        val remoteWorkdays = workdayRemoteDataSource.getWorkday(workId)
        if (remoteWorkdays is Result.Success) {
            workdayLocalDataSource.saveWorkday(remoteWorkdays.data)
        }
    }

    suspend fun saveWorkday(workday: Workday) {
        coroutineScope {
            launch { workdayRemoteDataSource.saveWorkday(workday) }
            launch { workdayLocalDataSource.saveWorkday(workday) }
        }
    }

}
