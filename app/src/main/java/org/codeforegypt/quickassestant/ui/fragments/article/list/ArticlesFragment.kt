package org.codeforegypt.quickassestant.ui.fragments.article.list

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentArticelsBinding

@AndroidEntryPoint
class ArticlesFragment : Fragment() {

    private lateinit var articlesAdapter: ArticleAdapter

    val articleViewModel: ArticleViewModel by viewModels()

    private var _binding: FragmentArticelsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArticelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoading()
        binding.ivReturn.setOnClickListener {
            findNavController().navigateUp()
        }
        initArticlesList()
        initChips()
        observeOfArticles()
        articleViewModel.getArticle()
    }

    private fun initChips() {
        binding.cgTypes.setOnCheckedStateChangeListener { group, _ ->
            val chip = binding.cgTypes.findViewById<Chip>(group.checkedChipId)
            if (chip == null)
                articleViewModel.filterArticles(null)
            else
                articleViewModel.filterArticles(chip.text.toString())
        }

    }


    private fun initArticlesList() {
        articlesAdapter = ArticleAdapter()
        articlesAdapter.onArticleClicked = { article ->
            Log.d("ArticleE", "$article")
            val action = ArticlesFragmentDirections.actionArticlesFragmentToArticleFragment(article)
            Log.d("ArticleE", "$action")
            findNavController().navigate(action)
        }
        binding.rvArticles.apply {
            setHasFixedSize(true)
            adapter = articlesAdapter
        }
    }


    private fun createChip(type: String): Chip {
        return Chip(requireContext()).apply {
            setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            //textSize = 200f
            setChipDrawable(
                ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    R.style.Colors_Widget_MaterialComponents_Chip_Choice
                )
            )
            isCheckable = true
            text = type
            gravity = Gravity.CENTER
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
    }

    private fun observeOfArticles() {
        viewLifecycleOwner.lifecycleScope.launch {
            articleViewModel.article.collectLatest { articlesState ->
                binding.cgTypes.removeAllViews()
                articlesState.articles.map { it.type }.distinct().forEach { type ->
                    binding.cgTypes.addView(createChip(type))
                }

                if (articlesState.filteredArticles == null)
                    articlesAdapter.submitList(articlesState.articles)
                else
                    articlesAdapter.submitList(articlesState.filteredArticles)
            }
        }
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            articleViewModel.loadingState
                .collectLatest { isLoading ->
                    showLoading(isLoading)
                }
        }

    }
    private fun showLoading(visible: Boolean) {
        binding.flLoading.isVisible = visible
    }
}
