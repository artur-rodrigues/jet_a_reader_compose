package com.example.jetaeader.di

import com.example.jetaeader.BuildConfig
import com.example.jetaeader.data.api.BookApi
import com.example.jetaeader.data.api.call.ReaderResponseAdapterFactory
import com.example.jetaeader.data.datasource.BookRemoteDataSourceImpl
import com.example.jetaeader.data.datasource.FireBaseBookDataSourceImpl
import com.example.jetaeader.data.datasource.FireBaseUserDataSourceImpl
import com.example.jetaeader.data.repository.BookRepositoryImpl
import com.example.jetaeader.data.repository.FireBaseBookRepositoryImpl
import com.example.jetaeader.data.repository.FireBaseUserRepositoryImpl
import com.example.jetaeader.domain.datasource.BookRemoteDataSource
import com.example.jetaeader.domain.datasource.FireBaseBookDataSource
import com.example.jetaeader.domain.datasource.FireBaseUserDataSource
import com.example.jetaeader.domain.repository.BookRepository
import com.example.jetaeader.domain.repository.FireBaseBookRepository
import com.example.jetaeader.domain.repository.FireBaseUserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModuleProviders {

    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): BookApi {
        return retrofit.create(BookApi::class.java)
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addCallAdapterFactory(ReaderResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(
                if(BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            )
        }
    }

}

@InstallIn(ViewModelComponent::class)
@Module
object FireBaseProviders {

    @Provides
    fun provideFireBaseBookDataSource(): FireBaseBookDataSource {
        return FireBaseBookDataSourceImpl(FirebaseFirestore.getInstance().collection("books"))
    }

    @Provides
    fun provideFireBaseUserDataSource(): FireBaseUserDataSource {
        return FireBaseUserDataSourceImpl(
            Firebase.auth,
            FirebaseFirestore.getInstance().collection("users")
        )
    }
}

@InstallIn(ViewModelComponent::class)
@Module
abstract class AppModulesBindings {

    @Binds
    abstract fun bindBookRemoteDataSource(dataSource: BookRemoteDataSourceImpl) : BookRemoteDataSource

    @Binds
    abstract fun bindBookRepository(repository: BookRepositoryImpl) : BookRepository

    @Binds
    abstract fun bindFireBaseBookRepository(repository: FireBaseBookRepositoryImpl) : FireBaseBookRepository

    @Binds
    abstract fun bindFireBaseUserRepository(repository: FireBaseUserRepositoryImpl) : FireBaseUserRepository
}