package com.github.reline.jisho.radicals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.reline.jisho.databinding.DialogFragmentRadicalsBinding
import com.github.reline.jisho.util.enableFullscreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RadicalsDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RadicalsViewModel

    lateinit var binding: DialogFragmentRadicalsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(RadicalsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentRadicalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RadicalsAdapter()
        binding.radicalsList.adapter = adapter
        viewModel.radicals.observe(this) {
            adapter.submitList(it.values.toList())
        }
        lifecycleScope.launch {
            adapter.clicks.asFlow().collect {
                viewModel.onRadicalSelected(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        enableFullscreen()
    }
}