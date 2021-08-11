package idv.bruce.code1922.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import idv.bruce.code1922.databinding.FragmentQrcodeBinding
import idv.bruce.code1922.ui_com.PreviewFragment
import idv.bruce.code1922.viewmodel.DataViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.concurrent.Executors

class QrcodeFragment : PreviewFragment() {
    companion object {
        private const val TAG = "QrcodeFragment"
    }

    private lateinit var binding : FragmentQrcodeBinding

    private lateinit var imageAnalysis : ImageAnalysis

    private var toast : Toast? = null

    private val dataViewModel : DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentQrcodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.preview.surfaceProvider)
        }

        imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(Executors.newSingleThreadExecutor(), QrcodeAnalyzer(binding.mask.area))
            }

        initCamera()
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun setupUseCase() {
        super.setupUseCase()

        cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, imageAnalysis)
    }

    override fun onStart() {
        super.onStart()
        startScan()
    }


    private fun startScan() {
        if (!isInit) return
        MainScope().launch {
            if (!cameraProvider.isBound(imageAnalysis))
                cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, imageAnalysis)
        }
    }

    private fun stopScan() {
        if (!isInit) return
        MainScope().launch {
            if (cameraProvider.isBound(imageAnalysis))
                cameraProvider.unbind(imageAnalysis)
        }

    }

    private fun onScan(barcode : Barcode) {

        Log.d(TAG, "Barcode type : ${barcode.valueType}")
        when (barcode.valueType) {
            Barcode.TYPE_SMS -> {
                val sms = barcode.sms ?: return
                if (sms.phoneNumber != "1922") {
                    onScanError()
                    startScan()
                    return
                }

                dataViewModel.onCodeScanned(sms.message!!)
            }
            else -> {
                onScanError()
                startScan()
            }
        }
    }

    private fun onScanError() {
        MainScope().launch {
            if (toast != null)
                toast?.cancel()

            toast = Toast.makeText(requireContext(), "暫時僅用於1922實名登記QRCODE", Toast.LENGTH_LONG)
            toast?.show()
        }

    }

    private inner class QrcodeAnalyzer(private val area : Rect? = null) : ImageAnalysis.Analyzer {
        private val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        private val scanner = BarcodeScanning.getClient(options)

        private var scanArea : Rect? = null

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy : ImageProxy) {

            val mediaImage = imageProxy.image
            if (mediaImage != null) {


                if (scanArea == null) {
                    val previewRect : Rect = imageProxy.cropRect
                    val size : Int =
                        (previewRect.width().coerceAtMost(previewRect.height()) * 0.95).toInt()

                    scanArea = Rect(
                        (previewRect.width() - size) / 2,
                        (previewRect.height() - size) / 2,
                        (previewRect.width() + size) / 2,
                        (previewRect.height() + size) / 2
                    )
                }

                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                val task : Task<MutableList<Barcode>> = scanner.process(image)

                Tasks.await(task)

                for (barcode in task.result) {
                    if (scanArea!!.contains(barcode.boundingBox ?: continue)) {
                        stopScan()
                        Log.d(TAG, "Scanned : ${barcode.rawValue}\n${barcode.boundingBox}")
                        onScan(barcode)
                    }
                }

            }
            imageProxy.close()
        }
    }
}