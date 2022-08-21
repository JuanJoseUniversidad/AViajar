package es.uji.al394516.aviajar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CountryEntity:: class,
        ExpenseEntity::class,
        PersonEntity::class,
        TravelEntity::class,
        PersonExpenseEntity::class
    ],
    version = 1
)
abstract class TravelsDatabaseAbstract: RoomDatabase() {
    abstract fun getDAO():ITravelsDAO
}

class TravelDatabase private constructor(context: Context){
    companion object: SingletonHolder<TravelDatabase,Context>(::TravelDatabase)
    val dao = Room.databaseBuilder(context,TravelsDatabaseAbstract::class.java,"Database").build().getDAO()
}