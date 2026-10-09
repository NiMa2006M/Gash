package com.example.gash.core.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfileImageStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun saveFromUri(sourceUri: String): String = withContext(Dispatchers.IO) {
        val uri = Uri.parse(sourceUri)
        val resolver = context.contentResolver

        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
            ?: throw IOException("Cannot open image")
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) throw IOException("Invalid image")

        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = calculateSampleSize(bounds.outWidth, bounds.outHeight)
        }
        val decoded = resolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, decodeOptions)
        } ?: throw IOException("Cannot decode image")

        val rotation = resolver.openInputStream(uri)?.use { readRotationDegrees(it) } ?: 0
        val upright = if (rotation != 0) {
            val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
            Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
        } else decoded

        val side = minOf(upright.width, upright.height)
        val square = Bitmap.createBitmap(
            upright, (upright.width - side) / 2, (upright.height - side) / 2, side, side
        )
        val finalBitmap = if (side > MAX_SIDE_PX) {
            Bitmap.createScaledBitmap(square, MAX_SIDE_PX, MAX_SIDE_PX, true)
        } else square

        val dir = File(context.filesDir, DIR_NAME).apply { mkdirs() }
        val file = File(dir, "profile_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            if (!finalBitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out)) {
                throw IOException("Failed to compress image")
            }
        }
        file.absolutePath
    }

    fun delete(path: String) {
        runCatching { File(path).delete() }
    }

    private fun calculateSampleSize(width: Int, height: Int): Int {
        var sample = 1
        val shortSide = minOf(width, height)
        while (shortSide / (sample * 2) >= MAX_SIDE_PX) sample *= 2
        return sample
    }

    private fun readRotationDegrees(input: InputStream): Int = runCatching {
        when (ExifInterface(input).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }.getOrDefault(0)

    private companion object {
        const val DIR_NAME = "profile"
        const val MAX_SIDE_PX = 512
        const val JPEG_QUALITY = 90
    }
}