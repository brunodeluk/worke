package com.worke.workdays

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.worke.data.source.DefaultWorkdayRepository

class StartWorkdayViewModel(app: Application): AndroidViewModel(app) {
    private val defaultRepository = DefaultWorkdayRepository.getRepository(app)
}
