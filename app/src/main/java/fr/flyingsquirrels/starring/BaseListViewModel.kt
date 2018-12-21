package fr.flyingsquirrels.starring

import androidx.lifecycle.ViewModel

abstract class BaseListViewModel<T> : ViewModel() {

    var pageNumber: Int = 0
    val list = mutableListOf<T>()

}
