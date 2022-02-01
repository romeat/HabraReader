package com.rprikhodko.habrareader.categories.hubs

import com.rprikhodko.habrareader.categories.hubs.data.HubPreview

interface OnHubListener {
    fun onHubClick(hub: HubPreview)
}