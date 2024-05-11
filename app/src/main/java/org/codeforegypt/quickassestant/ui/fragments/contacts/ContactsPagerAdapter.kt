package org.codeforegypt.quickassestant.ui.fragments.contacts

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ContactsPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments = ArrayList<Fragment>()

    fun addFragment(fragment: Fragment){
        fragments.add(fragment)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}