package com.worke

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.worke.data.source.DefaultWorkdayRepository
import com.worke.data.source.SessionRepository

class SessionViewModel(app: Application): AndroidViewModel(app) {

    private val defaultRepository = SessionRepository.getRepository(app)

    init {

    }

    fun signIn(): Task<AuthResult> {
        return defaultRepository.signIn()
    }

    fun signOut() {
        defaultRepository.signOut()
    }

}
