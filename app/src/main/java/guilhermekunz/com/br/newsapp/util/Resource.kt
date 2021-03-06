package guilhermekunz.com.br.newsapp.util

sealed class Resource<T>(
     val data: T? = null,
     //mostra a mensagem de erro
     val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}