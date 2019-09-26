package com.antoniofm.myfoodlist.Fragment

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.antoniofm.myfoodlist.BaseFragment
import com.antoniofm.myfoodlist.R
import com.antoniofm.myfoodlist.foodList.AddingFoodPresenter
import com.antoniofm.myfoodlist.foodList.FoodListPresenter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.Serializable

object FragmentObject : FragmentContract() {

    const val HISTORY_KEY = "HISTORY_KEY"
    const val CURRENT_TAB_KEY = "CURRENT_TAB_KEY"

    private var mBottomNavigationView: BottomNavigationView? = null
    private var mFragmentManager: FragmentManager? = null
    private var mContainerId: Int = 0
    private var mCurrentTab = 0
    private lateinit var mHistory: Array<ArrayList<String>>

    override fun getHistory(): Array<ArrayList<String>> = mHistory

    override fun setHistory(history: Serializable)  {
        mHistory = history as Array<ArrayList<String>>
    }

    override fun setCurrentTab(index: Int) {
        mCurrentTab = index
    }

    override fun getCurrentTab(): Int = mCurrentTab

    override fun init(fragmentManager: FragmentManager, bottomNavigationView: BottomNavigationView, containerId: Int, isRestored: Boolean) {
        mBottomNavigationView = bottomNavigationView
        mFragmentManager = fragmentManager
        mHistory = Array(bottomNavigationView.menu.size()) { arrayListOf<String>() }
        mContainerId = containerId
        if(!isRestored)
            initRootFragments()
        setBottomNavigationListener()
    }

    override fun addFragment(fragment: BaseFragment, tag: String?, inStack: Boolean?, addAndHide: Boolean?) {
        if(mFragmentManager?.beginTransaction() == null) return
        var ft = mFragmentManager!!.beginTransaction()
        var formattedTag = tag

        val tabIndex = tag!!.split("_")[0].toInt()
        mHistory[tabIndex].add(tag!!)

        ft = ft.add(mContainerId, fragment, formattedTag)

        if (addAndHide == true)
            ft = ft.hide(fragment)
        if (inStack == true)
            ft.commitAllowingStateLoss()
    }

    override fun destroy() {
        mBottomNavigationView = null
        mFragmentManager = null
        mCurrentTab = 0
        mHistory.forEach { it.clear() }
    }

    override fun initRootFragments() {
        val foodListFragment = FoodListPresenter.newInstance(Bundle().also {it.putString(
            FoodListPresenter.KEY_TITLE,"List Root") })
        val addingFoodFragment = AddingFoodPresenter.newInstance(Bundle().also { it.putString(
            AddingFoodPresenter.KEY_TITLE,"Add Root")})
        var tabRootFragments = ArrayList<BaseFragment>()
        tabRootFragments.add(addingFoodFragment)

        //TODO based on tab count , you can add more roots
        tabRootFragments.forEachIndexed { index, fragment ->
            val i = index + 1
            addFragment(fragment, "${i}_0", addAndHide = true)
        }
        addFragment(foodListFragment, "0_0")
    }

    override fun changeTab(index: Int) {
        val activeFragment = getActiveFragment(mCurrentTab)
        val requrestedTabFragment = mFragmentManager?.findFragmentByTag(mHistory[index].last())
        if (activeFragment == null || requrestedTabFragment == null)
            return
        mFragmentManager?.beginTransaction()?.hide(activeFragment)?.show(requrestedTabFragment)?.commitAllowingStateLoss()
        mCurrentTab = index
    }

    override fun getActiveFragment(tabIndex: Int): Fragment? {
        return mFragmentManager?.findFragmentByTag(mHistory[tabIndex].last())
    }

    override fun setBottomNavigationListener() {
        mBottomNavigationView?.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {
                        changeTab(0)
                        return true
                    }
                    R.id.navigation_dashboard -> {
                        changeTab(1)
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun getMenuItemId(tabIndex: Int): Int {
        if(tabIndex==0){
            return R.id.navigation_home
        }else {
            return R.id.navigation_dashboard
        }
    }

}