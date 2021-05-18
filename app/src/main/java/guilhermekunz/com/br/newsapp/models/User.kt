package guilhermekunz.com.br.newsapp.models

data class User(val uid: String, val name: String, val profileImageUrl: String) {
    constructor() : this("", "", "")
}