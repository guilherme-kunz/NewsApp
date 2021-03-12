package guilhermekunz.com.br.newsapp.database

import androidx.room.TypeConverter
import guilhermekunz.com.br.newsapp.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}