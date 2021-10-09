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
import com.hfad.androidmaterialdesign.databinding.BeforeYesterdayFragmentStartBinding
import com.hfad.androidmaterialdesign.ui.details.PictureOfTheDayData
import com.hfad.androidmaterialdesign.ui.details.PictureOfTheDayViewModel
import java.text.SimpleDateFormat
import java.util.*


class BeforeYesterdayFragment : Fragment() {

    private var _binding: BeforeYesterdayFragmentStartBinding? = null
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
        _binding = BeforeYesterdayFragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(beforeYesterdayDate()).observe(viewLifecycleOwner, {
            renderData(it)
        })
        binding.beforeYesterdayImageView.setOnClickListener {
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
        constraintSet.clone(context, R.layout.before_yesterday_fragment_end)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(5.0f)
        transition.duration = 1000
        TransitionManager.beginDelayedTransition(
            binding.beforeYesterdayFragmentStartContainer,
            transition
        )
        constraintSet.applyTo(binding.beforeYesterdayFragmentStartContainer)
    }

    private fun hideInfo() {
        shown = false
        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.before_yesterday_fragment_start)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(5.0f)
        transition.duration = 1000
        TransitionManager.beginDelayedTransition(
            binding.beforeYesterdayFragmentStartContainer,
            transition
        )
        constraintSet.applyTo(binding.beforeYesterdayFragmentStartContainer)
    }


    private fun beforeYesterdayDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -2)
        return formatter.format(calendar.time)
    }

    private fun renderData(data: PictureOfTheDayData) = with(binding) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Url is empty")
                } else {
                    beforeYesterdayImageView.load(url) {
                        lifecycle(this@BeforeYesterdayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    beforeYesterdayTitle.text = serverResponseData.title
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
        fun newInstance() = BeforeYesterdayFragment()
    }
}

