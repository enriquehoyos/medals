package pe.com.medals.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pe.com.medals.data.datastore.DataStoreManager
import pe.com.medals.data.medal.MedalLocalDataSource
import pe.com.medals.data.medal.MedalRepositoryImpl
import pe.com.medals.data.progress.MedalProgressRepositoryImpl
import pe.com.medals.domain.MedalProcessRepository
import pe.com.medals.domain.repository.MedalProgressRepository
import pe.com.medals.domain.repository.MedalRepository
import javax.inject.Singleton

/**
 * Created by Quique on 10/16/2025.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext ctx: Context) = DataStoreManager(ctx)

    @Provides
    @Singleton
    fun provideDefinitionLocal(@ApplicationContext ctx: Context) = MedalLocalDataSource(ctx)

    @Provides
    @Singleton
    fun provideDefinitionRepo(local: MedalLocalDataSource): MedalRepository =
        MedalRepositoryImpl(local)

    @Provides
    @Singleton
    fun provideProgressRepo(ds: DataStoreManager): MedalProgressRepository =
        MedalProgressRepositoryImpl(ds)

    @Provides
    @Singleton
    fun provideMedalRepository(
        defs: MedalRepository,
        prog: MedalProgressRepository
    ) = MedalProcessRepository(defs, prog)
}