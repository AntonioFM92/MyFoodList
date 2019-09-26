package com.antoniofm.myfoodlist.main

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.antoniofm.myfoodlist.BasePresenter
import com.antoniofm.myfoodlist.BaseView
import com.google.android.material.bottomnavigation.BottomNavigationView

interface MainContract {

    interface Presenter: BasePresenter {
        fun onViewCreated(isRestored: Boolean)
        fun onSaveInstanceState(outState: Bundle)
        fun onRestoreInstanceState(savedInstanceState: Bundle)
    }

    interface View: BaseView<Presenter> {
        fun getFragmentHolderId(): Int
        fun getMainFragmentManager(): FragmentManager
        fun getBottomNavigation(): BottomNavigationView
        fun finishActivity()
    }

}