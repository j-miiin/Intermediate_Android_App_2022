package com.example.carrot_market_part3_chapter06.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.carrot_market_part3_chapter06.R
import com.example.carrot_market_part3_chapter06.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private var binding: FragmentMyPageBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMypageBinding = FragmentMyPageBinding.bind(view)
        binding = fragmentMypageBinding

        fragmentMypageBinding.signInOutButton.setOnClickListener {
            binding?.let { binding ->
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                if (auth.currentUser == null) {
                    // 로그인
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                successSignIn()
                            } else {
                                Toast.makeText(context, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // 로그아웃
                    auth.signOut()
                    binding.emailEditText.text.clear()
                    binding.emailEditText.isEnabled = true
                    binding.passwordEditText.text.clear()
                    binding.passwordEditText.isEnabled = true

                    binding.signInOutButton.text = "로그인"
                    binding.signInOutButton.isEnabled = false
                    binding.signUpButton.isEnabled = false
                }
            }
        }

        fragmentMypageBinding.signUpButton.setOnClickListener {
            binding?.let { binding ->
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "회원가입에 실패했습니다. 이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        fragmentMypageBinding.emailEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }

        fragmentMypageBinding.passwordEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutButton.isEnabled = enable
                binding.signUpButton.isEnabled = enable
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            binding?.let { binding ->
                binding.emailEditText.text.clear()
                binding.emailEditText.isEnabled = true
                binding.passwordEditText.text.clear()
                binding.passwordEditText.isEnabled = true

                binding.signInOutButton.text = "로그인"
                binding.signInOutButton.isEnabled = false
                binding.signUpButton.isEnabled = false
            }
        } else {
            binding?.let { binding ->
                binding.emailEditText.setText(auth.currentUser!!.email)
                binding.emailEditText.isEnabled = false
                binding.passwordEditText.setText("*************")
                binding.passwordEditText.isEnabled = false

                binding.signInOutButton.text = "로그아웃"
                binding.signInOutButton.isEnabled = true
                binding.signUpButton.isEnabled = false
            }
        }
    }

    private fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        binding?.emailEditText?.isEnabled = false
        binding?.passwordEditText?.isEnabled = false
        binding?.signUpButton?.isEnabled = false
        binding?.signInOutButton?.text = "로그아웃"
    }


}