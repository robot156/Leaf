import com.android.build.api.dsl.ApplicationExtension
import io.github.jean.leaf.LeafConfig
import io.github.jean.leaf.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)

                defaultConfig.apply {
                    applicationId = LeafConfig.APPLICATION_ID
                    targetSdk = LeafConfig.TARGET_SDK
                    versionCode = LeafConfig.VERSION_CODE
                    versionName = LeafConfig.VERSION_NAME
                }
            }
        }
    }
}
