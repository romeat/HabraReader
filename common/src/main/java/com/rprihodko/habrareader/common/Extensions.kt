package com.rprihodko.habrareader.common

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import java.lang.Exception

fun ViewModel.launchTryCatch(tryAction: () -> Unit, catchAction: (e: Exception) -> Unit) {
    viewModelScope.launch {
        try {
            tryAction()
        } catch (e: Exception) {
            catchAction(e)
        }
    }
}

fun Toolbar.initWithBackButton(title: String, backAction: () -> Unit) {
    this.initRootToolbar(title)
    this.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
    this.setNavigationOnClickListener { backAction.invoke() }
}

fun Toolbar.initRootToolbar(title: String) {
    this.setTitleTextColor(ContextCompat.getColor(this.context, R.color.colorToolbarText))
    this.title = title
}

fun Fragment.setBackHandlerOnCreate() {
    activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigateUp()
        }
    })
}