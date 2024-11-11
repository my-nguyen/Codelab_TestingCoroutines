package com.nguyen.codelab_testingcoroutines.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nguyen.codelab_testingcoroutines.R
import com.nguyen.codelab_testingcoroutines.TodoApplication
import com.nguyen.codelab_testingcoroutines.databinding.StatisticsFragBinding
import com.nguyen.codelab_testingcoroutines.util.setupRefreshLayout

/**
 * Main UI for the statistics screen.
 */
class StatisticsFragment : Fragment() {

    private lateinit var viewDataBinding: StatisticsFragBinding

    private val viewModel by viewModels<StatisticsViewModel> {
        StatisticsViewModelFactory((requireContext().applicationContext as TodoApplication).taskRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.statistics_frag, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewDataBinding.viewmodel = viewModel
        viewDataBinding.lifecycleOwner = viewLifecycleOwner
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
    }
}
