package io.github.rezmike.foton.ui.screens.filters

import io.github.rezmike.foton.ui.abstracts.AbstractPresenter
import io.github.rezmike.foton.utils.DaggerService
import mortar.MortarScope

class FiltersPresenter : AbstractPresenter<FiltersView, FiltersModel, FiltersPresenter>() {

    override fun initDagger(scope: MortarScope) {
        DaggerService.getDaggerComponent<FiltersScreen.Component>(scope).inject(this)
    }
}
