package hava.coronasocialnetwork

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import hava.coronasocialnetwork.activities.RegisterActivity


class MainActivity : AppCompatActivity() {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mAuth = FirebaseAuth.getInstance()

        startActivity(Intent(this, RegisterActivity::class.java))
    }

}
