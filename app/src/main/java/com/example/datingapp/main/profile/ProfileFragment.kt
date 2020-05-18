package com.example.datingapp.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.billingclient.api.SkuDetails

import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentProfileBinding
import com.example.datingapp.inappbilling.MyBillingClient
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var myBillingClient: MyBillingClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myBillingClient = MyBillingClient(requireActivity())
        binding.btnTindoGold.setOnClickListener {
            myBillingClient.launchPurchaseFlow()
        }
    }

}
