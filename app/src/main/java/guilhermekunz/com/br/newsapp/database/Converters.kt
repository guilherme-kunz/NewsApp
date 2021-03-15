package guilhermekunz.com.br.newsapp.database

import androidx.room.TypeConverter
import guilhermekunz.com.br.newsapp.models.Source

class Converters {

    @TypeConverter
    //Converte o name para uma String
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    //Converte o name para um Source
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}