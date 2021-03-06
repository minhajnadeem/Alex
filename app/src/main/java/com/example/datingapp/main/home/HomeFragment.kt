package com.example.datingapp.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.datingapp.databinding.FragmentHomeBinding
import com.example.datingapp.events.HomeEvent
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.HomeResponse
import com.example.datingapp.networking.ProfileResponse
import com.example.datingapp.networking.SuccessResponse
import com.example.datingapp.utils.AlexDialogsHelper
import com.example.datingapp.utils.MyPreferences
import com.example.datingapp.utils.notifications.NotificationsModel
import com.yuyakaido.android.cardstackview.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), CardStackListener {

    private lateinit var binding: FragmentHomeBinding
    private val homeFragmentModel = HomeFragmentModel()
    private lateinit var myPreferences: MyPreferences
    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var swipeRightAnim: SwipeAnimationSetting
    private lateinit var swipeLeftAnim: SwipeAnimationSetting
    private lateinit var swipeRewindAnim: RewindAnimationSetting
    private val alexDialogsHelper = AlexDialogsHelper()
    private lateinit var profileList:List<ProfileResponse>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myPreferences = MyPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardStackLayoutManager = CardStackLayoutManager(requireContext(), this)
        binding.layoutCardStack.layoutManager = cardStackLayoutManager
        swipeRightAnim = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        swipeLeftAnim = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()

        swipeRewindAnim = RewindAnimationSetting.Builder()
            .setDirection(Direction.Bottom)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(DecelerateInterpolator())
            .build()

        binding.btnTryAgain.setOnClickListener {
//            binding.layoutCardStack.adapter = HomeAdapter(body.profile)
            Toast.makeText(context, "click", Toast.LENGTH_LONG).show()

        }

        binding.btnHomeCross.setOnClickListener {
            cardStackLayoutManager.setSwipeAnimationSetting(swipeLeftAnim)
            binding.layoutCardStack.swipe()
            profileDisLiked()
        }

        binding.btnHomeRefresh.setOnClickListener {
            cardStackLayoutManager.setRewindAnimationSetting(swipeRewindAnim)
            binding.layoutCardStack.rewind()
        }

        binding.btnHomeFav.setOnClickListener {
            cardStackLayoutManager.setSwipeAnimationSetting(swipeRightAnim)
            binding.layoutCardStack.swipe()
            profileLiked()
        }

        executeHomeApi()
        registerFcmToken()
    }

    private fun registerFcmToken() {
        val authToken = myPreferences.getAuthToken()
        val fcmToken = myPreferences.fcmToken
        fcmToken?.let {
            NotificationsModel().registerFcmToken(
                it,
                authToken,
                object : ApiListener<SuccessResponse> {
                    override fun onSuccess(body: SuccessResponse?) {

                    }

                    override fun onFailure(error: Throwable) {
                    }

                })
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Left -> profileDisLiked()
            Direction.Right -> profileLiked()
        }
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }

    private fun profileLiked() {
        if(cardStackLayoutManager.topPosition!=profileList.size) {
            val profile = homeAdapter.getProfileByPosition(cardStackLayoutManager.topPosition)
            homeFragmentModel.exeLikeApi(
                profile.id,
                myPreferences.getAuthToken(),
                object : ApiListener<SuccessResponse> {
                    override fun onSuccess(body: SuccessResponse?) {

                    }

                    override fun onFailure(error: Throwable) {
                        error.printStackTrace()
                    }

                })
        }else{
           // Toast.makeText(activity, "Last Index Reached", Toast.LENGTH_SHORT).show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onHomeEvent(event: HomeEvent) {
        executeHomeApi()
    }

    private fun executeHomeApi() {
        homeFragmentModel.exeHomeDataApi(myPreferences.getAuthToken(),
            myPreferences.currentLocation?.latitude,
            myPreferences.currentLocation?.longitude,
            myPreferences.minAge,
            myPreferences.maxAge,
            myPreferences.radius,
            object : ApiListener<HomeResponse> {
                override fun onSuccess(body: HomeResponse?) {
                    if (body != null) {
                        if (body.success) {
                            profileList = body.profiles
                            homeAdapter = HomeAdapter(body.profiles.reversed())
                            binding.layoutCardStack.adapter = homeAdapter
                        } else {
                            AlexDialogsHelper().showToastMessage(
                                requireContext(),
                                "Something Went Wrong!"
                            )
                            //                            myPreferences.clearPrefs()
                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    error.printStackTrace()
                }

            })
    }

    private fun profileDisLiked() {
        if(cardStackLayoutManager.topPosition!=profileList.size) {
            val profile = homeAdapter.getProfileByPosition(cardStackLayoutManager.topPosition)
            homeFragmentModel.exeDisLikeApi(
                profile.id,
                myPreferences.getAuthToken(),
                object : ApiListener<SuccessResponse> {
                    override fun onSuccess(body: SuccessResponse?) {
                    }

                    override fun onFailure(error: Throwable) {
                    }

                })
        }else{
           // Toast.makeText(activity, "Last Index Reached", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}
