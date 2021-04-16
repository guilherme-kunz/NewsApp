package guilhermekunz.com.br.newsapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import guilhermekunz.com.br.newsapp.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        //transição para activity registro
        text_resgister_now.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Botão de Login
        login_button.setOnClickListener {
            if (login_email.text.trim().toString().isNotEmpty() || login_password.text.trim().toString().isNotEmpty()) {
                signInUser(login_email.text.trim().toString(), login_password.text.trim().toString())
            } else {
                Toast.makeText(this, "Enter E-mail and Password", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener (this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error!!"+task.exception, Toast.LENGTH_LONG).show()
                }
            }
    }

}