package com.example.datingapp.auth.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.datingapp.Constants
import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentLoginBinding
import com.example.datingapp.networking.ApiListener
import com.example.datingapp.networking.AuthResponse
import com.example.datingapp.utils.AlexDialogsHelper
import com.example.datingapp.utils.LoginDialogListener
import com.example.datingapp.utils.MyPreferences
import com.example.datingapp.utils.RegisterDialogListener
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val loginFragmentModel = LoginFragmentModel()
    private lateinit var myPreferences: MyPreferences
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginFragmentViewModel: LoginFragmentViewModel
    private val dialogs = AlexDialogsHelper()
    private var fcmToken: String? = null

    //google sign-in
    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    //facebook sign-in
    private val callbackManager = CallbackManager.Factory.create()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myPreferences = MyPreferences(context)
        fcmToken = myPreferences.fcmToken
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

        binding.progressBar.visibility = View.INVISIBLE

        binding.btnLoginFb.setOnClickListener {
            loginWithFb()
        }
        binding.btnLoginGoogle.setOnClickListener {
            loginWithGmail()
        }
        binding.btnLoginEmail.setOnClickListener {
            loginWithEmail()
        }

    }

    private fun loginWithFb() {
        LoginManager.getInstance()
            .logInWithReadPermissions(
                this,
                Arrays.asList("email", "public_profile", "user_friends")
            )
    }

    private fun initFbSignIn() {
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Toast.makeText(requireContext(), "Facebook Login Success", Toast.LENGTH_LONG)
                        .show()
                    handleFbLogin(result)
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

    private fun handleFbLogin(result: LoginResult?) {
        if (result != null) {
            binding.progressBar.visibility = View.VISIBLE
            val listener = object : ApiListener<AuthResponse> {
                override fun onSuccess(body: AuthResponse?) {
                    binding.progressBar.visibility = View.INVISIBLE

                    if (body != null) {
                        if (body.success) {
                            doLogin(body)
                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    dialogs.showToastMessage(requireContext(), error.message)
                }

            }
            loginFragmentModel.exeFbLoginApi(result.accessToken.token, listener)
        }
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

    private fun loginWithEmail() {
        val registerListener = object : ApiListener<AuthResponse> {
            override fun onSuccess(body: AuthResponse?) {
                dialogs.hideLoginProgressBar()
                dialogs.hideRegisterProgressBar()
                if (body != null) {
                    if (body.success) {
                        dialogs.hideLoginEmailDialog()
                        dialogs.hideRegisterEmailDialog()
                        doLogin(body)
                    } else {
                        dialogs.showToastMessage(requireContext(), body.message)
                    }
                }

            }

            override fun onFailure(error: Throwable) {
                dialogs.hideLoginProgressBar()
                dialogs.hideRegisterProgressBar()
                dialogs.showToastMessage(requireContext(), error.message)
            }
        }

        dialogs.showLoginEmailDialog(requireActivity().supportFragmentManager,
            object : LoginDialogListener {
                override fun onClickLogin(email: String, password: String) {
                    loginFragmentModel.exeLoginApi(email, password, registerListener)
                }

                override fun onClickRegister() {
                    dialogs.showRegisterDialog(
                        requireActivity().supportFragmentManager,
                        object : RegisterDialogListener {
                            override fun onClickRegister(
                                displayName: String,
                                email: String,
                                password: String
                            ) {
                                loginFragmentModel.exeRegisterApi(
                                    displayName,
                                    email,
                                    password,
                                    1589316956
                                    , fcmToken
                                    , registerListener
                                )
                            }

                            override fun onClickLogin() {
                                loginWithEmail()
                            }

                        })
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_GOOGLE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
            handleGoogleLogin(account)
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private fun handleGoogleLogin(account: GoogleSignInAccount) {
        account.idToken?.let {
            loginFragmentModel.exeGoogleLoginApi(it, object : ApiListener<AuthResponse> {
                override fun onSuccess(body: AuthResponse?) {
                    body?.let {
                        if (it.success) {
                            doLogin(it)
                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    error.printStackTrace()
                }

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun doLogin(body: AuthResponse) {
        myPreferences.profile = body.profile
        if (!body.profile.isComplete) {
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
