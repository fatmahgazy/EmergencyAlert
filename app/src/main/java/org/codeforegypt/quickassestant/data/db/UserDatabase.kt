package org.codeforegypt.quickassestant.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.codeforegypt.quickassestant.data.model.Article
import org.codeforegypt.quickassestant.data.model.Contact
import org.codeforegypt.quickassestant.data.model.User
import org.codeforegypt.quickassestant.other.Constants.USER_DATABASE_NAME

@Database(
    entities = [User::class,Contact::class, Article::class],
    version = 1
)

abstract class UserDatabase: RoomDatabase() {
    companion object{
        fun get(context: Context) :UserDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                USER_DATABASE_NAME
            ).build()
        }
    }

    abstract fun getUserDao(): UserDao
    abstract fun getArticleDao(): ArticleDao
    abstract fun getContactDao(): ContactDao
}