package com.worke.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.worke.R
import com.worke.workdays.WorkdayActivity

class SignInActivity : AppCompatActivity() {

    private val TAG = "SignInActivity"
    private val RC_SIGN_IN = 9001
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .build()

        this.googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<Button>(R.id.sign_in_btn).setOnClickListener {
            startActivityForResult(this.googleSignInClient.signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)

            }
        }
    }

    private fun firebaseAuthWithGoogle(acc: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(acc.idToken, null)
        FirebaseAuth.getInstance()
            .signInWithCredential(credentials)
            .addOnSuccessListener {
                startActivity(Intent(this, WorkdayActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Log.w(TAG, "Firebase sign in with credentials failed", it)
            }

    }
}
