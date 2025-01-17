package com.depromeet.housekeeper.ui.signIn

import androidx.lifecycle.viewModelScope
import com.depromeet.housekeeper.base.BaseViewModel
import com.depromeet.housekeeper.data.repository.UserRepository
import com.depromeet.housekeeper.data.utils.TokenManager
import com.depromeet.housekeeper.model.request.SocialType
import com.depromeet.housekeeper.model.request.Token
import com.depromeet.housekeeper.model.ui.NewMember
import com.depromeet.housekeeper.util.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
) : BaseViewModel() {

    private val _newMember: MutableStateFlow<NewMember?> = MutableStateFlow(null)
    val newMember: StateFlow<NewMember?>
        get() = _newMember

    private val _code: MutableStateFlow<String> = MutableStateFlow("")
    val code: StateFlow<String>
        get() = _code

    fun setAuthCode(authCode: String){
        PrefsManager.setAuthCode(authCode)
        viewModelScope.launch(Dispatchers.IO){
            tokenManager.saveRefreshToken("LOGIN")
        }
    }

    /**
     * Network Communication
     */

    fun requestLogin() {
        viewModelScope.launch {
            userRepository.getGoogleLogin(
                SocialType("GOOGLE")
            ).collectLatest {
                val result = receiveApiResult(it) ?: return@collectLatest
                _newMember.value = NewMember(result.hasTeam, result.isNewMember)

                PrefsManager.setAuthCode(null) // 로그인 성공했으니 authCode는 이제 유효하지 않으므로 null로 변경

                result.memberName?.let { name -> PrefsManager.setUserName(name) }
                PrefsManager.setMemberId(result.memberId)
                PrefsManager.setHasTeam(result.hasTeam)

                withContext(Dispatchers.IO){
                    tokenManager.saveAccessToken(result.accessToken)
                    tokenManager.saveRefreshToken(result.refreshToken)
                }

                // 로그인 후 set fcm token
                saveToken()

                Timber.d("Auth \naccessToken:${result.accessToken}\nrefreshToken:${result.refreshToken}")
                Timber.d("memeberId : ${result.memberId}, ${PrefsManager.memberId}")
                Timber.d("isNewMember : ${result.isNewMember}, team: ${result.hasTeam}, MemberName: ${result.memberName}")
            }
        }
    }

    private fun saveToken() {
        viewModelScope.launch {
            if (PrefsManager.deviceToken.isNullOrEmpty()) {
                Timber.e("fcm token is not set")
                return@launch
            }
            userRepository.saveToken(Token(token = PrefsManager.deviceToken!!))
                .collectLatest {
                    val result = receiveApiResult(it)
                    if (result != null) {
                        Timber.d("set fcm token response $this")
                    }
                }
        }
    }

}
