package com.example.tagfinderapp.Util

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.core.content.FileProvider
import java.io.*

class ApkSharingHelper(private val fragment: Fragment) {

    private val arrayListApkFilePath = arrayListOf<Uri>()

    fun shareApk(packageName: String, authority: String) {
        // Call the method to retrieve the APK URI
        addApkToShareList(packageName, authority)

        if (arrayListApkFilePath.isNotEmpty()) {
            val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                type = "application/vnd.android.package-archive"
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayListApkFilePath)
            }

            // Launch the chooser for sharing
            fragment.startActivity(
                Intent.createChooser(shareIntent, "Share ${arrayListApkFilePath.size} Files Via")
            )
        } else {
            Log.e("ApkSharingHelper", "No APK file found to share.")
        }
    }

    private fun addApkToShareList(packageName: String, authority: String) {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val packageManager = fragment.requireContext().packageManager
        val pkgAppsList: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

        pkgAppsList.forEach { info ->
            if (info.activityInfo.packageName == packageName) {
                val apkFile = File(info.activityInfo.applicationInfo.publicSourceDir)
                Log.e("file path",""+apkFile)

                val outputDir = File(Environment.getExternalStorageDirectory(), "Downloads")
                Log.e("directly",""+outputDir)
                if (!outputDir.exists()) outputDir.mkdirs()

                val copiedFile = File(outputDir, "${info.loadLabel(packageManager)}.apk")
                Log.e("copiedFile",""+copiedFile)
                if (copyApkFile(apkFile, copiedFile)) {
                    arrayListApkFilePath.add(
                        FileProvider.getUriForFile(
                            fragment.requireContext(),
                            authority,
                            copiedFile
                        )
                    )
                }
            }
        }
    }

    private fun copyApkFile(source: File, destination: File): Boolean {
        return try {
            FileInputStream(source).use { input ->
                FileOutputStream(destination).use { output ->
                    val buffer = ByteArray(4096)
                    var length: Int
                    while (input.read(buffer).also { length = it } > 0) {
                        output.write(buffer, 0, length)
                    }
                }
            }
            Log.d("ApkSharingHelper", "APK copied to: ${destination.absolutePath}")
            true
        } catch (e: IOException) {
            Log.e("ApkSharingHelper", "Error copying APK: ${e.message}")
            false
        }
    }
}
