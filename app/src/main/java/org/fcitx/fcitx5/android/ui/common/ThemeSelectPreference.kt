package org.fcitx.fcitx5.android.ui.common

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import org.fcitx.fcitx5.android.data.theme.Theme
import org.fcitx.fcitx5.android.data.theme.ThemeManager
import org.fcitx.fcitx5.android.ui.main.settings.theme.ResponsiveThemeListView
import org.fcitx.fcitx5.android.ui.main.settings.theme.SimpleThemeListAdapter
import splitties.dimensions.dp

class ThemeSelectPreference(context: Context, private val defaultTheme: Theme) :
    Preference(context) {

    init {
        setDefaultValue(defaultTheme.name)
    }

    private val currentThemeName
        get() = getPersistedString(defaultTheme.name)

    override fun onClick() {
        val view = ResponsiveThemeListView(context).apply {
            // force AlertDialog's customPanel to grow
            minimumHeight = dp(500)
        }
        val allThemes = ThemeManager.getAllThemes()
        val adapter = SimpleThemeListAdapter(allThemes).apply {
            selected = allThemes.indexOfFirst { it.name == currentThemeName }
        }
        view.adapter = adapter
        AlertDialog.Builder(context)
            .setTitle(title)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                adapter.selectedTheme?.let {
                    if (callChangeListener(it.name)) {
                        persistString(it.name)
                        notifyChanged()
                    }
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setView(view)
            .show()
    }

}