package guilhermekunz.com.br.newsapp.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import guilhermekunz.com.br.newsapp.util.NewsApplication
import guilhermekunz.com.br.newsapp.models.Article
import guilhermekunz.com.br.newsapp.models.NewsResponse
import guilhermekunz.com.br.newsapp.repository.NewsRepository
import guilhermekunz.com.br.newsapp.ui.activities.User
import guilhermekunz.com.br.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
        app: Application,
        private val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    //Live Data Objects
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    //Solicita a rede
    init {
        getBreakingNews("us")
    }


    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    //define se queremos omitir o estado de sucesso ou de erro das breaking news
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                //aumenta o numero de paginas
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                    //adicona os artigos mais velhos a Breaking News
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    //define se queremos omitir o estado de sucesso ou de erro do searchnews
    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                //aumenta o numero de paginas
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                    //adicona mais artigos sobre o tema pesquisado
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                // A resposta de rede é salva no response
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                // erro no retrofit
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                // erro de conversão no Json
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                // A resposta de rede é salva no response
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                // erro no retrofit
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                // erro de conversão no Json
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    // verifica se está conectado a internet ou não
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activityNetwork = connectivityManager.activeNetwork ?: return false
            //verifica o estado de rede
            val capabilities = connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false
            // verifica o tipo de rede
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}