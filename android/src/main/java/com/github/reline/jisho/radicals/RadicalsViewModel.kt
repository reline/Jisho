package com.github.reline.jisho.radicals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.reline.jisho.models.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RadicalsViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    val radicals = MutableStateFlow<Map<String, Radical>>(emptyMap())

    init {
        viewModelScope.launch {
            radicals.value = defaultRadicals()
        }
    }

    private suspend fun defaultRadicals() = repository.getRadicals().associateByTo(
        destination = LinkedHashMap(),
        keySelector = { it.value },
    )

    fun onRadicalToggled(toggledRadical: Radical) = viewModelScope.launch {
        val selectedRadicals = radicals.value.values.filter { radical ->
            if (radical.value == toggledRadical.value) {
                !radical.isSelected
            } else {
                radical.isSelected
            }
        }

        if (selectedRadicals.isEmpty()) {
            radicals.value = defaultRadicals()
            return@launch
        }

        val related = repository.getRelatedRadicals(selectedRadicals)
        val results = radicals.value.mapValues { radical ->
            val isSelected = selectedRadicals.contains(radical.value)
            val isEnabled = related.any { it.value == radical.value.value }
            radical.value.copy(isEnabled = isEnabled, isSelected = isSelected)
        }
        radicals.value = results
    }
}
