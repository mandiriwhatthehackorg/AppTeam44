package id.co.bankmandiri.common.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * @author hendrawd on 24 Mar 2019
 */
inline fun <reified T : ViewModel> Fragment.viewModel(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}