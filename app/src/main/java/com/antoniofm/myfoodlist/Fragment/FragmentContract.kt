package com.antoniofm.myfoodlist.Fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.antoniofm.myfoodlist.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.Serializable

abstract class FragmentContract {
    // bottom navigation related
    abstract fun init(fragmentManager: FragmentManager, bottomNavigationView: BottomNavigationView, containerId: Int, isRestored: Boolean)
    abstract fun getCurrentTab(): Int
    abstract fun setCurrentTab(index: Int)
    abstract fun changeTab(index: Int)
    abstract fun setBottomNavigationListener()
    abstract fun getMenuItemId(tabIndex: Int): Int

    // fragment manager related
    abstract fun addFragment(fragment: BaseFragment, tag: String? = null, inStack: Boolean? = true, addAndHide: Boolean? = false)
    abstract fun initRootFragments()
    abstract fun getHistory(): Array<ArrayList<String>>
    abstract fun setHistory(history: Serializable)
    abstract fun getActiveFragment(tabIndex: Int): Fragment?
    abstract fun destroy()
}