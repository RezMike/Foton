package io.github.rezmike.photon.ui.dialogs.album

import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import io.github.rezmike.photon.R
import io.github.rezmike.photon.data.storage.dto.AlbumInfoDto
import io.github.rezmike.photon.data.storage.dto.DialogResult
import io.github.rezmike.photon.ui.dialogs.AbstractDialog
import io.github.rezmike.photon.ui.others.AnimHelper
import io.github.rezmike.photon.ui.others.CustomTextWatcher
import io.github.rezmike.photon.ui.others.changeError
import mortar.PopupPresenter

class AlbumDialog(context: Context) : AbstractDialog<AlbumInfoDto>(context) {

    private var name: EditText? = null
    private var description: EditText? = null
    private var okBtn: Button? = null
    private var cancelBtn: Button? = null

    override fun onFinishInflate(view: View, presenter: PopupPresenter<AlbumInfoDto, DialogResult>, info: AlbumInfoDto) {
        if (presenter !is AlbumDialogPresenter) {
            throw ClassCastException("presenter must implements AlbumDialogPresenter")
        }

        name = view.findViewById(R.id.name_album_et) as EditText
        description = view.findViewById(R.id.description_album_et) as EditText
        okBtn = view.findViewById(R.id.ok_btn) as Button
        cancelBtn = view.findViewById(R.id.cancel_btn) as Button

        name?.addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                presenter.checkName(s.toString())
            }
        })
        description?.addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                presenter.checkDescription(s.toString())
            }
        })
        name?.setText(info.name)
        description?.setText(info.description)

        okBtn?.setOnClickListener { presenter.onClickOk() }
        cancelBtn?.setOnClickListener { presenter.onClickCancel() }
    }

    override fun setLayoutRes(): Int = R.layout.dialog_album

    override fun clearFields() {
        name = null
        description = null
        okBtn = null
        cancelBtn = null
    }

    //region ======================== Error ========================

    fun showNameError() {
        name?.changeError(true, context.getString(R.string.album_title_error))
    }

    fun hideNameError() {
        name?.changeError(false)
    }

    fun showDescriptionError() {
        description?.changeError(true, context.getString(R.string.album_title_error))
    }

    fun hideDescriptionError() {
        description?.changeError(false)
    }

    //endregion

    //region ======================== Anim ========================

    fun accentTitle() {
        AnimHelper.accentAnim(name!!)
    }

    fun accentDescription() {
        AnimHelper.accentAnim(description!!)
    }

    fun accentFields() {
        accentTitle()
        accentDescription()
    }

    //endregion

}