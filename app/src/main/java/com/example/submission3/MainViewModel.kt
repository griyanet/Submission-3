package com.example.submission3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission3.model.FollowersItem
import com.example.submission3.model.UserDetails
import com.example.submission3.model.UserItem
import com.example.submission3.model.UserQuery
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _usersResponse: MutableLiveData<Response<ArrayList<UserItem>>> = MutableLiveData()
    val usersResponse: LiveData<Response<ArrayList<UserItem>>>
        get() = _usersResponse

    private val _userQuery: MutableLiveData<Response<UserQuery>> = MutableLiveData()
    val userQuery: LiveData<Response<UserQuery>>
        get() = _userQuery

    private val _userDetails: MutableLiveData<Response<UserDetails>> = MutableLiveData()
    val userDetails: LiveData<Response<UserDetails>>
        get() = _userDetails

    private val _userFollower: MutableLiveData<Response<ArrayList<FollowersItem>>> =
        MutableLiveData()
    val userFollower: LiveData<Response<ArrayList<FollowersItem>>>
        get() = _userFollower

    private val _userFollowing: MutableLiveData<Response<ArrayList<FollowersItem>>> =
        MutableLiveData()
    val userFollowing: LiveData<Response<ArrayList<FollowersItem>>>
        get() = _userFollowing

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    var userName: String = "a"

    fun getUser() {
        loading.value = true
        viewModelScope.launch {
            val response = repository.getUser()
            _usersResponse.value = response
            loading.value = false
        }
    }

    fun getQueryUser(username: String): LiveData<Response<UserQuery>> {
        if (username != null) {
            userName = username
        }
        return userQuery
    }

    fun getUserQuery() {
        loading.value = true
        viewModelScope.launch {
            val response = repository.getUserQuery(userName)
            _userQuery.value = response
            loading.value = false
        }
    }

    fun getUserDetail(username: String) {
        loading.value = true
        viewModelScope.launch {
            val response = repository.getUserDetail(username)
            _userDetails.value = response
        }
    }

    fun getUserFollower(username: String) {
        loading.value = true
        viewModelScope.launch {
            val response = repository.getUserFollower(username)
            _userFollower.value = response
            loading.value = false
        }
    }

    fun getUserFollowing(username: String) {
        loading.value = true
        viewModelScope.launch {
            val response = repository.getUserFollowing(username)
            _userFollowing.value = response
            loading.value = false
        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}