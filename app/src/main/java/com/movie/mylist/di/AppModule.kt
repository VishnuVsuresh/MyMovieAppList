package com.movie.mylist.di

import android.content.Context
import androidx.room.Room
import com.DATABASE_NAME
import com.movie.mylist.database.local.MovieAppDataBase
import com.movie.mylist.database.local.MovieConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun provideActivityContext(@ActivityContext context: Context): Context {
        return context
    }


    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): MovieAppDataBase {
        return Room.databaseBuilder(
            context,
            MovieAppDataBase::class.java, DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }


}