package com.antoniofm.myfoodlist.foodList

import com.antoniofm.myfoodlist.BasePresenter
import com.antoniofm.myfoodlist.BaseView
import com.antoniofm.myfoodlist.api.FoodResponse

interface FoodListContract {

    interface Presenter: BasePresenter {
        fun getFoodList()
        fun onViewCreated()
        fun addFragment()
    }

    interface View: BaseView<Presenter> {
        fun onFailure(message: String)
        fun showFoodList(foodDto: List<FoodResponse?>?)
        fun showError()
    }

}