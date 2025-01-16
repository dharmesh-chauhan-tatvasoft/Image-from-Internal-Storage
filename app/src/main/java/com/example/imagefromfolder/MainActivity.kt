package com.example.imagefromfolder

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var imageRecyclerView: RecyclerView
    private lateinit var selectFolderButton: Button
    private lateinit var imagesAdapter: ImagesAdapter
    private var selectedFolder: String = Constants.FOLDER_PATH_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setColorFromPreference()
        initializeDataForImage()
        requestPermission()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestStoragePermissions()
        } else {
            accessFiles()
        }

        // Handle permission for Android 11 and above for MANAGE_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                requestManageStoragePermission()
            } else {
                accessFiles()
            }
        }
    }

    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            Constants.STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestManageStoragePermission() {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        startActivityForResult(intent, Constants.MANAGE_STORAGE_PERMISSION_REQUEST_CODE)
    }

    private fun accessFiles() {
        loadImages()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Constants.STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accessFiles()
                } else {
                    Toast.makeText(this, getString(R.string.storage_permission_denied), Toast.LENGTH_SHORT).show()
                }
            }
            Constants.MANAGE_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                    accessFiles()
                } else {
                    Toast.makeText(this, getString(R.string.manage_all_files_permission_required), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initializeDataForImage() {
        imageRecyclerView = findViewById(R.id.imageRecyclerView)
        selectFolderButton = findViewById(R.id.selectFolderButton)

        imageRecyclerView.layoutManager = GridLayoutManager(this, 3)

        selectFolderButton.setOnClickListener {
            val intent = Intent(this, com.example.imagefromfolder.Settings::class.java)
            startActivityForResult(intent, 1)
        }
    }

    private fun setColorFromPreference() {
        val sharedPreferences = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE)
        val actionBarColor = sharedPreferences.getInt(Constants.ACTION_BAR_COLOR, getColor(R.color.black))
        val statusBarColor = sharedPreferences.getInt(Constants.STATUS_BAR_COLOR, getColor(R.color.white))

        window.statusBarColor = statusBarColor
        supportActionBar?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(actionBarColor))
    }

    private fun loadImages() {
        val images = ArrayList<String>()
        if (selectedFolder.isNotEmpty()) {
            val folder = File(selectedFolder)
            if (folder.exists() && folder.isDirectory) {
                val files = folder.listFiles { file -> file.extension in listOf(Constants.JPEG, Constants.JPG, Constants.PNG) }
                files?.forEach { file ->
                    images.add(file.absolutePath)
                }
                imagesAdapter = ImagesAdapter(images)
                imageRecyclerView.adapter = imagesAdapter
            } else {
                Toast.makeText(this, getString(R.string.folder_not_exist), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            selectedFolder = data?.getStringExtra(Constants.SELECTED_FOLDER) ?: ""
            loadImages()
            setColorFromPreference()
        }
    }
}