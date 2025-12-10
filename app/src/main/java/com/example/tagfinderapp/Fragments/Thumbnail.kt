package com.example.tagfinderapp.Fragments

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tagfinderapp.Adaptor.DimensionAdapter
import com.example.tagfinderapp.Model.DimensionModel
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.databinding.FragmentThumbnailBinding
import com.squareup.picasso.Picasso

class Thumbnail(val todayModel: VideoModel) : Fragment() {

    lateinit var binding: FragmentThumbnailBinding
    lateinit var adapter: DimensionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentThumbnailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = todayModel.items.firstOrNull()?.snippet?.thumbnails?.standard?.url
        val title = todayModel.items.firstOrNull()?.snippet?.title

        if (imageUrl != null && title != null) {
            binding.materialCardView.isVisible = true
            Picasso.get()
                .load(imageUrl)
                .into(binding.thumbnail)

            binding.description.text = title
        } else {
            binding.noData.isVisible = true
        }

        val thumbnailClassObject = todayModel.items.firstOrNull()?.snippet?.thumbnails
        val standard = thumbnailClassObject?.standard
        val medium = thumbnailClassObject?.medium
        val default = thumbnailClassObject?.default
        val high = thumbnailClassObject?.high
        val maxres = thumbnailClassObject?.maxres

        val standardUrl = standard?.url
        val standardWidth = standard?.width
        val standardHeight = standard?.height

        val mediumUrl = medium?.url
        val mediumWidth = medium?.width
        val mediumHeight = medium?.height

        val defaultUrl = default?.url
        val defaultWidth = default?.width
        val defaultHeight = default?.height

        val highUrl = high?.url
        val highWidth = high?.width
        val highHeight = high?.height

        val maxresUrl = maxres?.url
        val maxerWidth = maxres?.width
        val maxerHeight = maxres?.height


        val dimensionList = listOf(
            DimensionModel(defaultUrl,defaultWidth.toString(),defaultHeight.toString()),
            DimensionModel(mediumUrl,mediumWidth.toString(),mediumHeight.toString()),
            DimensionModel(highUrl,highWidth.toString(),highHeight.toString()),
            DimensionModel(standardUrl,standardWidth.toString(),standardHeight.toString()),
            DimensionModel(maxresUrl,maxerWidth.toString(),maxerHeight.toString())
            )

        binding.dimensionRecycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = DimensionAdapter(requireContext(),dimensionList){ isChecked,count ->
            if (isChecked) {
                binding.content.isVisible = true
            } else {
                binding.content.isVisible = false
            }
            if(count != 0){
                binding.count.isVisible = true
                binding.count.text = count.toString()
            }else{
                binding.count.isVisible = false
                binding.count.text = count.toString()
            }

        }
        binding.dimensionRecycler.adapter = adapter


        binding.cancelall.setOnClickListener {
            clearAllCheckboxes()
        }

        binding.downloadImage.setOnClickListener {
            val urls = adapter.getSelectedUrl()

            if (urls.isEmpty()) {
                return@setOnClickListener
            }

            urls.forEach { url ->
                if(url != null){
                    downloadImage(url)
                }
            }
        }

    }

    fun clearAllCheckboxes() {
        adapter.clearSelection() // Clear all selections in the adapter
    }

    private fun downloadImage(url: String) {
        try {
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("image_${System.currentTimeMillis()}")
                .setDescription("Downloading image")
                .setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    "TagFinder/image_${System.currentTimeMillis()}.jpg"
                )

            val dm = requireContext()
                .getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            adapter.clearSelection()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
            adapter.clearSelection()
        }
    }

}
