package guilhermekunz.com.br.newsapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        //botão de registrar um usuario
        register_button.setOnClickListener {
            if (email_register.text.trim().toString().isNotEmpty() || password_register.text.trim()
                    .toString().isNotEmpty()
            ) {
                createUser(
                    email_register.text.trim().toString(), password_register.text.trim()
                        .toString()
                )
            } else {
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_LONG).show()
            }
        }

        //transição para activity login
        text_login_now.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.e("Task Message", "Successful...")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("Task Message", "Failed..." + task.exception)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser

        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}