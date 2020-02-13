package com.worke.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.worke.R
import com.worke.signin.SignInActivity
import com.worke.workdays.WorkdayActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val currentUser = FirebaseAuth.getInstance().currentUser
        var intent = Intent(this, WorkdayActivity::class.java)

        if (currentUser == null) {
            intent = Intent(this, SignInActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
