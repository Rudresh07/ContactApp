package com.rudy.contactapp.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream


//this function takes the file selected along with the
fun copyUriToInternalStorage(context: Context, uri: Uri, fileName:String): String?{
    val file = File(context.filesDir,fileName)
    return try{
        context.contentResolver.openInputStream(uri)?.use {inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }

        }
        file.absolutePath
    }catch (e:Exception){
        e.printStackTrace()
        null
    }
}