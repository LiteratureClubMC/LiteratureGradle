package rip.sayori.lcmc.lg

import org.gradle.api.Plugin
import org.gradle.api.Project
import rip.sayori.lcmc.lg.tasks.Task
import rip.sayori.lcmc.lg.utils.UrlUtils.GAME_LIBRARIES
import java.io.File

/**
 * Just Monika!
 */
class LiteratureGradle : Plugin<Project> {
    override fun apply(target: Project) {
        target.logger.lifecycle("Literature Gradle By LiteratureClubMC")
        LGExtension.project = target
        target.extensions.add("minecraft", LGExtension)
        target.afterEvaluate {
            afterEvaluate(target)
        }
    }

    private fun afterEvaluate(project: Project) {
        project.repositories.maven { mavenArtifactRepository ->
            mavenArtifactRepository.name = "minecraft"
            mavenArtifactRepository.setUrl(GAME_LIBRARIES)
        }

        project.repositories.maven { mavenArtifactRepository ->
            mavenArtifactRepository.name = "Enaium"
            mavenArtifactRepository.setUrl("https://maven.enaium.cn/")
        }

        project.repositories.maven { mavenArtifactRepository ->
            mavenArtifactRepository.name = "SpongePowered"
            mavenArtifactRepository.setUrl("https://repo.spongepowered.org/maven/")
        }

        project.repositories.maven { mavenArtifactRepository ->
            mavenArtifactRepository.name = "Cleanroom Release"
            mavenArtifactRepository.setUrl("https://repo.cleanroommc.com/releases")
        }

        project.repositories.maven { mavenArtifactRepository ->
            mavenArtifactRepository.name = "Cleanroom Snapshot"
            mavenArtifactRepository.setUrl("https://repo.cleanroommc.com/snapshot")
        }

        project.repositories.maven { mavenArtifactRepository ->
            mavenArtifactRepository.name = "Cleanroom Snapshot"
            mavenArtifactRepository.setUrl("https://maven.outlands.top/releases")
        }

        Task.tasks.forEach {
            project.tasks.create(it.key,it.value)
        }
    }

    companion object {
        fun getUserCache(): File {
            val userCache =
                File(LGExtension.project?.gradle?.gradleUserHomeDir, "caches" + File.separator + "lg")

            if (!userCache.exists()) {
                userCache.mkdirs()
            }

            return userCache
        }
    }
}