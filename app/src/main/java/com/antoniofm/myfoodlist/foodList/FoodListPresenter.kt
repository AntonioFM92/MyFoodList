package com.antoniofm.myfoodlist.foodList

import android.os.Bundle
import com.antoniofm.myfoodlist.BaseFragment
import com.antoniofm.myfoodlist.Fragment.FragmentObject
import com.antoniofm.myfoodlist.api.FoodRepository
import com.antoniofm.myfoodlist.api.FoodResponse

class FoodListPresenter(var mView: FoodListContract.View?, private val foodRepository: FoodRepository): FoodListContract.Presenter {

    companion object{
        const val KEY_TITLE: String = "KEY_TITLE"
        var mFragmentCount = 0
        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = FoodListFragment()
            args?.let { fragment.arguments = it }
            return fragment
        }
    }

    override fun onViewCreated() {
        // TODO do network calls, db operations etc.
    }

    override fun onDestroy() {
        mView = null
        mFragmentCount--//
    }

    override fun addFragment() {
        mFragmentCount++
        val instance = newInstance(Bundle().also {
            it.putString(
                KEY_TITLE,
                "FoodList $mFragmentCount"
            )
        })
        FragmentObject.addFragment(instance)
    }

    override fun getFoodList(){
        mView?.showProgress()
        foodRepository?.getFoods(object : FoodRepository.RepositoryCallback<List<FoodResponse?>?> {

            override fun onSuccess(foodDto: List<FoodResponse?>?) {
                if (mView!=null) {
                    mView?.hideProgress()
                    mView?.showFoodList(foodDto)
                }

            }

            override fun onError(error: String) {
                if (mView!=null) {
                    mView?.hideProgress()
                }
            }
        })
    }

}