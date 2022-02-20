package com.example.facedetection

import android.content.Context
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val img: ImageView = findViewById(R.id.imageFace)
        // assets folder image file name with extension
        val fileName = "face-test.jpg"

        // get bitmap from assests folder
        val bitmap: Bitmap? = assetsToBitmap(fileName)
        bitmap?.apply{
            img.setImageBitmap(this)
        }

        val btn: Button = findViewById(R.id.btnTest)
        btn.setOnClickListener {
            val highAccuracyOpts = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE).build()
            val detector = FaceDetection.getClient(highAccuracyOpts)
            val image = InputImage.fromBitmap(bitmap!!,0)
            val result = detector.process(image)
                .addOnSuccessListener { faces ->
                bitmap?.apply { img.setImageBitmap(drawWithRectangles(faces))
                }
            }.addOnFailureListener {e ->
                    //Task failed
                }
        }
    }

    fun Bitmap.drawWithRectangles(faces: List<Face>):Bitmap?{
        val bitmap = copy(config,true)
        val canvas = Canvas(bitmap)
        for (face in faces){
            val bounds = face.boundingBox
            Paint().apply {
                color = Color.RED
                style = Paint.Style.STROKE
                strokeWidth = 4.0f
                isAntiAlias = true
                // draw rectangles
                canvas.drawRect(
                    bounds,
                    this
                )
            }
        }
        return bitmap
    }

    fun Context.assetsToBitmap(fileName: String): Bitmap?{
        return try{
        with(assets.open(fileName)){
            BitmapFactory.decodeStream(this)
        }
        } catch (e: IOException) {null}
    }
}