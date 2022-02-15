package com.rprihodko.habrareader.post.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rprihodko.habrareader.common.QuoteSpanClass
import com.rprihodko.habrareader.common.R
import com.rprihodko.habrareader.common.Utils
import com.rprihodko.habrareader.common.Utils.Companion.toStringWithThousands
import com.rprihodko.habrareader.common.dto.PostPage
import com.rprihodko.habrareader.post.databinding.FragmentPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.xml.sax.XMLReader
import javax.inject.Inject

@AndroidEntryPoint
class PostFragment: Fragment() {

    val imageUrls = mutableListOf<String>()

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

            val styledText = HtmlCompat.fromHtml(post.content, HtmlCompat.FROM_HTML_MODE_LEGACY, null, PostTagHandler())
            replaceQuoteSpans(styledText as Spannable)
            content.text = styledText
            content.movementMethod = LinkMovementMethod.getInstance()

            val stats = post.statistics
            formatScore(stats.score)
            views.text = stats.readingCount.toStringWithThousands
            bookmarked.text = stats.favoritesCount.toString()
            comments.text = stats.commentsCount.toString()

        }
        Glide.with(binding.avatar)
            .load("https:".plus(post.author.avatarUrl))
            .transform(CenterInside(), RoundedCorners(10))
            .placeholder(R.drawable.ic_user_avatar_default_48)
            .into(binding.avatar)

        loadImages()
    }

    // dummy object that marks custom tag position
    object Strike {}

    inner class PostTagHandler : Html.TagHandler {
        override fun handleTag(
            opening: Boolean,
            tag: String?,
            output: Editable?,
            xmlReader: XMLReader?
        ) {
            if(tag.equals(IMAGE_CUSTOM_TAG)) {
                if (opening) {
                    output?.apply {
                        val position = this.length
                        this.setSpan(Strike, position, position, Spannable.SPAN_MARK_MARK)
                    }
                } else {
                    output?.apply {
                        val start = this.getSpanEnd(Strike)
                        val end = this.length
                        this.removeSpan(Strike)

                        val imageContent = this.substring(start, end)
                        val imageTag = saveImageUrl(imageContent)
                        this.replace(start, end, imageTag)
                    }
                }
            }

            if(tag.equals("hr")) {
                // TODO insert horizontal line
            }

            if(tag.equals("ol")) {
                // TODO add tag support
                // https://gist.github.com/vincent1086/1ca4df897efcf9bc0a4bda771af5636a
            }

            if(tag.equals("code")) {
                // TODO add code formatting
                val t = tag
            }
        }

        private fun saveImageUrl(imageContent: String): String {
            val srcPattern = "src=\"(https://.*\\..*\\.\\w+)\"[\\s/].*".toRegex()
            val dataSrcPattern = "data-src=\"(https://.*\\..*\\.\\w+)\"[\\s/].*".toRegex()

            val link: String? = if(imageContent.contains("data-src=")) {
                dataSrcPattern.find(imageContent)?.groupValues?.get(1)
            } else {
                srcPattern.find(imageContent)?.groupValues?.get(1)
            }

            link?.let {
                imageUrls.add(it)
                return it.substringAfterLast("/")
            }
            return ""
        }
    }

    private fun replaceQuoteSpans(spannable: Spannable)
    {
        val quoteSpans: Array<QuoteSpan> =
            spannable.getSpans(0, spannable.length - 1, QuoteSpan::class.java)

        for (quoteSpan in quoteSpans)
        {
            val start: Int = spannable.getSpanStart(quoteSpan)
            val end: Int = spannable.getSpanEnd(quoteSpan)
            val flags: Int = spannable.getSpanFlags(quoteSpan)
            spannable.removeSpan(quoteSpan)
            spannable.setSpan(
                QuoteSpanClass(ContextCompat.getColor(requireContext(), R.color.colorBrand), 10f, 10f),
                start, end, flags
            )
        }
    }

    private fun loadImages() {
        imageUrls.forEach{ link ->
            Glide.with(binding.content)
                .asBitmap()
                .load(link)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onLoadCleared(placeholder: Drawable?) { }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val tag = link.substringAfterLast("/")
                        val drawable = BitmapDrawable(resources, resource)

                        val width = Resources.getSystem().displayMetrics.widthPixels - 50
                        val aspectRatio: Float =
                            (drawable.intrinsicWidth.toFloat()) / (drawable.intrinsicHeight.toFloat())
                        val height = width / aspectRatio

                        drawable.setBounds(0, 0, width, height.toInt())

                        val ssb = SpannableStringBuilder(binding.content.text)
                        val start = binding.content.text.indexOf(tag)
                        ssb.setSpan(ImageSpan(drawable), start, start+tag.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        ssb.setSpan(object : ClickableSpan() { // only for test purposes/upcoming features
                            override fun onClick(widget: View) {
                                //Snackbar.make(widget, "image clicked", Snackbar.LENGTH_SHORT).show()
                            }
                        }, start, start+tag.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        binding.content.text = ssb
                    }
                })
        }
    }

    private fun formatScore(score: Int) {
        formatScoreText(score)
        formatScoreColor(score)
    }

    private fun formatScoreText(score: Int) {
        binding.score.text = score.let{
            when {
                it > 0 -> {
                    "+$it"
                }
                else -> it.toString()
            }
        }
    }

    private fun formatScoreColor(score: Int) {
        val color: Int? = score.let {
            when {
                it > 0 -> { R.color.colorGreen }
                it < 0 -> { R.color.colorRed }
                else -> null
            }
        }
        color?.let {
            binding.score.setTextColor(ContextCompat.getColor(requireContext(), it))
        }
    }
}

