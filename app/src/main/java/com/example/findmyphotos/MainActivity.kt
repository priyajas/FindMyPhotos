package com.example.findmyphotos

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.database.Cursor
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.findmyphotos.databinding.ActivityMainBinding
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var binding: ActivityMainBinding
    private val REQUEST = 112

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        imageAdapter = ImageAdapter()
        checkPermission()
        val format = SimpleDateFormat("dd-MM-yyyy")
        val strDate = format.format(Date())
        binding.btnDate.text = strDate.toString()
        val imgList = getImagesPath(this, strDate)
        prepareRecyclerView()
        setImageList(imgList)

    }

    private fun setImageList(imgList: ArrayList<String?>) {
        imageAdapter.setImageList(imgList as ArrayList<String>)
    }

    fun funclick(view: View) {
        pickDateTime()
    }

    private fun prepareRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = imageAdapter
        }
    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST
            )
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { picker, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, picker.year)
                calendar.set(Calendar.MONTH, picker.month)
                calendar.set(Calendar.DATE, picker.dayOfMonth)
                val format = SimpleDateFormat("dd-MM-yyyy")
                val strDate = format.format(calendar.getTime())
                val imgList: ArrayList<String?> = getImagesPath(this, strDate)
                setImageList(imgList)
            },
            startYear,
            startMonth,
            startDay
        ).show()
    }


    fun getImagesPath(activity: Activity, strDate: String): ArrayList<String?> {
        val listOfAllImages = ArrayList<String?>()
        val cursor: Cursor?
        var PathOfImage: String? = null
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf<String>(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        cursor = activity.contentResolver.query(
            uri, projection, null,
            null, null
        )
        val column_index_data: Int = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data)
            val file: File = File(PathOfImage)
            if (file.exists()) {
                val date = file.lastModified()
                val yourDate = Date(date)
                val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
                val formattedDate: String = formatter.format(yourDate)
                if (strDate.equals(formattedDate)) listOfAllImages.add(PathOfImage)
            }
            // listOfAllImages.add(PathOfImage)
        }
        return listOfAllImages
    }
}
