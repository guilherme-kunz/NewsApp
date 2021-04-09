package guilhermekunz.com.br.newsapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import guilhermekunz.com.br.newsapp.R
import guilhermekunz.com.br.newsapp.database.ArticleDatabase
import guilhermekunz.com.br.newsapp.repository.NewsRepository
import guilhermekunz.com.br.newsapp.ui.viewmodel.NewsViewModel
import guilhermekunz.com.br.newsapp.ui.viewmodel.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*
import java.io.File
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var toggle: ActionBarDrawerToggle

    var currentPath: String? = null
    val TAKE_PICTURE = 1
    val SELECT_PICTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Instancia o Repositorio
        val newsRepository = NewsRepository(ArticleDatabase(this))
        //Instancia o Factory
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        //Instancia o ViewModel
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        //Conecta o tab de navegação com os componentes de navegação
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Item1 -> accessCameraIntent()
                R.id.Item2 -> accessGalleryIntent()
                R.id.maps -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // recebe a foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val file = File(currentPath)
                val uri = Uri.fromFile(file)
                profile_image.setImageURI(uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                profile_image.setImageURI(uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    //abre a galeria
    private fun accessGalleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_PICTURE)
    }


    //abre a camera
    private fun accessCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (photoFile != null) {
                var photoUri = FileProvider.getUriForFile(
                    this,
                    "guilhermekunz.com.br.newsapp.fileprovider", photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, TAKE_PICTURE)
            }
        }
    }

    //cria a imagem
    private fun createImage(): File {
        val timeStamp = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val imageName = "JPEG_" + timeStamp + "_"
        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile(imageName, ".jpg", storageDir)
        currentPath = image.absolutePath
        return image
    }

}