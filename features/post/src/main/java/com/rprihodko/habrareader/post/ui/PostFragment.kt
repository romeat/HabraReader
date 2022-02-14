package com.rprihodko.habrareader.post.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
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
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.rprihodko.habrareader.common.R
import com.rprihodko.habrareader.common.Utils
import com.rprihodko.habrareader.common.Utils.Companion.toStringWithThousands
import com.rprihodko.habrareader.common.dto.PostPage
import com.rprihodko.habrareader.post.databinding.FragmentPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.xml.sax.XMLReader
import java.time.Duration
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

            content.text = HtmlCompat.fromHtml(post.content, HtmlCompat.FROM_HTML_MODE_COMPACT, null, PostTagHandler())
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

        launchGlide()
    }

    object Strike {}

    inner class PostTagHandler : Html.TagHandler {
        override fun handleTag(
            opening: Boolean,
            tag: String?,
            output: Editable?,
            xmlReader: XMLReader?
        ) {
            if(tag.equals("habra-img")) {
                if (opening) {
                    val er = output?.lastIndex?.toString()
                    val d = output?.length!!
                    output?.setSpan(Strike, d, d, Spannable.SPAN_MARK_MARK)
                } else {

                    //val ee = output?.lastIndexOf("<figure>")
                    val b = output?.length!!
                    val wher = output?.getSpanEnd(Strike)!!
                    output?.removeSpan(Strike)

                    val imageContent = output?.substring(wher, b)

                    val tag = saveLinks(imageContent)
                    output?.replace(wher, b, tag)
                    //val image = ContextCompat.getDrawable(this@PostFragment.requireContext(), R.drawable.ic_baseline_bookmark_24)

                }

            }
        }

        private fun saveLinks(imageContent: String): String {
            val srcPattern = "src=\"(https://.*\\..*\\.\\w+)\"[\\s/].*".toRegex()
            val dataSrcPattern = "data-src=\"(https://.*\\..*\\.\\w+)\"[\\s/].*".toRegex()

            var link: String? = if(imageContent.contains("data-src=")) {
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

    private fun launchGlide() {
        imageUrls.forEach{ link ->
            Glide.with(binding.content)
                .asBitmap()
                .load(link)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }

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

                        drawable.setBounds(10, 20, width, height.toInt())

                        val ssb = SpannableStringBuilder(binding.content.text)
                        val start = binding.content.text.indexOf(tag)
                        ssb.setSpan(ImageSpan(drawable), start, start+tag.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        ssb.setSpan(object : ClickableSpan() {
                            override fun onClick(widget: View) {

                                // Handle image click
                                // only for test purposes/upcoming features
                                //Snackbar.make(widget, "image clicked wowee", Snackbar.LENGTH_SHORT).show()
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

