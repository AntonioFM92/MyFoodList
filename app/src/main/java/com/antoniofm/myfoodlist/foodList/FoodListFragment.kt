package com.antoniofm.myfoodlist.foodList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.antoniofm.myfoodlist.BaseFragment
import com.antoniofm.myfoodlist.R
import com.antoniofm.myfoodlist.api.FoodRepository
import com.antoniofm.myfoodlist.api.FoodResponse
import kotlinx.android.synthetic.main.fragment_foodlist.*
import kotlinx.android.synthetic.main.fragment_foodlist.view.*

class FoodListFragment: BaseFragment(), FoodListContract.View {

    private var mPresenter: FoodListContract.Presenter? = null
    private lateinit var mRootView : View
    private lateinit var foodAdapter : FoodAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_foodlist, container, false)
        attachPresenter(FoodListPresenter(this, FoodRepository()))

        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var lista: ArrayList<FoodResponse> = arrayListOf()
        foodAdapter = FoodAdapter(lista , mRootView.context)
        mRootView.rvFoodList.adapter = foodAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments?.getString(FoodListPresenter.KEY_TITLE,"")
        if(arg?.contains("Root") != true) {
            mRootView.setBackgroundColor(ContextCompat.getColor(mRootView.context, R.color.colorAccent))
        }

        swipeRefreshLayout.setOnRefreshListener {
            mPresenter?.getFoodList()
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent)

        mPresenter?.getFoodList()

    }

    override fun onDestroyView() {
        detachPresenter()
        super.onDestroyView()
    }

    override fun attachPresenter(presenter: FoodListContract.Presenter) {
        mPresenter = presenter
    }

    override fun detachPresenter() {
        mPresenter?.onDestroy()
        mPresenter = null
    }

    override fun showProgress() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showError() {
        Toast.makeText(this@FoodListFragment.context!!,context?.getString(R.string.try_again), Toast.LENGTH_LONG).show()
    }

    override fun showFoodList(foodDto: List<FoodResponse?>?) {
        foodAdapter = FoodAdapter(foodDto , mRootView.context)
        mRootView.rvFoodList.adapter = foodAdapter

        if(swipeRefreshLayout.isRefreshing){
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onFailure(message: String) {
        Toast.makeText(this@FoodListFragment.context!!,message, Toast.LENGTH_LONG).show()
    }

}