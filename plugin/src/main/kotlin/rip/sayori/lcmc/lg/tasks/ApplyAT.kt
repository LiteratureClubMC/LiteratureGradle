package rip.sayori.lcmc.lg.tasks

import net.minecraftforge.accesstransformer.TransformerProcessor
import rip.sayori.lcmc.lg.LGExtension
import rip.sayori.lcmc.lg.LiteratureGradle
import java.io.File

open class ApplyAT : Task() {
    override fun action(){
        val patchedMergedJar = File(LiteratureGradle.getUserCache(),"cleanroom-patched-merged-${LGExtension.version}.jar")
        val patchedMergedTransformedJar = File("./build","cleanroom-patched-merged-transformed-${LGExtension.version}.jar")
        if(LGExtension.at.isNotEmpty()) {
            TransformerProcessor.main(
                "--inJar",
                patchedMergedJar.path,
                "--at",
                LGExtension.at,
                "--outJar",
                patchedMergedTransformedJar.path
            )
        }
        val patchedMergedTransformedDeobfusizedJar = File(LiteratureGradle.getUserCache(),"cleanroom-patched-merged-transformed-${LGExtension.channel}-${LGExtension.mapver}-${LGExtension.version}.jar")
    }
}