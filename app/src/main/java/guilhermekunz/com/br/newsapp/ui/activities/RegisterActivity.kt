package guilhermekunz.com.br.newsapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import guilhermekunz.com.br.newsapp.R
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

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

        civProfileImageRegister.setOnClickListener {
            galleryAccess()
        }

    }

    private fun registerUser() {
        if (etName.text.toString().isEmpty()){
            etName.error = "Please enter Name"
            etName.requestFocus()
            return
        }

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
                                uploadImageToFirebaseStorage()
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

    //Acessa a Galeria
    private fun galleryAccess() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed and check what selected image was...
            Log.d("RegisterActivity", "Photo was selected")
            selectedPhotoUri = data.data
            civProfileImageRegister.setImageURI(selectedPhotoUri)
        }
    }


    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener{

            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, etName.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally we saved the user to Firebase Database")
            }
    }
}

class User(val uid: String, val name: String, val profileImageUrl: String) {
    constructor() : this("", "", "")
}

