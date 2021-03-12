package guilhermekunz.com.br.newsapp.repository

import guilhermekunz.com.br.newsapp.api.RetrofitInstance
import guilhermekunz.com.br.newsapp.database.ArticleDatabase

class NewsRepository(
     val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
            RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

}