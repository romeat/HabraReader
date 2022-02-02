package com.rprikhodko.habrareader.categories.authors

import com.rprihodko.habrareader.common.dto.AuthorPreview

interface OnAuthorListener {
    fun onAuthorClick(author: AuthorPreview)
}