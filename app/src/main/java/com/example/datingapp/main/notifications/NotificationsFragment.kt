package com.example.datingapp.main.notifications

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.datingapp.R
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.NotificationsResponse
import com.example.datingapp.utils.MyPreferences

/**
 * A simple [Fragment] subclass.
 */
class NotificationsFragment : Fragment() {

    private lateinit var notificationFragmentModel: NotificationFragmentModel
    private lateinit var myPreferences: MyPreferences

    private lateinit var rvNotifications: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        notificationFragmentModel = NotificationFragmentModel()
        myPreferences = MyPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        notificationFragmentModel.exeGetNotificationApi(myPreferences.getAuthToken(),
            object : ApiListener<NotificationsResponse> {
                override fun onSuccess(body: NotificationsResponse?) {
                    body?.also {
                        if (it.success) {
                            val notificationsAdapter = NotificationsAdapter()
                            rvNotifications.adapter = notificationsAdapter
                            notificationsAdapter.setData(it.notifications)
                        }
                    }

                }

                override fun onFailure(error: Throwable) {
                    error.printStackTrace()
                }

            })
    }

    private fun initViews(view: View) {
        rvNotifications = view.findViewById(R.id.rvNotifications)

    }
}
