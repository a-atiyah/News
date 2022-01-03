package com.a.atiyah.news.data.dependencyInjection

import android.content.Context
import com.a.atiyah.news.data.database.AppDb
import com.a.atiyah.news.data.database.NewsDao
import com.a.atiyah.news.data.network.NewsApi
import com.a.atiyah.news.utils.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun retrofitInstance(retrofit: Retrofit) : NewsApi {
        return retrofit.create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun retroInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun appDbInstance(@ApplicationContext ctx: Context): AppDb {
        return AppDb.getDbInstance(ctx)
    }

    @Provides
    @Singleton
    fun newsDao(appDb: AppDb): NewsDao {
        return appDb.newsDao()
    }
}