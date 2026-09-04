// PushNotificationRegistrar.kt
//
// Copyright (C) 2026-present, Celestia Development Team
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.

package space.celestia.celestiaui.pushnotification

interface PushNotificationRegistrar {
    /**
     * Registers with the push provider, or pushes the current registration state to the server
     * when [installationId] is provided by the push provider.
     * Implementations should no-op if the user has not opted in to push notifications.
     */
    suspend fun register(installationId: String? = null)
}
