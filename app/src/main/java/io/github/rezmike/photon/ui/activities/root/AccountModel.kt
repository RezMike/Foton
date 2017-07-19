package io.github.rezmike.photon.ui.activities.root

import io.github.rezmike.photon.data.network.req.AlbumReq
import io.github.rezmike.photon.data.network.req.LoginReq
import io.github.rezmike.photon.data.network.req.RegisterReq
import io.github.rezmike.photon.jobs.CreateAlbumJob
import io.github.rezmike.photon.jobs.UserAvatarJob
import io.github.rezmike.photon.ui.screens.AbstractModel
import rx.Completable

class AccountModel : AbstractModel() {

    fun isUserAuth() = dataManager.isUserAuth()

    fun getUserLogin() = dataManager.getUserLogin()

    fun getUserName() = dataManager.getUserName()

    fun login(email: String, password: String): Completable {
        return dataManager.loginUserCompl(LoginReq(email, password))
    }

    fun register(name: String, login: String, email: String, password: String): Completable {
        return dataManager.registerUserCompl(RegisterReq(name, login, email, password))
    }

    fun createAlbum(title: String, description: String) {
        jobManager.addJobInBackground(CreateAlbumJob(AlbumReq(dataManager.getUserId()!!, title, description)))
    }

    fun updateProfileInfo(name: String, login: String): Completable {
        TODO("not implemented")
    }

    fun uploadAvatarToServer(avatarUrl: String) {
        jobManager.addJobInBackground(UserAvatarJob(avatarUrl))
    }
}