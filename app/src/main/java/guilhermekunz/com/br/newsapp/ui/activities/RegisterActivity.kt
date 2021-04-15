package guilhermekunz.com.br.newsapp.ui.activities

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import guilhermekunz.com.br.newsapp.R
import guilhermekunz.com.br.newsapp.ui.fragments.BreakingNewsFragment
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        //botão de registrar um usuario
        registerButton.setOnClickListener {
            if (emailRegister.text.trim().toString().isNotEmpty() || passwordRegister.text.trim()
                    .toString().isNotEmpty()
            ) {
                createUser(
                    emailRegister.text.trim().toString(), passwordRegister.text.trim()
                        .toString()
                )
            } else {
                Toast.makeText(this, "Input Required", Toast.LENGTH_LONG).show()
            }
        }

        //transição para activity login
        textAlreadyHaveAccountLoginNow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.e("Task Message", "Successful...")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
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
        }
    }

}