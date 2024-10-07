package cl.bootcamp.shoestapapp


import android.content.Context
import cl.bootcamp.shoestapapp.repository.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Proveer CartRepository y pasar el contexto
    @Provides
    @Singleton
    fun provideCartRepository(@ApplicationContext context: Context): CartRepository {
        return CartRepository(context)
    }
}
