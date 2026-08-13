/*
 * ModalNavigationDrawer.kt
 *
 * Copyright (C) 2001-2020, Celestia Development Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

package space.celestia.mobilecelestia.compose

import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer as MaterialModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

/** The side of the screen a [ModalNavigationDrawer] is anchored to. */
enum class DrawerAlignment {
    /** The drawer slides in from the leading (start) edge. This is the Material default. */
    Start,

    /** The drawer slides in from the trailing (end) edge. */
    End,
}

/**
 * A thin wrapper around the Material3 [ModalNavigationDrawer] that also supports anchoring the
 * drawer to the [DrawerAlignment.End] edge.
 *
 * The Material component only knows how to slide the sheet in from the leading edge, but it already
 * mirrors correctly under a right-to-left [LayoutDirection]. To place the sheet on the opposite
 * (end) edge we simply run the drawer with the layout direction flipped, then restore the original
 * direction inside [drawerContent] and [content] so their own UI is not mirrored.
 *
 * @param drawerContent content inside this drawer
 * @param modifier the [Modifier] to be applied to this drawer
 * @param drawerState state of the drawer
 * @param drawerAlignment the edge the drawer is anchored to
 * @param gesturesEnabled whether or not the drawer can be interacted by gestures
 * @param scrimColor color of the scrim that obscures content when the drawer is open
 * @param content content of the rest of the UI
 */
@Composable
fun ModalNavigationDrawer(
    drawerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    drawerAlignment: DrawerAlignment = DrawerAlignment.Start,
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    content: @Composable () -> Unit,
) {
    if (drawerAlignment == DrawerAlignment.Start) {
        MaterialModalNavigationDrawer(
            drawerContent = drawerContent,
            modifier = modifier,
            drawerState = drawerState,
            gesturesEnabled = gesturesEnabled,
            scrimColor = scrimColor,
            content = content,
        )
        return
    }

    val originalLayoutDirection = LocalLayoutDirection.current
    val flippedLayoutDirection =
        if (originalLayoutDirection == LayoutDirection.Ltr) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides flippedLayoutDirection) {
        MaterialModalNavigationDrawer(
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides originalLayoutDirection) {
                    drawerContent()
                }
            },
            modifier = modifier,
            drawerState = drawerState,
            gesturesEnabled = gesturesEnabled,
            scrimColor = scrimColor,
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides originalLayoutDirection) {
                    content()
                }
            },
        )
    }
}
