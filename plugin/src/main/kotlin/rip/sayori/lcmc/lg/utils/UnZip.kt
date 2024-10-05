package rip.sayori.lcmc.lg.utils

import java.io.*
import java.util.zip.ZipInputStream


object UnZip {
    private const val BUFFER_SIZE = 4096

    @Throws(IOException::class)
    fun unzip(ZipFilePath: File, Destination_Directory: File) {
        if (!Destination_Directory.exists()) {
            Destination_Directory.mkdir()
        }
        val Zip_Input_Stream = ZipInputStream(FileInputStream(ZipFilePath))
        var Zip_Entry = Zip_Input_Stream.nextEntry

        while (Zip_Entry != null) {
            val File_Path = Destination_Directory.path + File.separator + Zip_Entry.name
            if (!Zip_Entry.isDirectory) {
                extractFile(Zip_Input_Stream, File_Path)
            } else {
                val directory = File(File_Path)
                directory.mkdirs()
            }
            Zip_Input_Stream.closeEntry()
            Zip_Entry = Zip_Input_Stream.nextEntry
        }
        Zip_Input_Stream.close()
    }

    @Throws(IOException::class)
    private fun extractFile(Zip_Input_Stream: ZipInputStream, File_Path: String) {
        val Buffered_Output_Stream =
            BufferedOutputStream(FileOutputStream(File_Path))
        val Bytes = ByteArray(BUFFER_SIZE)
        var Read_Byte: Int
        while ((Zip_Input_Stream.read(Bytes).also { Read_Byte = it }) != -1) {
            Buffered_Output_Stream.write(Bytes, 0, Read_Byte)
        }
        Buffered_Output_Stream.close()
    }
}