package com.example.consumerapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consumerapp.model.FollowersItem
import com.example.consumerapp.model.UserDetails
import com.example.consumerapp.model.UserItem
import com.example.consumerapp.model.UserQuery
import com.example.consumerapp.repository.Repository
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
    var userDetailName: String = ""

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

    fun getUserNameDetail(username: String): LiveData<Response<UserDetails>> {
        if (username != null) {
            userDetailName = username
        }
        return userDetails
    }

    fun getUserDetail() {
        loading.value = true
        viewModelScope.launch {
            val response = repository.getUserDetail(userDetailName)
            _userDetails.value = response
        }
    }

    fun getUserFollower(username: String) {
        loading.value = true
        viewModelScope.launch {
            //val response = repository.getUserFollower(username)
            _userFollower.value = repository.getUserFollower(username)
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