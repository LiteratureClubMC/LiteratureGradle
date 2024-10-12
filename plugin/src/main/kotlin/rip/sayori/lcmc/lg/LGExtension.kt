package rip.sayori.lcmc.lg

import org.gradle.api.Project

object LGExtension {
    var project: Project? = null
    var at: String = ""
    var version: String = "0.2.3-alpha"
    var channel: String = "stable"
    var mapver: String = "39"
    var mainClass: String = "top.outlands.foundation.boot.Foundation"
    var runArgs: String = "--username Dev --version 1.12.2  --assetsDir %s --assetIndex %s --tweakClass net.minecraftforge.fml.common.launcher.FMLTweaker"
}