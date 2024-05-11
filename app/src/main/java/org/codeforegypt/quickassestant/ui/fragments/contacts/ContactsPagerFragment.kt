package org.codeforegypt.quickassestant.ui.fragments.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentContactsPagerBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ContactsPagerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentContactsPagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactsPagerAdapter: ContactsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentContactsPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        contactsPagerAdapter = ContactsPagerAdapter(requireActivity())

        contactsPagerAdapter.addFragment(ContactsFragment.newInstance(true))
        contactsPagerAdapter.addFragment(ContactsFragment.newInstance(false))
        binding.vpContacts.adapter = contactsPagerAdapter

        TabLayoutMediator(binding.tabPager, binding.vpContacts) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "General"
                }

                1 -> {
                    tab.text = "Personal"
                }
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactsPagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}