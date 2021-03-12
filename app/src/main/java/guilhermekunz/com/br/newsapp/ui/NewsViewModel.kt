package guilhermekunz.com.br.newsapp.ui

import androidx.lifecycle.ViewModel
import guilhermekunz.com.br.newsapp.repository.NewsRepository

class NewsViewModel(
     val newsRepository: NewsRepository
) : ViewModel() {


}