package com.antoniofm.myfoodlist.addFoodToList

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.antoniofm.myfoodlist.BaseFragment
import com.antoniofm.myfoodlist.R
import com.antoniofm.myfoodlist.api.FoodRepository
import com.antoniofm.myfoodlist.api.FoodResponse
import com.antoniofm.myfoodlist.foodList.AddingFoodPresenter
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.fragment_addingfood.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddingFoodFragment: BaseFragment(), AddingFoodContract.View {

    private var mPresenter: AddingFoodContract.Presenter? = null
    private lateinit var mRootView : View
    private var currImagePath: String? = null
    private val photoRequest = 111
    private val PERMISSION_REQUEST_CODE: Int = 101

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_addingfood, container, false)
        attachPresenter(AddingFoodPresenter(this, FoodRepository()))
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments?.getString(AddingFoodPresenter.KEY_TITLE,"")
        if(arg?.contains("Root") != true) {
            mRootView.setBackgroundColor(ContextCompat.getColor(mRootView.context, R.color.colorAccent))
        }

        hideProgress()

        text_title.text = mRootView.context.getString(R.string.newFood)

        button_add_fragment.setOnClickListener {
            mPresenter?.launchCamera()
        }

        button_add_food.setOnClickListener {
            if (checkEmptyValues()) {
                Toast.makeText(this@AddingFoodFragment.context!!, context?.getString(R.string.must_fill_all_fields), Toast.LENGTH_SHORT).show()
            }else{
                mPresenter?.postFood(FoodResponse(barcodeT.text.toString().toLong(), foodName.text.toString(), foodBrand.text.toString()))
            }
        }
    }

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun attachPresenter(presenter: AddingFoodContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        loading_progress.visibility=View.VISIBLE
        button_add_food.isClickable = false
    }

    override fun hideProgress() {
        loading_progress.visibility=View.INVISIBLE
        button_add_food.isClickable = true
    }

    override fun launchCamera() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA),PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showCamera()
                } else {
                    Toast.makeText(this@AddingFoodFragment.context!!, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun showCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val file: File = createImageFile()

        val uri: Uri = FileProvider.getUriForFile(
            this@AddingFoodFragment.context!!,
            "com.antoniofm.myfoodlist.fileprovider",
            file
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri)

        startActivityForResult(intent, photoRequest)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = this@AddingFoodFragment.activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currImagePath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == photoRequest && resultCode == RESULT_OK) {
            var photo: Bitmap= BitmapFactory.decodeFile(currImagePath)
            getBarCode(photo)
        }
    }

    private fun getBarCode(photo: Bitmap){
        val image = FirebaseVisionImage.fromBitmap(photo)

        try {
            val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                    FirebaseVisionBarcode.FORMAT_EAN_8,
                    FirebaseVisionBarcode.FORMAT_EAN_13)
                .build()
            val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)


            detector.detectInImage(image)
                .addOnSuccessListener { barcodes ->

                    if (barcodes.count() > 0) {
                        for (barcode in barcodes) {
                            val rawValue = barcode.rawValue
                            barcodeT.text = Editable.Factory.getInstance().newEditable(rawValue.toString())
                        }
                    }else{
                        Toast.makeText(this@AddingFoodFragment.context!!, context?.getString(R.string.invalid_code), Toast.LENGTH_SHORT).show()
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this@AddingFoodFragment.context!!, context?.getString(R.string.invalid_code), Toast.LENGTH_SHORT).show()
                }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkEmptyValues(): Boolean {
        var isSomeValueEmpty = false
        if (foodName.text.toString().isEmpty() || foodBrand.text.toString().isEmpty() || barcodeT.text.toString().isEmpty()) {
            isSomeValueEmpty = true
        }
        return isSomeValueEmpty
    }

    override fun clearValues() {
        foodName.text.clear()
        foodBrand.text.clear()
        barcodeT.text.clear()
    }

    override fun showSuccessfulMessage(message: String){
        Toast.makeText(this@AddingFoodFragment.context!!, message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessage(error: String){

        Toast.makeText(this@AddingFoodFragment.context!!, error, Toast.LENGTH_SHORT).show()
    }

}