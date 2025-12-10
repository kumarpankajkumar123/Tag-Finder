package com.example.tagfinderapp.Fragments

import android.R.id.message
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.tagfinderapp.R
import com.example.tagfinderapp.Util.ApkSharingHelper
import com.example.tagfinderapp.databinding.FragmentMoreBinding
import com.google.android.material.button.MaterialButton
import java.io.File


class More : Fragment(), OnClickListener {

    private val apkSharingHelper = ApkSharingHelper(this)

    lateinit var binding: FragmentMoreBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.instagram.setOnClickListener(this)
        binding.facebook.setOnClickListener(this)
        binding.ytube.setOnClickListener(this)
        binding.contact.setOnClickListener(this)
        binding.ShareApp.setOnClickListener(this)
        binding.question.setOnClickListener(this)
        binding.policy.setOnClickListener(this)
        binding.rate.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.instagram.id -> {
                openUrl("https://www.instagram.com/er_pankaj_kumarr/")
            }

            binding.facebook.id -> {
                openUrl("https://www.linkedin.com/in/pankaj-kumar-a5a827224/")
            }

            binding.contact.id -> {
                sendMsg("7417322289", "type..")
            }

            binding.ShareApp.id -> {
//                Log.e("share methods", "call hua")
//                apkSharingHelper.shareApk(
//                    packageName = requireContext().packageName,
//                    authority = "com.example.tagfinderapp.fileprovider"
//                )
                shareApk()
            }

            binding.question.id -> {
                showDialog("question")
            }

            binding.policy.id -> {
                showDialog("policy")
            }

            binding.rate.id -> {
                showDialog("rate")
            }
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            } else {
                // Open directly in the default browser
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Unable to open link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMsg(mobile: String, msg: String) {
        var toNumber = "+91 $mobile" // contains spaces.
        toNumber = toNumber.replace("+", "").replace(" ", "");

        val sendIntent = Intent("android.intent.action.MAIN");
        sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text/plain");
        startActivity(sendIntent)
    }

    fun shareApk() {
        // Path to the APK file
        val apkFile =
            File(Environment.getExternalStorageDirectory(), "Download/app-debug-androidTest.apk")
        Log.e("apk file path", "" + apkFile)

        // Check if the file exists
        if (apkFile.exists()) {
            // Create a Uri from the APK file
            val apkUri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.tagfinderapp.fileprovider",  // Replace with your app's FileProvider authority
                apkFile
            )
            Log.e("apk uri path", "" + apkUri)
            // Create an Intent to share the APK
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/vnd.android.package-archive"
                putExtra(Intent.EXTRA_STREAM, apkUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission to the receiving app
            }

            // Start the chooser activity to allow the user to select an app to share the APK
            startActivity(Intent.createChooser(shareIntent, "Share APK via"))
        } else {
            // If the file doesn't exist, show a message
            Toast.makeText(requireContext(), "APK file not found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog(q: String) {
        if (q.equals("question")) {
            val dialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.questionsanswer, null)
            val dialog = android.app.Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(dialogView)
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btn = dialogView.findViewById<MaterialButton>(R.id.watchTutorial1)
            btn.setOnClickListener {
                try {
                    val intent = Intent(
                        Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "pankajkm347@gmail.com", null
                        )
                    )
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Tag#Finder is not working")
                    intent.putExtra(Intent.EXTRA_TEXT, message)
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"))
                    Toast.makeText(requireContext(), "Thank you for contact me", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "not sending email", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show()
        } else if (q.equals("policy")) {
            val dialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.privacypolicy, null)
            val dialog = android.app.Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(dialogView)
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        } else {
            val dialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.rateapp, null)
            val dialog = android.app.Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(dialogView)
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btn = dialogView.findViewById<MaterialButton>(R.id.searchbtn)
            btn.setOnClickListener {
                val rating = dialogView.findViewById<EditText>(R.id.search_edit_text1)
                val stringrating: String = rating.text?.toString()?.trim().toString()
                Log.e("rating", "" + stringrating)
                if (stringrating.isEmpty()) {
                    Toast.makeText(requireContext(), "please enter rating", Toast.LENGTH_SHORT)
                        .show()

                } else if (stringrating.toInt() > 5) {
                    Toast.makeText(
                        requireContext(),
                        "please enter rating out of 5",
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (stringrating.toInt() < 1) {
                    Toast.makeText(
                        requireContext(),
                        "do not give negative rating",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "Thank you..", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

    }

}