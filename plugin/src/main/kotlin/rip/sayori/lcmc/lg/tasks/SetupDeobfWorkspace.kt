package rip.sayori.lcmc.lg.tasks

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import rip.sayori.lcmc.lg.LGExtension
import rip.sayori.lcmc.lg.LiteratureGradle
import rip.sayori.lcmc.lg.utils.UnZip
import rip.sayori.lcmc.lg.utils.UrlUtils
import java.io.File

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
        (versionJson.getJSONArray("libraries").toJavaList(JSONObject::class.java)).forEach { it: JSONObject ->
            //val name = it["name"] as String
            val path = File(librariesFolder,it.getJSONObject("downloads").getJSONObject("artifact").getString("path"))
            val url = it.getJSONObject("downloads").getJSONObject("artifact").getString("url")
            if(url.isNotEmpty()){
                if(!path.parentFile.exists()){
                    path.parentFile.mkdirs()
                }
                UrlUtils.downloadFileCached(url,path)
            }
        }
    }
}