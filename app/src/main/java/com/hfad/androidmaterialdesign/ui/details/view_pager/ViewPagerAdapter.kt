package com.hfad.androidmaterialdesign.ui.details.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

private const val TODAY = 0
private const val YESTERDAY = 1
private const val DAY_BEFORE_YESTERDAY = 2

class ViewPagerAdapter
    (private val fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = arrayOf(TodayFragment(), YesterdayFragment(), DayBeforeYesterday()
    )


    override fun getCount(): Int = fragmentList.size




    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragmentList[TODAY]
            1 -> fragmentList[YESTERDAY]
            2 -> fragmentList[DAY_BEFORE_YESTERDAY]
            else -> fragmentList[TODAY]
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Сегодня"
            1 -> "Вчера"
            2 -> "Позавчера"
            else -> "Сегодня"
            //Не смог разобраться, как здесь вместо хардкода использовать R.strings
        }
    }
}