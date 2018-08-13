package com.stratagile.qlink.shadowsocks.plugin

import com.stratagile.qlink.R
import com.stratagile.qlink.application.AppConfig

object NoPlugin : Plugin() {
    override val id: String get() = ""
    override val label: CharSequence get() = AppConfig.instance.getText(R.string.plugin_disabled)
}
