package guilhermekunz.com.br.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import guilhermekunz.com.br.newsapp.models.Article

@Database(
        entities = [Article::class],
        version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    //cria o banco de dados real
    companion object {
        @Volatile
        //instancia o banco de dados
        private var instance: ArticleDatabase? = null
        //sincroniza a configuração da instancia, que assegura que recebemos apenas uma unica instancia do banco de dados de uma vez
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it}
        }

        //cria o banco de dados
        private fun createDatabase(context: Context) =
                Room.databaseBuilder(
                     context.applicationContext,
                     ArticleDatabase::class.java,
                     "article_database.db"
                ).build()
    }
}