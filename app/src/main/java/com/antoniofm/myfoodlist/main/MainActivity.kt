package com.antoniofm.myfoodlist.main

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentManager
import com.antoniofm.myfoodlist.BaseActivity
import com.antoniofm.myfoodlist.R
import com.antoniofm.myfoodlist.foodList.AddingFoodPresenter
import com.antoniofm.myfoodlist.foodList.FoodListPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.View {

    private var mPresenter: MainContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        attachPresenter(MainPresenter(this))
        mPresenter?.onViewCreated(savedInstanceState != null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mPresenter?.onSaveInstanceState(outState)

        outState.putInt("FoodListPresenter.mFragmentCount", FoodListPresenter.mFragmentCount)
        outState.putInt("AddingFoodPresenter.mFragmentCount", AddingFoodPresenter.mFragmentCount)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mPresenter?.onRestoreInstanceState(savedInstanceState)

        FoodListPresenter.mFragmentCount = savedInstanceState.getInt("FoodListPresenter.mFragmentCount")
        AddingFoodPresenter.mFragmentCount = savedInstanceState.getInt("AddingFoodPresenter.mFragmentCount")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        detachPresenter()
        super.onDestroy()
    }

    override fun attachPresenter(presenter: MainContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun finishActivity() {
        finish()
    }

    override fun getMainFragmentManager(): FragmentManager = supportFragmentManager

    override fun getFragmentHolderId(): Int = R.id.container_main

    override fun getBottomNavigation(): BottomNavigationView = nav_view

    override fun showProgress() {}

    override fun hideProgress() {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragments = supportFragmentManager.fragments
        fragments[fragments.size - 1].onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
