package com.mrntlu.recyclerviewguide.ui

import androidx.fragment.app.Fragment

abstract class BaseFragment<T>: Fragment() {
    protected var _binding: T? = null
    protected val binding get() = _binding!!

    // To prevent memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}