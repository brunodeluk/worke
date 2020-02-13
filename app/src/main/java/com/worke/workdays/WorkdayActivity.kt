package com.worke.workdays

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.worke.R
import java.util.*
import java.util.concurrent.TimeUnit

class WorkdayActivity : AppCompatActivity() {

    private val TAG = "WorkdayActivity"

    private lateinit var playButton: FloatingActionButton
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var working = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workday)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        playButton = findViewById(R.id.play_action_btn)

        val currentUser = firebaseAuth.currentUser

        val workdaysCollectionPath = getString(
            R.string.firestore_workdays_collection_path,
            currentUser?.uid
        )

        val cacheDocumentPath = getString(
            R.string.firestore_user_document_path,
            currentUser?.uid
        )

        firestore.collection(workdaysCollectionPath)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Error in snapshot", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    firestore
                        .document(cacheDocumentPath)
                        .update(getString(R.string.cache_start), FieldValue.delete())
                }
            }

        firestore.document(cacheDocumentPath)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                working = documentSnapshot.data?.get(getString(R.string.cache_start)) != null
            }

        playButton.setOnClickListener { view -> if(working) pause() else play(view) }
    }



    private fun play(view: View) {
        Log.d(TAG, "Playing")
        working = true
        playButton.setImageDrawable(getDrawable(R.drawable.ic_pause_24dp))

        val documentPath = getString(
            R.string.firestore_user_document_path,
            firebaseAuth.currentUser?.uid
        )

        Log.d(TAG, documentPath)

        val data = hashMapOf(getString(R.string.cache_start) to Date())

        Log.d(TAG, "writing ${data}")

        firestore
            .document(documentPath)
            .set(data)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully saved data into firestore ${data}")
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "Error setting data in firestore", error)
                playButton.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_24dp))
                Snackbar.make(view, "There was an error", Snackbar.LENGTH_LONG)
            }
    }

    private fun pause() {
        Log.d(TAG, "Pausing")
        working = false
        playButton.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_24dp))

        val currentUser = firebaseAuth.currentUser

        val cacheDocumentPath = getString(
            R.string.firestore_user_document_path,
            currentUser?.uid
        )

        val workdaysCollectionPath = getString(
            R.string.firestore_workdays_collection_path,
            currentUser?.uid
        )

        firestore
            .document(cacheDocumentPath)
            .get()
            .addOnSuccessListener { source ->
                val workday = hashMapOf(
                    "time" to calculateWorkday(source.getDate(getString(R.string.cache_start))!!, Date()),
                    "date" to Date()
                )

                Log.d(TAG, "creating workday ${workday}")

                firestore
                    .collection(workdaysCollectionPath)
                    .document(Date().time.toString())
                    .set(workday)
                    .addOnSuccessListener { Log.d(TAG, "workday successfully saved") }
            }
    }

    private fun calculateWorkday(dateFrom: Date, dateTo: Date): Long {
        return dateTo.time - dateFrom.time
    }

}
