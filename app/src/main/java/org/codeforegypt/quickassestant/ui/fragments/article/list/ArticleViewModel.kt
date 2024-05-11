package org.codeforegypt.quickassestant.ui.fragments.article.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.data.model.Article
import org.codeforegypt.quickassestant.domain.repository.IArticleRepo
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepo: IArticleRepo
): ViewModel() {
    private val _article = MutableStateFlow(ArticlesState())
     val article = _article.stateIn(
         viewModelScope,
         SharingStarted.WhileSubscribed(5000),
         ArticlesState()
     )

    private var _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    fun getArticle() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingState.update { true }
            val getArticle = articleRepo.getArticles()
            _loadingState.update { false }
            _article.update {
                it.copy(
                    articles = getArticle
                )
            }
        }
    }

    fun filterArticles(selectedFilter: String?) {
        _loadingState.update { true }
        _article.update { state ->
            _loadingState.update { true }
            if (selectedFilter == null)
                state.copy(
                    filter = null,
                    filteredArticles = null

                )

            else
                state.copy(
                    filter = selectedFilter,
                    filteredArticles = state.articles.filter { it.type == selectedFilter }
                )

        }
        _loadingState.update { false }

    }
}