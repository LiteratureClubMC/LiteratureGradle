package rip.sayori.lcmc.lg.utils

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import rip.sayori.lcmc.lg.LGExtension
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * @author Enaium
 */
object UrlUtils {
    const val GAME_URL = "https://launchermeta.mojang.com/mc/game/version_manifest_v2.json"
    const val GAME_LIBRARIES = "https://libraries.minecraft.net/"
    const val GAME_RESOURCE = "https://resources.download.minecraft.net"
    const val MCP = "https://maven.outlands.top/releases/de/oceanlabs/mcp/mcp_config/1.12.2/mcp_config-1.12.2.zip"
    const val CLEANROOM = "https://maven.outlands.top/releases/com/cleanroommc/cleanroom/%s/cleanroom-%s-installer.jar"
    const val CLIENT_JAR = "https://launcher.mojang.com/mc/game/1.12.2/client/0f275bc1547d01fa5f56ba34bdc87d981ee12daf/client.jar"
    const val SERVER_JAR = "https://launcher.mojang.com/mc/game/1.12.2/server/886945bfb2b978778c3a0288fd7fab09d315b25f/server.jar"
    const val MCPMAP = "https://github.com/ModCoderPack/MCPMappingsArchive/raw/refs/heads/master/mcp_%s/%s-1.12/mcp_%s-%s-1.12.zip"
    const val ASSETS = "https://launchermeta.mojang.com/mc/assets/1.12/4bdf1632edbb01b4fee0e1311b8f3821cbcff2fc/1.12.json"

    fun readString(link: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val url = URL(link)
            val urlConnection = url.openConnection()
            var connection: HttpURLConnection? = null
            if (urlConnection is HttpURLConnection) {
                connection = urlConnection
            }

            IOUtils.readLines(connection!!.inputStream, StandardCharsets.UTF_8).forEach { line ->
                stringBuilder.append(
                    line
                ).append("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }

    fun readFile(link: String?): ByteArray? {
        var bytes: ByteArray? = null
        try {
            val url = URL(link)
            val urlConnection = url.openConnection()
            var connection: HttpURLConnection? = null
            if (urlConnection is HttpURLConnection) {
                connection = urlConnection
            }

            bytes = IOUtils.toByteArray(connection!!.inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bytes
    }

    fun downloadFileCached(url: String, tgt: File): File {
        if (tgt.exists())
            return tgt
        LGExtension.project?.logger?.lifecycle("Downloading $url")
        FileUtils.copyURLToFile(URL(url), tgt)
        return tgt
    }
}