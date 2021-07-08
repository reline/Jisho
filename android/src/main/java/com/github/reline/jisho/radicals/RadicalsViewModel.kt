package com.github.reline.jisho.radicals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.reline.jisho.models.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RadicalsViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    val radicals = MutableLiveData<LinkedHashMap<String, Radical>>()

    init {
        viewModelScope.launch {
            radicals.postValue(repository.getRadicals().associateByTo(
                destination = LinkedHashMap(),
                keySelector = { it.value },
                valueTransform = { Radical(it.value, it.strokes.toInt()) },
            ))
        }
    }

    fun onRadicalSelected(radical: Radical) {
        val rads = radicals.value ?: return
        rads[radical.value] = radical.copy(isSelected = !radical.isSelected)
        radicals.postValue(rads)
    }
}
