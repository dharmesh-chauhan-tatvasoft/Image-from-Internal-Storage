package com.example.imagefromfolder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class Settings : AppCompatActivity() {
    private lateinit var selectFolderSpinner: Spinner
    private lateinit var statusBarColorSpinner: Spinner
    private lateinit var actionBarColorSpinner: Spinner
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSpinnerData()
    }

    private fun setSpinnerData() {
        selectFolderSpinner = findViewById(R.id.selectFolderSpinner)
        statusBarColorSpinner = findViewById(R.id.statusBarColorSpinner)
        actionBarColorSpinner = findViewById(R.id.actionBarColorSpinner)
        saveButton = findViewById(R.id.saveSettings)

        val folders = listOf(Constants.FOLDER_PATH_1, Constants.FOLDER_PATH_2, Constants.FOLDER_PATH_3)
        val folderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, folders)
        folderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        selectFolderSpinner.adapter = folderAdapter

        val colorsList = listOf(getString(R.string.red), getString(R.string.blue), getString(R.string.green), getString(R.string.yellow))
        val statusBarColorAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, colorsList)
        statusBarColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusBarColorSpinner.adapter = statusBarColorAdapter

        val actionBarColorAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, colorsList)
        actionBarColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        actionBarColorSpinner.adapter = actionBarColorAdapter

        saveButton.setOnClickListener {
            val selectedFolder = selectFolderSpinner.selectedItem.toString()
            val selectedStatusBarColor = getColorFromString(statusBarColorSpinner.selectedItem.toString())
            val selectedActionBarColor = getColorFromString(actionBarColorSpinner.selectedItem.toString())

            val sharedPreferences = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(Constants.SELECTED_FOLDER, selectedFolder)
            editor.putInt(Constants.STATUS_BAR_COLOR, selectedStatusBarColor)
            editor.putInt(Constants.ACTION_BAR_COLOR, selectedActionBarColor)
            editor.apply()

            val resultIntent = Intent()
            resultIntent.putExtra(Constants.SELECTED_FOLDER, selectedFolder)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun getColorFromString(colorName: String): Int {
        return when (colorName) {
            getString(R.string.red) -> getColor(R.color.red)
            getString(R.string.blue) -> getColor(R.color.blue)
            getString(R.string.green) -> getColor(R.color.green)
            getString(R.string.yellow) -> getColor(R.color.yellow)
            else -> getColor(R.color.purple_700)
        }
    }
}