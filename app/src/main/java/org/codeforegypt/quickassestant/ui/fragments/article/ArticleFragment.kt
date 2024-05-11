package org.codeforegypt.quickassestant.ui.fragments.article

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.codeforegypt.quickassestant.databinding.FragmentArticleBinding

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() =  _binding!!

    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        binding.tvContent.text = Html.fromHtml(article.content)
        binding.tvTitle.text = article.title
        Glide.with(binding.root.context)
            .load(article.imgUrl)
            .into(binding.Image)
    }
}