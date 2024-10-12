package rip.sayori.lcmc.lg.tasks

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import org.gradle.internal.impldep.org.objectweb.asm.ClassReader
import org.gradle.internal.impldep.org.objectweb.asm.commons.Remapper
import rip.sayori.lcmc.lg.LGExtension
import rip.sayori.lcmc.lg.LiteratureGradle
import rip.sayori.lcmc.lg.utils.UnZip
import rip.sayori.lcmc.lg.utils.UrlUtils
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class ToMCPNames : Task() {
    override fun action() {
        val patchedMergedTransformedDeobfusizedJar = File("./build","cleanroom-patched-merged-transformed-${LGExtension.channel}-${LGExtension.mapver}-${LGExtension.version}.jar")
        val patchedMergedTransformedJar = File("./build","cleanroom-patched-merged-transformed-${LGExtension.version}.jar")
        if(patchedMergedTransformedDeobfusizedJar.exists())
            return
        val mcp = UrlUtils.downloadFileCached(UrlUtils.MCPMAP.format(LGExtension.channel,LGExtension.mapver,LGExtension.channel,LGExtension.mapver),File(LiteratureGradle.getUserCache(),"mcp-${LGExtension.channel}-${LGExtension.mapver}.zip"))
        val mcpExtracted = File(LiteratureGradle.getUserCache(),"mcp-${LGExtension.channel}-${LGExtension.mapver}")
        UnZip.unzip(mcp,mcpExtracted)
        val methodsFile = File(mcpExtracted, "methods.csv")
        val fieldsFile = File(mcpExtracted, "fields.csv")
        val paramsFile = File(mcpExtracted, "params.csv")
        val methods = CsvReader.builder().ofCsvRecord(methodsFile.reader())
        val fields = CsvReader.builder().ofCsvRecord(fieldsFile.reader())
        val params = CsvReader.builder().ofCsvRecord(paramsFile.reader())
        val remapper: MCPRemapper = MCPRemapper(methods,fields,params)
        patchedMergedTransformedJar.inputStream().use { it ->
            val zipOutputStream = ZipOutputStream(patchedMergedTransformedDeobfusizedJar.outputStream())
            val zipInputStream = ZipInputStream(it)
            var entry: ZipEntry?
            while((zipInputStream.nextEntry.also { entry = it })!=null){
                val entry: ZipEntry = entry!!
                if(entry.name.endsWith(".class")){
                    //val clazzReader = ClassReader(zipInputStream.readAllBytes())
                    TODO("Fuck it!")
                }
            }
        }

    }
    class MCPRemapper(private val methods: CsvReader<CsvRecord>, private val fields: CsvReader<CsvRecord>, val params: CsvReader<CsvRecord>) : Remapper() {
        override fun mapFieldName(owner: String?, name: String?, descriptor: String?): String {
            fields.forEach { it ->
                if(it.getField(0)==name)
                    return it.getField(1)
            }
            return super.mapFieldName(owner, name, descriptor)
        }

        override fun mapMethodName(owner: String?, name: String?, descriptor: String?): String {
            methods.forEach { it ->
                if(it.getField(0)==name)
                    return it.getField(1)
            }
            return super.mapMethodName(owner, name, descriptor)
        }
    }
}