/*******************************************************************************
 *                                                                             *
 *  Copyright (C) 2017 by Max Lv <max.c.lv@gmail.com>                          *
 *  Copyright (C) 2017 by Mygod Studio <contact-shadowsocks-android@mygod.be>  *
 *                                                                             *
 *  This program is free software: you can redistribute it and/or modify       *
 *  it under the terms of the GNU General Public License as published by       *
 *  the Free Software Foundation, either version 3 of the License, or          *
 *  (at your option) any later version.                                        *
 *                                                                             *
 *  This program is distributed in the hope that it will be useful,            *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 *  GNU General Public License for more details.                               *
 *                                                                             *
 *  You should have received a copy of the GNU General Public License          *
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *                                                                             *
 *******************************************************************************/

package com.stratagile.qlink.shadowsocks.tasker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stratagile.qlink.R.string.app
import com.stratagile.qlink.application.AppConfig
import com.stratagile.qlink.shadowsocks.database.ProfileManager

class ActionListener : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val settings = Settings.fromIntent(intent)
        var changed = false
        if (ProfileManager.getProfile(settings.profileId) != null) {
            AppConfig.instance.switchProfile(settings.profileId)
            changed = true
        }
        if (settings.switchOn) {
            AppConfig.instance.startService()
            if (changed) AppConfig.instance.reloadService()
        } else AppConfig.instance.stopService()
    }
}
