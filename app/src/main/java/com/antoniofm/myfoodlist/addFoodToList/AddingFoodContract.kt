package com.antoniofm.myfoodlist.addFoodToList

import com.antoniofm.myfoodlist.BasePresenter
import com.antoniofm.myfoodlist.BaseView
import com.antoniofm.myfoodlist.api.FoodResponse

interface AddingFoodContract {

    interface Presenter: BasePresenter {
        fun onViewCreated()
        fun addFragment()
        fun launchCamera()
        fun postFood(food: FoodResponse)
    }

    interface View: BaseView<Presenter> {
        fun launchCamera()
        fun clearValues()
        fun showErrorMessage(string: String)
        fun showSuccessfulMessage(message: String)
    }
    
}