package guilhermekunz.com.br.newsapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
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
        textViewDontHaveAccountRegisterNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //Botão de Login
        loginButton.setOnClickListener {
            if (loginEmail.text.trim().toString().isNotEmpty() || loginPassword.text.trim().toString().isNotEmpty()) {
                signInUser(loginEmail.text.trim().toString(), loginPassword.text.trim().toString())
            } else {
                Toast.makeText(this, "Input required", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener (this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Erro !!"+task.exception, Toast.LENGTH_LONG).show()
                }
            }
    }

}