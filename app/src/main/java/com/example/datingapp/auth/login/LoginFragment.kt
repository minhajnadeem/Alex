package com.example.datingapp.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.datingapp.Constants
import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentLoginBinding
import com.example.datingapp.utils.MyPreferences
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var myPreferences: MyPreferences
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginFragmentViewModel: LoginFragmentViewModel

    //google sign-in
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    //facebook sign-in
    private val callbackManager = CallbackManager.Factory.create()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        myPreferences = MyPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        loginFragmentViewModel = ViewModelProviders.of(this).get(LoginFragmentViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGoogleSignIn()
        initFbSignIn()
        binding.btnLoginFb.setOnClickListener {
            loginWithFb()
        }
        binding.btnLoginGoogle.setOnClickListener {
            loginWithGmail()
        }
        binding.btnLoginEmail.setOnClickListener {
            doLogin()
        }

    }

    private fun loginWithFb() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_friends"))
    }

    private fun initFbSignIn() {
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Toast.makeText(requireContext(), "Facebook Login Success", Toast.LENGTH_LONG)
                        .show()
                    doLogin()
                }

                override fun onCancel() {
                    Log.d("TAG", "cancle")

                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(requireContext(), "" + error.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.d("TAG", "" + error.toString())

                }

            })
    }

    private fun initGoogleSignIn() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(
                getString(
                    R.string.default_web_client_id
                )
            ).requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun loginWithGmail() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constants.RC_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_GOOGLE_SIGN_IN) {
            Toast.makeText(requireContext(), "Google Login Success", Toast.LENGTH_LONG).show()
            doLogin()

        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun doLogin() {
        if (myPreferences.isFirstStart()) {
            val direction =
                LoginFragmentDirections.actionLoginFragmentToVerificationFragment()
            findNavController().navigate(direction)

        } else {
            val direction =
                LoginFragmentDirections.actionLoginFragmentToHomeActivity()
            findNavController().navigate(direction)
            activity!!.finish()
        }
    }

}
