package guilhermekunz.com.br.newsapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import guilhermekunz.com.br.newsapp.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        register_button.setOnClickListener {
            registerUser()
        }

        text_login_now.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun registerUser() {
        if (email_register.text.toString().isEmpty()) {
            email_register.error = "Please enter email"
            email_register.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email_register.text.toString()).matches()) {
            email_register.error = "Please enter valid email"
            email_register.requestFocus()
            return
        }
        if (password_register.text.toString().isEmpty()) {
            password_register.error = "Please enter password"
            password_register.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(
            email_register.text.toString(),
            password_register.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}