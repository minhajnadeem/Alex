package com.example.datingapp.main.likes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.datingapp.databinding.FragmentLikesBinding
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.LikedListResponse
import com.example.datingapp.networking.ProfileResponse
import com.example.datingapp.utils.MyPreferences

/**
 * A simple [Fragment] subclass.
 */
class LikesFragment : Fragment() {

    private lateinit var binding: FragmentLikesBinding
    private val likesFragmentModel = LikesFragmentModel()
    private lateinit var preferences: MyPreferences


    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences = MyPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLikesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutNoLikedList.visibility = View.GONE
        likesFragmentModel.getLikedList(preferences.getAuthToken(),
            object : ApiListener<LikedListResponse> {
                override fun onSuccess(body: LikedListResponse?) {
                    binding.progressBar.visibility = View.INVISIBLE
                    body?.let {
                        val list = it.likedList
                        handleResponse(list)
                        return
                    }
                    handleResponse(null)
                }

                override fun onFailure(error: Throwable) {
                    handleResponse(null)
                    binding.progressBar.visibility = View.INVISIBLE

                }

            })
    }

    private fun handleResponse(list: List<ProfileResponse>?) {
        list?.let {
            binding.rvLikedList.adapter = LikedAdapter(it)
            if (it.isEmpty()) {
                binding.layoutNoLikedList.visibility = View.VISIBLE
            } else {
                binding.layoutNoLikedList.visibility = View.GONE

            }
            return
        }
        binding.layoutNoLikedList.visibility = View.VISIBLE
    }

}
