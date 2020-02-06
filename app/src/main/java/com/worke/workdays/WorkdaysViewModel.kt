package com.worke.workdays

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.worke.data.source.DefaultWorkdayRepository

class WorkdaysViewModel(app: Application) : AndroidViewModel(app) {
    private val workdayRepository = DefaultWorkdayRepository.getRepository(app)

    fun getWorkdays() {

    }
}
