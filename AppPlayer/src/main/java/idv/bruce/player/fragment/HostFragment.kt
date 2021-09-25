package idv.bruce.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import idv.bruce.player.R
import idv.bruce.player.databinding.FragmentHostBinding

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
            val v = binding.tabLayout.getChildAt(i)
            if (v is TableRow) {
                for (j in 0 until v.childCount) {
                    v.getChildAt(j).setOnClickListener {
                        val tag : String = it.tag as String ?: return@setOnClickListener

                        val cmd : String =
                            if (tag.startsWith("ACTION:")) tag.removePrefix("ACTION:") else return@setOnClickListener

                        when (cmd) {
                            "VIDEO" -> findNavController().navigate(R.id.action_hostFragment_to_videoFragment)
                            "RTSP" -> findNavController().navigate(R.id.action_hostFragment_to_RTSTFragment)
                            "CUSTOM" -> findNavController().navigate(R.id.action_hostFragment_to_customViewFragment)
                        }
                    }
                }
            }
        }
    }
}