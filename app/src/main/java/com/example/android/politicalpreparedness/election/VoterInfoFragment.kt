package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class VoterInfoFragment : Fragment() {

    private val navArgs: VoterInfoFragmentArgs by navArgs()
    private val voterInfoViewModel: VoterInfoViewModel by viewModel {
        parametersOf(navArgs.argElectionId, navArgs.argDivision)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.viewModel = voterInfoViewModel
        binding.lifecycleOwner = this

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */


        voterInfoViewModel.urlToOpen.observe(viewLifecycleOwner) {
            if (it != null) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(browserIntent)
                voterInfoViewModel.openUrlDone()
            }
        }
        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

        return binding.root
    }

}