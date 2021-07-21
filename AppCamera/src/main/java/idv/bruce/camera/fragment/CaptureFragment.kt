package idv.bruce.camera.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.navigation.fragment.findNavController
import idv.bruce.camera.R
import idv.bruce.camera.databinding.FragmentCaptureBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class CaptureFragment : PreviewFragment() {
    companion object {
        private const val TAG = "CaptureFragment"
    }

    private lateinit var binding : FragmentCaptureBinding

    private lateinit var imageCapture : ImageCapture

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentCaptureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.preview.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder().build().apply {
            targetRotation = binding.preview.display.rotation
        }

        binding.capture.setOnClickListener {

            imageCapture.takePicture(
                Executors.newSingleThreadExecutor(),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image : ImageProxy) {
                        val planeProxy : ImageProxy.PlaneProxy = image.planes[0]
                        val buffer = planeProxy.buffer
                        val bytes = ByteArray(buffer.remaining())
                        buffer[bytes]

                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                        val matrix: Matrix = Matrix()

                        matrix.postRotate(image.imageInfo.rotationDegrees.toFloat())

                        val rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                        bitmap.recycle()

                        val bundle : Bundle = Bundle().apply {
                            putParcelable("picture", rotateBitmap)
                        }
                        MainScope().launch {
                            findNavController().navigate(
                                R.id.action_captureFragment_to_pictureFragment,
                                bundle
                            )
                        }

                    }

                    override fun onError(exception : ImageCaptureException) {
                        MainScope().launch {
                            Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })
        }

        initCamera()
    }

    override fun setupUseCase() {
        super.setupUseCase()
        cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, imageCapture)
    }
}