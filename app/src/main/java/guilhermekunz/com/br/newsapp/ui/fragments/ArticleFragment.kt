package guilhermekunz.com.br.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import guilhermekunz.com.br.newsapp.R
import guilhermekunz.com.br.newsapp.ui.MainActivity
import guilhermekunz.com.br.newsapp.ui.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }
}