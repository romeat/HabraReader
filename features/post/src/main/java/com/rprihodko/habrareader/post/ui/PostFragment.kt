package com.rprihodko.habrareader.post.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
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
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rprihodko.habrareader.common.*
import com.rprihodko.habrareader.common.Utils.Companion.toStringWithThousands
import com.rprihodko.habrareader.common.Utils.Companion.withHttpsPrefix
import com.rprihodko.habrareader.common.dto.PostPage
import com.rprihodko.habrareader.common.navigation.ArgNames
import com.rprihodko.habrareader.post.databinding.FragmentPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.xml.sax.XMLReader
import java.lang.StringBuilder
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackHandlerOnCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)

        binding.myToolbar.initDefault(args.postId.toString()) { findNavController().navigateUp() }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                postViewModel.postData.collect { uiState ->
                    when(uiState) {
                        is PostUiState.Error -> showError()
                        is PostUiState.Loading -> showLoading()
                        is PostUiState.Success -> showData(uiState.post)
                    }
                }
            }
        }

        binding.authorClickableArea.setOnClickListener { v -> postViewModel.onAuthorClick() }

        postViewModel.eventsFlow
            .onEach {
                when(it) {
                    is Event.NavigateToAuthor -> findNavController().navigate(
                        Uri.parse(ArgNames.AUTHOR_DEEP_LINK + it.authorAlias))
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showError() {
        with(binding) {
            postContent.isVisible = false
            errorLabel.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showLoading() {
        with(binding) {
            postContent.isVisible = false
            errorLabel.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showData(post: PostPage) {
        with(binding) {
            postContent.isVisible = true
            errorLabel.isVisible = false
            progressBar.isVisible = false
            author.text = post.author.alias
            postDate.text = Utils.formatTime(post.timePublished)
            title.text = HtmlCompat.fromHtml(post.title, HtmlCompat.FROM_HTML_MODE_COMPACT)

            val styledText = HtmlCompat.fromHtml(
                replaceSpaces(post.content),
                HtmlCompat.FROM_HTML_MODE_LEGACY,
                null,
                PostTagHandler())

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
            .load(post.author.avatarUrl?.withHttpsPrefix)
            .transform(CenterInside(), RoundedCorners(10))
            .placeholder(R.drawable.ic_user_avatar_default_48)
            .into(binding.avatar)

        loadImages()
    }

    // dummy object that marks custom tag position
    object ImageMarker {}

    // dummy object that marks <code> tag position
    object CodeMarker {}

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
                        this.setSpan(ImageMarker, position, position, Spannable.SPAN_MARK_MARK)
                    }
                } else {
                    output?.apply {
                        val start = this.getSpanEnd(ImageMarker)
                        val end = this.length
                        this.removeSpan(ImageMarker)

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
                if (opening) {
                    output?.apply {
                        val position = length
                        setSpan(CodeMarker, position, position, Spannable.SPAN_MARK_MARK)
                    }
                } else {
                    output?.apply {
                        val start = getSpanEnd(CodeMarker)
                        val end = length
                        removeSpan(CodeMarker)
                        setSpan(TypefaceSpan("monospace"), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    }
                }
            }
        }

        private fun saveImageUrl(imageContent: String): String {
            val srcPattern = "src=\"(https://.*\\..*\\.\\w+)\"".toRegex()
            val dataSrcPattern = "data-src=\"(https://.*\\..*\\.\\w+)\"".toRegex()

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

    // because FromHtml is stupid and eats spaces
    private fun replaceSpaces(content: String): String {
        val sb = StringBuilder(content)
        return sb.replace("   ".toRegex(), "&nbsp;&nbsp;&nbsp;")
            .replace("  ".toRegex(), "&nbsp;&nbsp;")
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

