package com.antoniofm.myfoodlist.foodList

import android.os.Bundle
import com.antoniofm.myfoodlist.BaseFragment
import com.antoniofm.myfoodlist.Fragment.FragmentObject
import com.antoniofm.myfoodlist.addFoodToList.AddingFoodContract
import com.antoniofm.myfoodlist.addFoodToList.AddingFoodFragment
import com.antoniofm.myfoodlist.api.FoodRepository
import com.antoniofm.myfoodlist.api.FoodResponse

class AddingFoodPresenter(var mView: AddingFoodContract.View?, private val foodRepository: FoodRepository): AddingFoodContract.Presenter {

    companion object{
        const val KEY_TITLE: String = "KEY_TITLE"
        var mFragmentCount = 0
        fun newInstance(args: Bundle? = null): BaseFragment {
            val fragment = AddingFoodFragment()
            args?.let { fragment.arguments = it }
            return fragment
        }
    }

    override fun onViewCreated() {

    }

    override fun onDestroy() {
        mView = null
        mFragmentCount--
    }

    override fun addFragment() {
        mFragmentCount++
        val instance = newInstance(Bundle().also {
            it.putString(
                KEY_TITLE,
                "Home $mFragmentCount"
            )
        })
        FragmentObject.addFragment(instance)
    }

    override fun launchCamera(){
        mView?.launchCamera()
    }


    //SERVICE
    override fun postFood(food: FoodResponse){
        mView?.showProgress()
        foodRepository?.postFood(food, object : FoodRepository.RepositoryCallback<FoodResponse>  {

            override fun onSuccess(foodDto: FoodResponse?) {
                if (mView!=null) {
                    mView?.hideProgress()
                    mView?.clearValues()
                }

            }

            override fun onError(error: String) {
                if (mView!=null) {
                    mView?.hideProgress()
                    mView?.showErrorMessage(error)
                }
            }
        })
    }

}