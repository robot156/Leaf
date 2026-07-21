package io.github.jean.leaf

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication
import io.github.jean.leaf.di.AppGraph

class LeafApplication :
    Application(),
    MetroApplication {
    private val appGraph: AndroidAppGraph by lazy {
        createGraphFactory<AndroidAppGraph.Factory>().create(application = this)
    }

    override val appComponentProviders: MetroAppComponentProviders
        get() = appGraph

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}

val Context.appGraph: AppGraph
    get() = (applicationContext as LeafApplication).appGraph

@DependencyGraph(AppScope::class)
interface AndroidAppGraph :
    AppGraph,
    MetroAppComponentProviders {
    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides application: Application,
        ): AndroidAppGraph
    }
}
