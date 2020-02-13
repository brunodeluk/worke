package com.worke.data.source

import android.app.Application
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SessionRepository private constructor(app: Application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {

        @Volatile
        private var INSTANCE: SessionRepository ? = null

        fun getRepository(app: Application): SessionRepository {
            return INSTANCE ?: synchronized(this) {
                SessionRepository(app).also {
                    INSTANCE = it
                }
            }
        }
    }

    fun signIn(): Task<AuthResult> {
        return firebaseAuth.signInAnonymously()
    }

    fun signOut(): Unit {
        firebaseAuth.signOut()
    }

}