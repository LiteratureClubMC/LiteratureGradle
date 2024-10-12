package rip.sayori.lcmc.lg.tasks

import com.alibaba.fastjson.JSONObject
import net.minecraftforge.binarypatcher.Patcher
import net.minecraftforge.mergetool.Merger
import org.cadixdev.vignette.VignetteMain
import rip.sayori.lcmc.lg.LGExtension
import rip.sayori.lcmc.lg.LiteratureGradle
import rip.sayori.lcmc.lg.utils.UnZip
import rip.sayori.lcmc.lg.utils.UrlUtils
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

open class SetupDeobfWorkspace : Task() {
    override fun action() {
        val installer = UrlUtils.downloadFileCached(
            UrlUtils.CLEANROOM.format(LGExtension.version, LGExtension.version),
            File(LiteratureGradle.getUserCache(), "cleanroom-" + LGExtension.version + ".jar")
        )
        val installerExtractedFolder =
            File(LiteratureGradle.getUserCache(), "cleanroom-" + LGExtension.version + "-installer")
        if (!installerExtractedFolder.exists()) {
            installerExtractedFolder.mkdirs()
            UnZip.unzip(installer, installerExtractedFolder)
        }
        val versionFile = File(installerExtractedFolder,"version.json")
        val versionJson: JSONObject = JSONObject.parseObject(String(versionFile.readBytes()))
        val librariesFolder = File(LiteratureGradle.getUserCache(), "cleanroom-" + LGExtension.version + "-libraries")
        val libraries = ArrayList<File>()
        (versionJson.getJSONArray("libraries").toJavaList(JSONObject::class.java)).forEach { it: JSONObject ->
            //val name = it["name"] as String
            val path = File(librariesFolder,it.getJSONObject("downloads").getJSONObject("artifact").getString("path"))
            val url = it.getJSONObject("downloads").getJSONObject("artifact").getString("url")
            if(url.isNotEmpty()){
                if(!path.parentFile.exists()){
                    path.parentFile.mkdirs()
                }
                libraries.add(UrlUtils.downloadFileCached(url,path))
            }
        }
        val cleanroomJar = File(installerExtractedFolder,"maven/com/cleanroommc/cleanroom/${LGExtension.version}/cleanroom-${LGExtension.version}.jar".replace('/',File.separatorChar))
        val cleanroomJarZIS = ZipInputStream(cleanroomJar.inputStream())
        val binpatches = File(LiteratureGradle.getUserCache(),"binpatches-${LGExtension.version}.pack.lzma")
        val deobfTsrg = File(LiteratureGradle.getUserCache(),"deobf-${LGExtension.version}.tsrg")
        var zipEntry: ZipEntry?
        while((cleanroomJarZIS.nextEntry.also { zipEntry = it })!=null){
            if(zipEntry?.name  == "binpatches.pack.lzma"){
                binpatches.outputStream().write(cleanroomJarZIS.readNBytes(zipEntry!!.size.toInt()))
            }else if (zipEntry?.name  == "deobf_data-1.12.2.tsrg"){
                deobfTsrg.outputStream().write(cleanroomJarZIS.readNBytes(zipEntry!!.size.toInt()))
            }
        }
        val mergedJar = File(LiteratureGradle.getUserCache(),"merged-${LGExtension.version}.jar")
        if(!mergedJar.exists()){
            val clientJar = UrlUtils.downloadFileCached(UrlUtils.CLIENT_JAR,File(LiteratureGradle.getUserCache(),"client.jar"))
            val clientPatched = File(LiteratureGradle.getUserCache(),"client-patched-${LGExtension.version}.jar")
            val serverJar = UrlUtils.downloadFileCached(UrlUtils.SERVER_JAR,File(LiteratureGradle.getUserCache(),"server.jar"))
            val serverPatched = File(LiteratureGradle.getUserCache(),"server-patched-${LGExtension.version}.jar")
            val librariesPaths = ArrayList<String>()
            for(lib in libraries){
                librariesPaths.add("-l")
                librariesPaths.add(lib.path)
            }
            VignetteMain.main(arrayOf("-i",clientJar.path,"-o",clientPatched.path,"--mapping-format","tsrg","-m",deobfTsrg.path) + librariesPaths.toArray(Array(librariesPaths.size) { return@Array "" }))
            VignetteMain.main(arrayOf("-i",serverJar.path,"-o",serverPatched.path,"--mapping-format","tsrg","-m",deobfTsrg.path) + librariesPaths.toArray(Array(librariesPaths.size) { return@Array "" }))
            //FART.main(arrayOf("--input",clientJar.path,"--map",deobfTsrg.path))
            //FART.main(arrayOf("--input",serverJar.path,"--map",deobfTsrg.path))
            val merger = Merger(clientPatched,serverPatched,mergedJar)
            merger.process()
        }
        val patchedMergedJar = File(LiteratureGradle.getUserCache(),"cleanroom-patched-merged-${LGExtension.version}.jar")
        if(patchedMergedJar.exists())
            return

        val patcher = Patcher(mergedJar,patchedMergedJar)
        patcher.pack200(true)
        patcher.loadPatches(binpatches,null)
        patcher.process()
    }
    init {
        finalizedBy("applyAT") //TODO:toMCPNames

    }
}