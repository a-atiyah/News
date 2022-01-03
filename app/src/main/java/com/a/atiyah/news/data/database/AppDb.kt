package com.a.atiyah.news.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.a.atiyah.news.data.model.News
import com.a.atiyah.news.utils.Constant.APP_DB_NAME

@Database(entities = [News::class], version = 4, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    companion object{
        private var appDbInstance: AppDb?= null

        fun getDbInstance(ctx: Context): AppDb{
            if (appDbInstance == null)
                appDbInstance =
                    Room.databaseBuilder(ctx.applicationContext, AppDb::class.java, APP_DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()

            return appDbInstance!!
        }
    }

    abstract fun newsDao(): NewsDao
}