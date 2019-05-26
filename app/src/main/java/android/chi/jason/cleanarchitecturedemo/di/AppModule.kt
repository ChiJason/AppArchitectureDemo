package android.chi.jason.cleanarchitecturedemo.di

import android.app.Application
import android.chi.jason.cleanarchitecturedemo.MyApp
import android.chi.jason.cleanarchitecturedemo.db.AppDatabase
import android.chi.jason.cleanarchitecturedemo.db.TaskDao
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideDb(myApp: Application): AppDatabase {
        return Room.databaseBuilder(myApp, AppDatabase::class.java, "task.db")
                .allowMainThreadQueries()
                .build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao {
        return db.getTaskDao()
    }
}