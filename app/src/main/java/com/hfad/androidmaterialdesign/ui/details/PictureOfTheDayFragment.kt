package com.hfad.androidmaterialdesign.ui.details

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.*
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hfad.androidmaterialdesign.ui.MainActivity
import com.hfad.androidmaterialdesign.R
import com.hfad.androidmaterialdesign.databinding.MainFragmentBinding
import com.hfad.androidmaterialdesign.databinding.MainFragmentStartBinding
import com.hfad.androidmaterialdesign.ui.collapsing_toolbar.CollapsingToolbarActivity
import com.hfad.androidmaterialdesign.ui.details.view_pager.ViewPagerAdapter
import com.hfad.androidmaterialdesign.ui.settings.SettingsFragment

const val BOTTOM_SHEET_HEADER = "BottomSheetHeader"
const val BOTTOM_SHEET_CONTENT = "BottomSheetContent"

class PictureOfTheDayFragment : Fragment() {

    private var _binding: MainFragmentStartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetHeader: TextView
    private lateinit var bottomSheetContent: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(null).observe(viewLifecycleOwner, {
            renderData(it)
        })
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}"
                )
            })
        }
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        setBottomSheetBehaviour(view.findViewById(R.id.bottom_sheet_container))
        bottomSheetHeader = view.findViewById(R.id.bottom_sheet_description_header)
        bottomSheetContent = view.findViewById(R.id.bottom_sheet_description)
        setBottomAppBar(view)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun renderData(data: PictureOfTheDayData) = with(binding) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                main.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    toast("Url is empty")
                } else {
                    if (serverResponseData.title != null && serverResponseData.explanation != null) {
                        setBottomSheetTextSpanned(
                            serverResponseData.title,
                            serverResponseData.explanation
                        )
                    }
                }
            }

            is PictureOfTheDayData.Loading -> {
                main.visibility = View.GONE
                loadingLayout.visibility = View.VISIBLE
            }
            is PictureOfTheDayData.Error -> {
                main.visibility = View.VISIBLE
                loadingLayout.visibility = View.GONE
                toast(data.error.message)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun setBottomSheetTextSpanned(title: String, explanation: String) {
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(
            BackgroundColorSpan(resources.getColor(R.color.lime_green)),
            0, spannableTitle.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableTitle.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.dark_slate_blue)),
            0, spannableTitle.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableTitle.setSpan(
            StyleSpan(Typeface.BOLD_ITALIC),
            0, spannableTitle.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableTitle.setSpan(
            UnderlineSpan(),
            0, spannableTitle.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableTitle.setSpan(
            RelativeSizeSpan(1.5f),
            0, spannableTitle.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        bottomSheetHeader.text = spannableTitle

        val spannableContent = SpannableString(explanation)
        spannableContent.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            0, spannableContent.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableContent.setSpan(
            RelativeSizeSpan(1.2f),
            0, spannableContent.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        bottomSheetContent.text = spannableContent
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setBottomSheetBehaviour(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> toast("STATE_DRAGGING")
                    BottomSheetBehavior.STATE_COLLAPSED -> toast("STATE_COLLAPSED")
                    BottomSheetBehavior.STATE_EXPANDED -> toast("STATE_EXPANDED")
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> toast("STATE_HALF_EXPANDED")
                    BottomSheetBehavior.STATE_HIDDEN -> toast("STATE_HIDDEN")
                    BottomSheetBehavior.STATE_SETTLING -> toast("STATE_SETTLING")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(context, CollapsingToolbarActivity::class.java)
        intent.putExtra(BOTTOM_SHEET_HEADER, bottomSheetHeader.text)
        intent.putExtra(BOTTOM_SHEET_CONTENT, bottomSheetContent.text)
        when (item.itemId) {
            R.id.app_bar_fav -> startActivity(intent)
            R.id.app_bar_settings -> activity?.apply {
                this.supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, SettingsFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
            R.id.app_bar_search -> toast(getString(R.string.search))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) = with(binding) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottomAppBar.navigationIcon = null
                bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                bottomAppBar.navigationIcon = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_hamburger_menu_bottom_bar
                )
                bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
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
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }
}