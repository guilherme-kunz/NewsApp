package guilhermekunz.com.br.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import guilhermekunz.com.br.newsapp.R
import guilhermekunz.com.br.newsapp.ui.MainActivity
import guilhermekunz.com.br.newsapp.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        //obtem o artigo atual
        val article = args.article

        //exibe no modo web
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
    }
}