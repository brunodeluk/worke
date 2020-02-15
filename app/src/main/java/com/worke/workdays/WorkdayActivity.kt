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
import com.worke.data.source.DefaultWorkdayRepository
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.TimeUnit

class WorkdayActivity : AppCompatActivity() {

    private val TAG = "WorkdayActivity"

    private lateinit var playButton: FloatingActionButton
    private lateinit var repository: DefaultWorkdayRepository
    private var working = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workday)
        playButton = findViewById(R.id.play_action_btn)
        repository = DefaultWorkdayRepository.getRepository(this.application)

        repository.isWorking().addOnSuccessListener { isWorking ->
            Log.d(TAG, "Cheking if is working")
            if (isWorking != null && isWorking) {
                Log.d(TAG, "Is working!!")
                startWorking()
            }
            else {
                Log.d(TAG, "Is not working...")
            }
        }

        playButton.setOnClickListener { view -> if(working) pause(view) else play(view) }
    }

    private fun play(view: View) {
        Log.d(TAG, "Playing")
        startWorking()

        this.repository
            .start()
            .addOnSuccessListener {
                Snackbar.make(view, "Working...", Snackbar.LENGTH_LONG)
            }
            .addOnFailureListener {
                playButton.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_24dp))
                Snackbar.make(view, "There was an error", Snackbar.LENGTH_LONG)
            }
    }

    private fun pause(view: View) {
        Log.d(TAG, "Pausing")
        stopWorking()

        this.repository
            .end()
            .addOnFailureListener {
                playButton.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_24dp))
                Snackbar.make(view, "There was an error", Snackbar.LENGTH_LONG)
            }
    }

    private fun startWorking() {
        working = true
        playButton.setImageDrawable(getDrawable(R.drawable.ic_pause_24dp))
    }

    private fun stopWorking() {
        working = false
        playButton.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_24dp))
    }

}
