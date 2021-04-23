package guilhermekunz.com.br.newsapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import guilhermekunz.com.br.newsapp.R
import guilhermekunz.com.br.newsapp.database.ArticleDatabase
import guilhermekunz.com.br.newsapp.repository.NewsRepository
import guilhermekunz.com.br.newsapp.ui.viewmodel.NewsViewModel
import guilhermekunz.com.br.newsapp.ui.viewmodel.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var viewModel: NewsViewModel
    lateinit var toggle: ActionBarDrawerToggle

//    var currentPath: String? = null
//    val TAKE_PICTURE = 1
//    val SELECT_PICTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        setupBottomNav()
        setupNavDrawer()


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
                R.id.Camera -> Toast.makeText(applicationContext, "Clicked item", Toast.LENGTH_LONG)
                    .show()
                R.id.Gallery -> galleryAccess()
                R.id.maps -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                }
                R.id.logout -> logout()
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
            Log.d("MainActivity", "Photo was selected")
            selectedPhotoUri = data.data
            civProfileImage.setImageURI(selectedPhotoUri)
        }
    }

    //Controle do Navigation Drawer
    private fun setupNavDrawer() {
        val navController = findNavController(R.id.newsNavHostFragment)
        findViewById<NavigationView>(R.id.navView)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.articleFragment -> showNavDrawer()
                R.id.breakingNewsFragment -> showNavDrawer()
                R.id.savedNewsFragment -> showNavDrawer()
                R.id.searchNewsFragment -> showNavDrawer()
                else -> hideNavDrawer()
            }
        }
    }

    //Exibe o navigation drawer
    private fun showNavDrawer() {
        navView.visibility = View.VISIBLE
    }

    //Esconde o Navigation Drawer
    private fun hideNavDrawer() {
        navView.visibility = View.GONE
    }


    //Controle do Bottom Navigation
    private fun setupBottomNav() {
        val navController = findNavController(R.id.newsNavHostFragment)
        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.articleFragment -> showBottomNav()
                R.id.breakingNewsFragment -> showBottomNav()
                R.id.savedNewsFragment -> showBottomNav()
                R.id.searchNewsFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    //Exibe o bottom navigation
    private fun showBottomNav() {
        bottomNavigationView.visibility = View.VISIBLE
    }

    //Esconde o bootom navigation
    private fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE
    }

//    // recebe a foto
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
//            try {
//                val file = File(currentPath)
//                val uri = Uri.fromFile(file)
//                cviProfileImage.setImageURI(uri)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
//            try {
//                val uri = data!!.data
//                cviProfileImage.setImageURI(uri)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    //abre a galeria
//    private fun dispatchGalleryIntent(){
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_PICTURE)
//    }
//
//    //abre a camera
//    private fun dispatchCameraIntent() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (intent.resolveActivity(packageManager) != null) {
//            var photoFile: File? = null
//            try {
//                photoFile = createImage()
//            }catch (e: IOException){
//                e.printStackTrace()
//            }
//            if (photoFile != null) {
//                var photoUri = FileProvider.getUriForFile(this,
//                    "guilhermekunz.com.br.newsapp.fileprovider", photoFile)
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//                startActivityForResult(intent, TAKE_PICTURE)
//            }
//        }
//    }
//
//    //cria a imagem
//    private fun createImage(): File {
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageName = "JPEG_"+timeStamp+"_"
//        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        var image = File.createTempFile(imageName, ".jpg", storageDir)
//        currentPath = image.absolutePath
//        return image
//    }

    //desloga o usuario
    private fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}