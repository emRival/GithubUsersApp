package com.rival.githubusersapp.ui.detail.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rival.githubusersapp.data.model.GithubItemUser
import com.rival.githubusersapp.databinding.FragmentFollowingsBinding
import com.rival.githubusersapp.ui.detail.UserDetailActivity


class FollowingsFragment : Fragment() {

    private lateinit var binding: FragmentFollowingsBinding
    private val fragmentViewModel : FragmentViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowings.layoutManager = layoutManager

        val username = arguments?.getString(UserDetailActivity.EXTRA_FRAGMENT).toString()

        fragmentViewModel.findFollowings(username)

        fragmentViewModel.followers.observe(viewLifecycleOwner) { userFollowings ->
            setUserFollowings(userFollowings)

        }

        fragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setUserFollowings(userFollowings: List<GithubItemUser>) {

        val adapter = ListFragmentAdapter(userFollowings)
        binding.rvFollowings.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbFollowings.visibility = View.VISIBLE
        } else {
            binding.pbFollowings.visibility = View.INVISIBLE
        }
    }
}