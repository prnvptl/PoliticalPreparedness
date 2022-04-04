package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.electionViewModel = viewModel
        binding.lifecycleOwner = this
        val adapter = ElectionListAdapter(ElectionListAdapter.OnClickListener { election ->
            viewModel.navigateToVoterInfoWith(election)
        })
        val savedElectionsAdapter =
            ElectionListAdapter(ElectionListAdapter.OnClickListener { election ->
                viewModel.navigateToVoterInfoWith(election)
            })
        binding.elections.adapter = adapter
        binding.savedElections.adapter = savedElectionsAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.savedElections.observe(viewLifecycleOwner) {
            savedElectionsAdapter.submitList(it)
        }

        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner) { election ->
            if (election != null) {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election)
                )
                viewModel.navigateToVoterInfoDone()
            }
        }
        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters


        return binding.root
    }

    //TODO: Refresh adapters when fragment loads

}