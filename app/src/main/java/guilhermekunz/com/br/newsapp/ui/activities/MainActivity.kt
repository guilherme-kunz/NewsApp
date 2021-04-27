package guilhermekunz.com.br.newsapp.ui.activities

import android.content.Intent
import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        setupBottomNav()
        setupNavDrawer()

        fecthUser()

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
                R.id.Gallery -> Toast.makeText(
                    applicationContext,
                    "Clicked item",
                    Toast.LENGTH_LONG
                )
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

    //desloga o usuario
    private fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun fecthUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    it.toString()
                    val user = it.getValue(User::class.java)
                    tvUserNameHeader.text = user!!.name
                    Glide.with(this@MainActivity).load(user.profileImageUrl).into(civProfileImageHeader)
//                    Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO()
            }
        })
    }


}