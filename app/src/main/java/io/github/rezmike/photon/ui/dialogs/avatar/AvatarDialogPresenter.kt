package io.github.rezmike.photon.ui.dialogs.avatar

import android.Manifest.permission.*
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import io.github.rezmike.photon.R
import io.github.rezmike.photon.data.storage.dto.ActivityResultDto
import io.github.rezmike.photon.data.storage.dto.DialogResult
import io.github.rezmike.photon.ui.activities.root.AccountModel
import io.github.rezmike.photon.ui.activities.root.RootActivity
import io.github.rezmike.photon.ui.activities.root.RootPresenter
import io.github.rezmike.photon.ui.dialogs.AbstractDialogPresenter
import io.github.rezmike.photon.utils.ActionHelper
import io.github.rezmike.photon.utils.ConstantManager.REQUEST_PERMISSION_CAMERA
import io.github.rezmike.photon.utils.ConstantManager.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
import io.github.rezmike.photon.utils.ConstantManager.REQUEST_PROFILE_PHOTO_CAMERA
import io.github.rezmike.photon.utils.ConstantManager.REQUEST_PROFILE_PHOTO_GALLERY
import io.github.rezmike.photon.utils.createFileForPhoto
import io.github.rezmike.photon.utils.getFileFromUri
import mortar.Popup
import rx.Subscription
import java.io.File

class AvatarDialogPresenter(val rootPresenter: RootPresenter, val model: AccountModel) : AbstractDialogPresenter<Uri, AvatarDialog>() {

    private var photoUri: Uri = Uri.parse(model.getUserAvatar()!!)
    private var photoFile: File? = null
    private var requestSub: Subscription? = null

    override fun onLoad(savedInstanceState: Bundle?) {
        super.onLoad(savedInstanceState)
        requestSub = subscribeOnActivityResult()
    }

    override fun dropView(view: Popup<Uri, DialogResult>?) {
        requestSub?.unsubscribe()
        super.dropView(view)
    }

    override fun show() = show(photoUri)

    fun onClickCamera() {
        val permission = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE)
        if (rootPresenter.checkPermissionAndRequestIfNotGranted(permission, REQUEST_PERMISSION_CAMERA)) {

            photoFile = createFileForPhoto()
            if (photoFile == null) {
                getDialog()?.showMessage(R.string.avatar_dialog_file_not_created)
                return
            }
            rootPresenter.getRootView()?.startActivityForResult(ActionHelper.getCameraIntent(photoFile!!), REQUEST_PROFILE_PHOTO_CAMERA)
        }
    }

    fun onClickGallery() {
        val permission = arrayOf(READ_EXTERNAL_STORAGE)
        if (rootPresenter.checkPermissionAndRequestIfNotGranted(permission, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)) {
            rootPresenter.getRootView()?.startActivityForResult(ActionHelper.getGalleryIntent(), REQUEST_PROFILE_PHOTO_GALLERY)
        }
    }

    override fun onClickOk() {
        if (photoFile != null) model.uploadAvatarToServer(photoFile!!)
        getDialog()?.dismiss()
        onResult(DialogResult(photoFile != null))
    }

    private fun subscribeOnActivityResult(): Subscription {
        return rootPresenter.getActivityResultSubject()
                .subscribe({ if (it != null) handleActivityResult(it) }, { getRootView()?.showError(it) })
    }

    private fun handleActivityResult(activityResult: ActivityResultDto) {
        if (activityResult.resultCode == Activity.RESULT_OK) {
            when (activityResult.requestCode) {
                REQUEST_PROFILE_PHOTO_CAMERA -> {
                    if (photoFile != null) photoUri = Uri.fromFile(photoFile)
                }
                REQUEST_PROFILE_PHOTO_GALLERY -> {
                    if (activityResult.intent != null) {
                        photoUri = activityResult.intent.data
                        photoFile = getFileFromUri(photoUri, view!!.context)
                    }
                }
                else -> return
            }
            getDialog()?.showAvatar(photoUri.toString())
        } else {
            when (activityResult.requestCode) {
                REQUEST_PROFILE_PHOTO_CAMERA -> if (photoFile != null) getRootView()?.deleteFile(photoFile!!.name)
            }
        }
    }

    private fun getRootView(): RootActivity? {
        return rootPresenter.getRootView()
    }
}