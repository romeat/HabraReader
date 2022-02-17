package com.rprikhodko.habrareader.home.articles.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.rprihodko.habrareader.common.adapters.PostAdapter
import com.rprihodko.habrareader.common.navigation.ArgNames
import com.rprikhodko.habrareader.R
import com.rprihodko.habrareader.common.network.Period
import com.rprihodko.habrareader.common.network.Rating
import com.rprihodko.habrareader.common.network.SortBy
import com.rprikhodko.habrareader.databinding.FragmentArticlesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticlesFragment : Fragment() {

    private val articlesViewModel by viewModels<ArticlesViewModel>()
    private var _binding: FragmentArticlesBinding? = null

    private val binding get() = _binding!!

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        PostAdapter(PostAdapter.OnClickListener { post -> articlesViewModel.onPostClick(post)})
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)

        binding.postList.adapter = adapter

        adapter.addLoadStateListener { state ->
            binding.progressBar.isVisible = state.refresh == LoadState.Loading
            binding.postList.isVisible = state.refresh is LoadState.NotLoading
            binding.errorLabel.isVisible = state.refresh is LoadState.Error
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                articlesViewModel.posts.collectLatest{ value ->
                    adapter.submitData(value)
                }
            }
        }

        articlesViewModel.eventsFlow
            .onEach {
                when(it) {
                    is Event.RefreshAdapter -> binding.postList.scrollToPosition(0)
                    is Event.NavigateToPost -> {
                        findNavController().navigate(
                            R.id.post, bundleOf(ArgNames.POST_ID_ARG_NAME to it.postId))
                            // for some unknown reason deep link does not work from home stack
                            //Uri.parse(ArgNames.POST_DEEP_LINK + it.postId))
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                articlesViewModel.filtersState.collectLatest{
                    with(binding, {
                        ratingGroup.isVisible = it.ratingVisible
                        ratingSelector.check(ratingEnumToId(it.rating))
                        periodGroup.isVisible = it.periodVisible
                        periodSelector.check(periodEnumToId(it.period))
                        showFirstGroup.isVisible = it.sortByVisible
                        showFirstSelector.check(sortByEnumToId(it.sortBy))

                        expandFiltersButton.isChecked = it.toggleButton
                        expandFiltersButton.text = setFilterDescription(it)
                    })
                }
            }
        }

        return binding.root
    }

    private fun setFilterDescription(filtersState: ArticlesViewModel.FiltersViewState): CharSequence {

        val desc: Int = when(filtersState.sortBy) {
            SortBy.Period -> {
                when(filtersState.period) {
                    Period.Daily -> R.string.best_of_day_description_label
                    Period.Weekly -> R.string.best_of_week_description_label
                    Period.Monthly -> R.string.best_of_month_description_label
                    Period.Yearly -> R.string.best_of_year_description_label
                    Period.AllTime -> R.string.best_of_alltime_description_label
                }
            }
            SortBy.Rating -> {
                when(filtersState.rating) {
                    Rating.AnyRating -> R.string.any_rating_description_label
                    Rating.ZeroPlus -> R.string.zero_plus_description_label
                    Rating.TenPlus -> R.string.ten_plus_description_label
                    Rating.TwentyFivePlus -> R.string.twentyfive_plus_description_label
                }
            }
        }
        return getString(desc)
    }

    override fun onResume() {
        super.onResume()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        with(binding, {
            expandFiltersButton.setOnClickListener {
                articlesViewModel.onFiltersToggle()
            }
            showFirstSelector.setOnCheckedChangeListener { group, checkedId ->
                articlesViewModel.onSortByRadio(sortByIdToEnum(checkedId))
            }
            periodSelector.setOnCheckedChangeListener { group, checkedId ->
                articlesViewModel.onPeriodRadio(periodIdToEnum(checkedId))
            }
            ratingSelector.setOnCheckedChangeListener { group, checkedId ->
                articlesViewModel.onRatingRadio(ratingIdToEnum(checkedId))
            }
            adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                }
                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    binding.postList.scrollToPosition(0)
                }
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    binding.postList.scrollToPosition(0)
                }
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    if(positionStart == 0)
                        binding.postList.scrollToPosition(0)
                }
                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    binding.postList.scrollToPosition(0)
                }
                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                    binding.postList.scrollToPosition(0)
                }
            })
        })
    }

    private fun sortByIdToEnum(id: Int): SortBy {
        return when(id) {
            R.id.newPostsRadioButton -> SortBy.Rating
            R.id.bestPostsRadioButton -> SortBy.Period
            else -> SortBy.Period
        }
    }

    private fun sortByEnumToId(sortBy: SortBy): Int {
        return when(sortBy) {
            SortBy.Rating -> R.id.newPostsRadioButton
            SortBy.Period -> R.id.bestPostsRadioButton
        }
    }

    private fun periodIdToEnum(id: Int): Period {
        return when(id) {
            R.id.dailyRadioButton -> Period.Daily
            R.id.weeklyRadioButton -> Period.Weekly
            R.id.monthlyRadioButton -> Period.Monthly
            R.id.yearlyRadioButton -> Period.Yearly
            R.id.allTimeRadioButton -> Period.AllTime
            else -> Period.Daily
        }
    }

    private fun periodEnumToId(period: Period): Int {
        return when(period) {
            Period.Daily -> R.id.dailyRadioButton
            Period.Weekly -> R.id.weeklyRadioButton
            Period.Monthly -> R.id.monthlyRadioButton
            Period.Yearly -> R.id.yearlyRadioButton
            Period.AllTime -> R.id.allTimeRadioButton
        }
    }

    private fun ratingIdToEnum(id: Int): Rating {
        return when(id) {
            R.id.anyRatingRadioButton -> Rating.AnyRating
            R.id.zeroPlusRadioButton -> Rating.ZeroPlus
            R.id.tenPlusRadioButton -> Rating.TenPlus
            R.id.twentyFivePlusRadioButton -> Rating.TwentyFivePlus
            else -> Rating.AnyRating
        }
    }

    private fun ratingEnumToId(rating: Rating): Int {
        return when(rating) {
            Rating.AnyRating -> R.id.anyRatingRadioButton
            Rating.ZeroPlus -> R.id.zeroPlusRadioButton
            Rating.TenPlus -> R.id.tenPlusRadioButton
            Rating.TwentyFivePlus -> R.id.twentyFivePlusRadioButton
        }
    }
}