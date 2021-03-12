package guilhermekunz.com.br.newsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import guilhermekunz.com.br.newsapp.models.Article

@Dao
interface ArticleDao {

    //Insere ou atualiza o artigo no banco de dados
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    //Retorna todos os artigos da pesquisa
    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    //Deleta artigos do banco de dados
    @Delete
    suspend fun deleteArticle(article: Article)
}