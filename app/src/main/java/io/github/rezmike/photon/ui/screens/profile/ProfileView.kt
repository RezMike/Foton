package io.github.rezmike.photon.ui.screens.profile

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import com.squareup.picasso.Picasso
import io.github.rezmike.photon.R
import io.github.rezmike.photon.data.storage.realm.UserRealm
import io.github.rezmike.photon.ui.screens.AbstractView
import io.github.rezmike.photon.utils.DaggerService
import io.github.rezmike.photon.utils.toArrayList
import kotlinx.android.synthetic.main.screen_profile.view.*
import javax.inject.Inject

class ProfileView(context: Context, attrs: AttributeSet) : AbstractView<ProfilePresenter, ProfileView>(context, attrs) {

    @Inject
    lateinit var picasso: Picasso

    val adapter = ProfileAlbumsAdapter(picasso, { presenter.onClickItem(it) })

    override fun initDagger(context: Context) {
        DaggerService.getDaggerComponent<ProfileScreen.Component>(context).inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        profile_avatar_img.setOnClickListener { presenter.onClickChangeAvatar() }

        list_albums.layoutManager = GridLayoutManager(context, 2)
        list_albums.adapter = adapter
    }

    fun showProfileInfo(user: UserRealm) {
        initUserData(user)
        initList(user)
    }

    fun initUserData(user: UserRealm) {
        profile_login_tv.text = user.login
        profile_name_tv.text = user.name
        album_count.text = user.albumCount.toString()
        photocard_count.text = user.photocardCount.toString()

        picasso.load(user.avatar)
                .resize(80, 80)
                .placeholder(R.drawable.default_avatar)
                .into(profile_avatar_img)
    }

    private fun initList(user: UserRealm) {
        adapter.reloadAdapter(user.albums.toArrayList())
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}
