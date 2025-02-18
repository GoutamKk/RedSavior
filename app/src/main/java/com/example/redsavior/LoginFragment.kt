package com.example.redsavior

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.redsavior.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.eMailId.editText?.addTextChangedListener(emailTextWatcher)
        binding!!.passwordId.editText?.addTextChangedListener(passwordTextWatcher)

        binding!!.registerId.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createFragment)
        }

        binding!!.loginBtn.setOnClickListener {
            val email = binding!!.eMailId.editText?.text.toString()
            val password = binding!!.passwordId.editText?.text.toString()

            val bundle = Bundle()
            bundle.putString("name","")
            bundle.putString("email", binding!!.eMailId.editText?.text.toString())
            bundle.putString("password", binding!!.passwordId.editText?.text.toString())
            if (checkValidation()) {
                auth.signInWithEmailAndPassword(
                    email,
                    password
                )
                    .addOnCompleteListener{
                        task ->
                        if (task.isSuccessful) {
                            findNavController().navigate(R.id.action_loginFragment_to_detailsFragment,bundle)
                        }
                        else{
                            Toast.makeText(requireContext(), "Error Occurred!", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val email = s.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding!!.eMailId.error = "Invalid email format"
            } else {
                binding!!.eMailId.isErrorEnabled = false
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val password = s.toString()
            binding!!.passwordId.helperText = getPasswordStrength(password)
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun getPasswordStrength(password: String): String {
        var hasUppercase = false
        var hasLowercase = false
        var hasDigit = false
        var hasSpecialChar = false

        for (char in password) {
            when {
                char.isUpperCase() -> hasUppercase = true
                char.isLowerCase() -> hasLowercase = true
                char.isDigit() -> hasDigit = true
                !char.isLetterOrDigit() -> hasSpecialChar = true
            }
        }

        return when {
            password.length < 6 -> "Weak (Minimum 6 characters required)"
            hasUppercase && hasLowercase && hasDigit && hasSpecialChar -> "Strong"
            else -> "Medium (Use numbers, uppercase, and special characters)"
        }
    }

    private fun checkValidation(): Boolean {
        val email = binding!!.eMailId.editText?.text.toString()
        val password = binding!!.passwordId.editText?.text.toString()

        if (email.isEmpty()) {
            binding!!.eMailId.error = "Enter your email"
            return false
        }
        if (password.length < 6) {
            binding!!.passwordId.error = "Password too short(Min 6 characters)"
            return false
        }
        binding!!.eMailId.isErrorEnabled = false
        binding!!.passwordId.isErrorEnabled = false
        return true
    }

}
