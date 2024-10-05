package rip.sayori.lcmc.lg.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class Task : DefaultTask() {
    @TaskAction
    abstract fun action()

    companion object {
        val tasks = HashMap<String,Class<out Task>>()

        init {
            tasks.put("setupDeobfWorkspace",SetupDeobfWorkspace::class.java)
        }
    }
}