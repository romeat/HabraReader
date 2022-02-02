package com.rprikhodko.habrareader.categories.hubs

import com.rprihodko.habrareader.common.dto.HubPreview

interface OnHubListener {
    fun onHubClick(hub: HubPreview)
}