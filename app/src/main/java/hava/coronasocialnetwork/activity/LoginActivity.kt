package hava.coronasocialnetwork.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hava.coronasocialnetwork.MainActivity
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.operator.LoginStatus
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DaoAuthenManagement.getCurrentUser()?.run {
            redirectToMainScreen()
        }

        signUpTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        signInButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    it.isEnabled = false
                    val status = DaoAuthenManagement.login(
                        emailInput.text.toString(),
                        passwordInput.text.toString()
                    )
                    when (status) {
                        LoginStatus.NO_ACCOUNT_FOUND -> emailInput.error = "Account does not exist"
                        LoginStatus.INVALID_PASSWORD -> passwordInput.error = "Invalid password"
                        LoginStatus.OK -> redirectToMainScreen()
                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                } finally {
                    it.isEnabled = true
                }
            }
        }
    }

    private fun redirectToMainScreen() {
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()
    }
}