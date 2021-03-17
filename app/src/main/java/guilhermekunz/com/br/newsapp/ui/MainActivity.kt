package guilhermekunz.com.br.newsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import guilhermekunz.com.br.newsapp.R
import guilhermekunz.com.br.newsapp.database.ArticleDatabase
import guilhermekunz.com.br.newsapp.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Instancia o Repositorio
        val newsRepository = NewsRepository(ArticleDatabase(this))
        //Instancia o Factory
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        //Instancia o ViewModel
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        //Conecta o tab de navegação com os componentes de navegação
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}