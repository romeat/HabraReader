package com.rprikhodko.habrareader.categories.authors

import com.rprikhodko.habrareader.categories.authors.data.AuthorPreview

interface OnAuthorListener {
    fun onAuthorClick(author: AuthorPreview)
}