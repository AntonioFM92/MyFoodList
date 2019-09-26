package com.antoniofm.myfoodlist

interface BaseView<T> {
    fun attachPresenter(presenter: T)
    fun detachPresenter()
    fun showProgress()
    fun hideProgress()
}