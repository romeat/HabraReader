package com.rprihodko.habrareader.post.ui

import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.rprihodko.habrareader.common.Utils
import com.rprihodko.habrareader.common.Utils.Companion.toStringWithThousands
import com.rprihodko.habrareader.common.dto.PostPage
import com.rprihodko.habrareader.post.databinding.FragmentPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.xml.sax.XMLReader
import javax.inject.Inject

const val POST_ID_ARG_NAME = "postId"

@AndroidEntryPoint
class PostFragment: Fragment() {

    @Inject
    lateinit var postViewModelAssistedFactory: PostViewModel.AssistedFactory

    val args: PostFragmentArgs by navArgs()

    private val postViewModel: PostViewModel by viewModels {
        PostViewModel.provideFactory(postViewModelAssistedFactory, args.postId)
    }

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                postViewModel.postData.collect { uiState ->
                    when(uiState) {
                        is PostUiState.Error -> showError()
                        is PostUiState.Loading -> showLoading()
                        is PostUiState.Success -> showData(uiState.post)
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError() {

    }

    private fun showLoading() {

    }

    private fun showData(post: PostPage) {
        with(binding) {
            author.text = post.author.alias
            postDate.text = Utils.formatTime(post.timePublished)
            title.text = HtmlCompat.fromHtml(post.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
            content.text = HtmlCompat.fromHtml(post.content, HtmlCompat.FROM_HTML_MODE_COMPACT)
            content.movementMethod = LinkMovementMethod.getInstance()
            votes.text = post.statistics.votesCount.toString()
            views.text = post.statistics.readingCount.toStringWithThousands
            bookmarked.text = post.statistics.favoritesCount.toString()
            comments.text = post.statistics.commentsCount.toString()
        }
    }

    class PostTagHandler : Html.TagHandler {
        override fun handleTag(
            opening: Boolean,
            tag: String?,
            output: Editable?,
            xmlReader: XMLReader?
        ) {

        }
    }
}