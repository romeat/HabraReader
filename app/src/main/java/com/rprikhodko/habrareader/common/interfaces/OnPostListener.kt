package com.rprikhodko.habrareader.common.interfaces

import com.rprikhodko.habrareader.common.data.dto.PostPreview

interface OnPostListener {
    fun onPostClick(post: PostPreview)
}