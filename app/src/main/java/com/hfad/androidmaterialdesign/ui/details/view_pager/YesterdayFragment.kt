package com.hfad.androidmaterialdesign.ui.details.view_pager

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.hfad.androidmaterialdesign.R
import com.hfad.androidmaterialdesign.databinding.YesterdayFragmentBinding
import com.hfad.androidmaterialdesign.ui.details.PictureOfTheDayData
import com.hfad.androidmaterialdesign.ui.details.PictureOfTheDayViewModel
import java.text.SimpleDateFormat
import java.util.*

class YesterdayFragment : Fragment() {

    private var _binding: YesterdayFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = YesterdayFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(yesterdayDate()).observe(viewLifecycleOwner, {
            renderData(it)
        })
    }


    private fun yesterdayDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return formatter.format(calendar.time)
    }

    private fun renderData(data: PictureOfTheDayData)= with(binding) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Url is empty")
                } else {
                    yesterdayImageView.load(url) {
                        lifecycle(this@YesterdayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
            }
            is PictureOfTheDayData.Error -> {
                toast(data.error.message)
            }
        }
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = YesterdayFragment()
    }
}