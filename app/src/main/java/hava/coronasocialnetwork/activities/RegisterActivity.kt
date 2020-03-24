package hava.coronasocialnetwork.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hava.coronasocialnetwork.R
import hava.coronasocialnetwork.database.management.DaoAuthenManagement
import hava.coronasocialnetwork.database.operator.RegisterStatus
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener {
            if (passwordInput.text.toString() != rePasswordInput.text.toString()) {
                rePasswordInput.error = "Password does not match"
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val result = DaoAuthenManagement.register(
                            emailInput.text.toString(),
                            passwordInput.text.toString(),
                            phoneInput.text.toString(),
                            nameInput.text.toString(),
                            ""
                        )

                        when (result) {
                            RegisterStatus.EMAIL_ALREADY_EXISTED -> emailInput.error =
                                "Email already exists"
                            RegisterStatus.WRONG_EMAIL_FORMAT -> emailInput.error =
                                "Wrong email format"
                            RegisterStatus.OK -> finish()
                            RegisterStatus.WEAK_PASSWORD -> passwordInput.error =
                                "Password has at least 6 characters"
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            applicationContext,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
