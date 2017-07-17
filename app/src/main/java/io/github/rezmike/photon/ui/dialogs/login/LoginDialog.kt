package io.github.rezmike.photon.ui.dialogs.login

import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import io.github.rezmike.photon.R
import io.github.rezmike.photon.data.storage.dto.DialogResult
import io.github.rezmike.photon.data.storage.dto.LoginInfoDto
import io.github.rezmike.photon.ui.dialogs.AbstractDialog
import io.github.rezmike.photon.ui.others.AnimHelper
import io.github.rezmike.photon.ui.others.CustomTextWatcher
import io.github.rezmike.photon.ui.others.changeError
import mortar.PopupPresenter

class LoginDialog(context: Context) : AbstractDialog<LoginInfoDto>(context) {

    private var emailEt: EditText? = null
    private var passwordEt: EditText? = null
    private var okBtn: Button? = null
    private var cancelBtn: Button? = null

    override fun getLayoutRes() = R.layout.dialog_login

    override fun onFinishInflate(view: View, presenter: PopupPresenter<LoginInfoDto, DialogResult>, info: LoginInfoDto) {
        if (presenter !is LoginDialogPresenter) {
            throw ClassCastException("presenter must implements LoginDialogPresenter")
        }
        emailEt = view.findViewById(R.id.email_et) as EditText
        passwordEt = view.findViewById(R.id.password_et) as EditText
        okBtn = view.findViewById(R.id.ok_btn) as Button
        cancelBtn = view.findViewById(R.id.cancel_btn) as Button

        emailEt?.addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                presenter.checkEmail(s.toString())
            }
        })
        passwordEt?.addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                presenter.checkPassword(s.toString())
            }
        })
        emailEt?.setText(info.email)
        passwordEt?.setText(info.password)

        okBtn?.setOnClickListener { presenter.onClickOk() }
        cancelBtn?.setOnClickListener { presenter.onClickCancel() }
    }

    override fun onDialogDismiss() {
        okBtn = null
        cancelBtn = null
        emailEt = null
        passwordEt = null
    }

    fun showEmailError() {
        emailEt?.changeError(true, context.getString(R.string.login_email_error))
    }

    fun hideEmailError() {
        emailEt?.changeError(false)
    }

    fun showPasswordError() {
        passwordEt?.changeError(true, context.getString(R.string.login_password_error))
    }

    fun hidePasswordError() {
        passwordEt?.changeError(false)
    }

    fun accentEmail() {
        AnimHelper.accentAnim(emailEt!!)
    }

    fun accentPassword() {
        AnimHelper.accentAnim(passwordEt!!)
    }

    fun accentFields() {
        accentEmail()
        accentPassword()
    }
}
