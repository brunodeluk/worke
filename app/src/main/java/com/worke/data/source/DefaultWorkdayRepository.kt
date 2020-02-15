package com.worke.data.source

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.worke.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.floor
import kotlin.math.round

class DefaultWorkdayRepository private constructor(app: Application) {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userDocumentPath: String
    private val workdaysCollectionPath: String
    private val cacheStartString: String
    private val isWorkingString: String

    init {
        val currentUser = firebaseAuth.currentUser
        userDocumentPath = app.getString(R.string.firestore_user_document_path, currentUser?.uid)
        workdaysCollectionPath = app.getString(R.string.firestore_workdays_collection_path, currentUser?.uid)
        cacheStartString = app.getString(R.string.cache_start)
        isWorkingString = app.getString(R.string.is_working)
    }

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

    fun start(): Task<Void> = firestore
        .document(this.userDocumentPath)
        .set(hashMapOf(
            this.cacheStartString to Date(),
            this.isWorkingString to true
        ))

    fun end(): Task<Void> {
        Log.d("WorkdayActivity", "ending workday")
        return firestore
            .document(userDocumentPath)
            .get()
            .continueWithTask { source ->
                val date = Date()
                val time = calculateWorkday(source.result?.getDate(cacheStartString)!!, date)
                val workday = hashMapOf(
                    "displayDate" to formatDate(date),
                    "displayTime" to formatTime(time),
                    "time" to time,
                    "date" to date
                )

                return@continueWithTask firestore.collection(workdaysCollectionPath)
                    .document(Date().time.toString())
                    .set(workday)
            }
            .continueWithTask {
                return@continueWithTask firestore
                    .document(userDocumentPath)
                    .update(isWorkingString, false)
            }
    }


    private fun formatTime(time: Long): String {
        val seconds = time / 1000
        val partialMinutes = floor((seconds / 60).toDouble())
        val partialHour = partialMinutes / 60
        val hour = floor(partialHour)
        val minute = round((partialHour - hour) * 60)

        return "${hour.toInt()} Horas ${minute.toInt()} Minutos"
    }


    private fun formatDate(date: Date): String {
        return SimpleDateFormat("EEEE, d MMMM yyyy")
            .format(date)
    }

    fun isWorking() = firestore
        .document(this.userDocumentPath)
        .get()
        .continueWith { source ->
            return@continueWith source.result?.getBoolean(isWorkingString)
        }

    private fun calculateWorkday(dateFrom: Date, dateTo: Date) = dateTo.time - dateFrom.time

    fun observeWorkdays() = firestore.collection(workdaysCollectionPath)

}
