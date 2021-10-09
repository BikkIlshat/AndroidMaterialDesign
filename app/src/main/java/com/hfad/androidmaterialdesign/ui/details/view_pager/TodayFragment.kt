package com.hfad.androidmaterialdesign.ui.details.view_pager

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import coil.api.load
import com.hfad.androidmaterialdesign.R
import com.hfad.androidmaterialdesign.databinding.TodayFragmentStartBinding
import com.hfad.androidmaterialdesign.ui.details.PictureOfTheDayData
import com.hfad.androidmaterialdesign.ui.details.PictureOfTheDayViewModel

class TodayFragment : Fragment() {

    private var _binding: TodayFragmentStartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    private var shown = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodayFragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(null).observe(viewLifecycleOwner, {
            renderData(it)
        })
        binding.todayImageView.setOnClickListener {
            if (shown) {
                hideInfo()
            } else {
                showInfo()
            }
        }
    }


    private fun showInfo() {
        shown = true
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.today_fragment_end)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(5.0f)
        transition.duration = 1000
        TransitionManager.beginDelayedTransition(
            binding.todayFragmentStartContainer,
            transition
        )
        constraintSet.applyTo(binding.todayFragmentStartContainer)
    }

    private fun hideInfo() {
        shown = false
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.today_fragment_start)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(5.0f)
        transition.duration = 1000
        TransitionManager.beginDelayedTransition(
            binding.todayFragmentStartContainer,
            transition
        )
        constraintSet.applyTo(binding.todayFragmentStartContainer)
    }


    private fun renderData(data: PictureOfTheDayData)= with(binding)  {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Url is empty")
                } else {
                    todayImageView.load(url) {
                        lifecycle(this@TodayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    todayTitle.text = serverResponseData.title
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
        fun newInstance() = TodayFragment()
    }
}