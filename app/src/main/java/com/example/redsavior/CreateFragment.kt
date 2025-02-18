package com.example.redsavior

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.redsavior.databinding.FragmentCreateBinding
import com.google.firebase.auth.FirebaseAuth

class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eMailId.editText?.addTextChangedListener(emailTextWatcher)
        binding.passwordId.editText?.addTextChangedListener(passwordTextWatcher)

        binding.loginId.setOnClickListener {
            findNavController().navigate(R.id.action_createFragment_to_loginFragment)
        }

        binding.registerBtn.setOnClickListener {
            if (checkValidation()) {
                val email = binding.eMailId.editText?.text.toString()
                val password = binding.passwordId.editText?.text.toString()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Registration Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            val bundle = Bundle().apply {
                                putString(
                                    "name",
                                    binding.fullNameId.editText?.text.toString().trim()
                                )
                                putString("email", email)
                            }
                            findNavController().navigate(
                                R.id.action_createFragment_to_detailsFragment,
                                bundle
                            )
                        } else {
                            Toast.makeText(requireContext(), "Error Occurred!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }
    }

    private fun checkValidation(): Boolean {
        val name = binding.fullNameId.editText?.text.toString().trim()
        val email = binding.eMailId.editText?.text.toString().trim()
        val password = binding.passwordId.editText?.text.toString().trim()
        val confirmPassword = binding.confirmPasswordId.editText?.text.toString().trim()

        if (name.isEmpty()) {
            binding.fullNameId.error = "Please enter your name"
            return false
        } else {
            binding.fullNameId.isErrorEnabled = false
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.eMailId.error = "Invalid email format"
            return false
        } else {
            binding.eMailId.isErrorEnabled = false
        }

        if (password.length < 6) {
            binding.passwordId.error = "Password too weak (min. 6 characters)"
            return false
        } else {
            binding.passwordId.isErrorEnabled = false
        }

        if (password != confirmPassword) {
            binding.confirmPasswordId.error = "Passwords do not matching.."
            return false
        } else {
            binding.confirmPasswordId.isErrorEnabled = false
        }

        return true
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val email = p0.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.eMailId.error = "Invalid email format"
            } else {
                binding.eMailId.isErrorEnabled = false
            }
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val password = p0.toString().trim()
            binding.passwordId.helperText = getPasswordStrength(password)
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    private fun getPasswordStrength(password: String): String {
        var upper = false
        var lower = false
        var number = false
        var special = false
        for (char in password) {
            when {
                char.isUpperCase() -> upper = true
                char.isLowerCase() -> lower = true
                char.isDigit() -> number = true
                !char.isLetterOrDigit() -> special = true
            }
        }
        return when {
            password.length < 6 -> "Password too short (Min 6 characters)"
            upper && lower && number && special -> "Strong"
            else -> "Medium (Use numbers, uppercase, and special characters)"
        }
    }
}
