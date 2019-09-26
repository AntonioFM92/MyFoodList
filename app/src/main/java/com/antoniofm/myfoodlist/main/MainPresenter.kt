package com.antoniofm.myfoodlist.main

import android.os.Bundle
import com.antoniofm.myfoodlist.Fragment.FragmentObject

class MainPresenter(private var mView: MainContract.View?): MainContract.Presenter {

    override fun onViewCreated(isRestored: Boolean) {
        mView?.let {
            FragmentObject.init(it.getMainFragmentManager(), it.getBottomNavigation(), it.getFragmentHolderId(), isRestored)
        }
    }

    override fun onDestroy() {
        mView = null
        FragmentObject.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(FragmentObject.HISTORY_KEY, FragmentObject.getHistory())
        outState.putInt(FragmentObject.CURRENT_TAB_KEY, FragmentObject.getCurrentTab())
    }

    override fun onRestoreInstanceState(savedState: Bundle) {
        savedState.getSerializable(FragmentObject.HISTORY_KEY)?.let { FragmentObject.setHistory(it)  }
        savedState.getInt(FragmentObject.CURRENT_TAB_KEY).let { FragmentObject.setCurrentTab(it) }
    }

}