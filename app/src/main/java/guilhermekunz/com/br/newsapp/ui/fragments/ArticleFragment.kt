package guilhermekunz.com.br.newsapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import guilhermekunz.com.br.newsapp.R
import guilhermekunz.com.br.newsapp.ui.activities.MainActivity
import guilhermekunz.com.br.newsapp.ui.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()

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

        //botão para salvar o artigo
        fabFavorite.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }

        //botão para compartilhar o artigo
        fabShare.setOnClickListener {
            val articleShare = article.url
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, articleShare)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share to: "))
        }
    }
}