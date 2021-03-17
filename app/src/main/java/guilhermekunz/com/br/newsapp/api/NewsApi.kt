package guilhermekunz.com.br.newsapp.api

import guilhermekunz.com.br.newsapp.models.NewsResponse
import guilhermekunz.com.br.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    //Noticias de ultima hora -- Breaking News
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
            //Define o pais das noticias de ultima hora
            @Query("country")
            countryCode: String = "us",
            //Define a quantidade de artigos(20)
            @Query("page")
            pageNumber: Int = 1,
            //Recebe a API KEY do Constants
            @Query("apiKey")
            apiKey: String = API_KEY
    ): Response<NewsResponse>

    //Todas as noticias -- Search News
    @GET("v2/everything")
    suspend fun searchForNews(
            @Query("q")
            searchQuery: String,
            @Query("page")
            pageNumber: Int = 1,
            @Query("apiKey")
            apiKey: String = API_KEY
    ): Response<NewsResponse>
}