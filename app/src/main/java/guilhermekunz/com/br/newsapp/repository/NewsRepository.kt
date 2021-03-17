package guilhermekunz.com.br.newsapp.repository

import guilhermekunz.com.br.newsapp.api.RetrofitInstance
import guilhermekunz.com.br.newsapp.database.ArticleDatabase
import guilhermekunz.com.br.newsapp.models.Article

class NewsRepository(
        private val db: ArticleDatabase
) {
    //Pega as ultimas noticias da API
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
            RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    //Faz a busca das noticias
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
            RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    //insere as noticias
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    //pega as noticias salvas
    fun getSavedNews() = db.getArticleDao().getAllArticles()

    //Deleta artigo do banco de dados
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}