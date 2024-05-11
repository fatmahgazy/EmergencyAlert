package org.codeforegypt.quickassestant.ui.fragments.article.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.codeforegypt.quickassestant.databinding.ListArticlesBinding
import com.bumptech.glide.Glide
import org.codeforegypt.quickassestant.data.model.Article

class ArticleAdapter: RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>(){

    private var list: List<Article> = emptyList()

    internal var onArticleClicked: (Article) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(ListArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentArticle = list[position]
        holder.bind(currentArticle, onArticleClicked)
    }

    fun submitList(newList: List<Article>) {
       list = newList
        notifyDataSetChanged() // Notify adapter about the change
    }


    class ArticleViewHolder(val binding: ListArticlesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, onArticleClicked: (Article) -> Unit) {
            Glide.with(binding.root.context)
                .load(article.imgUrl)
                .into(binding.Image)
            binding.tvTitle.text = article.title

            binding.root.setOnClickListener {
                onArticleClicked(article)
            }
        }
    }
}