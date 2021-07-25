package idv.bruce.camera.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import idv.bruce.camera.R
import idv.bruce.camera.databinding.FragmentHostBinding

class HostFragment : Fragment() {
    private lateinit var binding : FragmentHostBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentHostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        for (i in 0 until binding.root.childCount) {
            val v = binding.root.getChildAt(i)
            if (v is TableRow) {
                for (j in 0 until v.childCount) {
                    v.getChildAt(j).setOnClickListener {
                        val tag : String = it.tag as String ?: return@setOnClickListener

                        val cmd : String =
                            if (tag.startsWith("ACTION:")) tag.removePrefix("ACTION:") else return@setOnClickListener

                        when (cmd) {
                            "PREVIEW" -> findNavController().navigate(R.id.action_hostFragment_to_previewFragment)
                            "CAPTURE" -> findNavController().navigate(R.id.action_hostFragment_to_captureFragment)
                            "DUAL" -> findNavController().navigate(R.id.action_hostFragment_to_dualFragment)
                            "QRCODE"->findNavController().navigate(R.id.action_hostFragment_to_qrcodeFragment)
                        }
                    }
                }
            }
        }
    }

}