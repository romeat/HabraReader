package com.rprikhodko.habrareader.categories.companies

import com.rprihodko.habrareader.common.dto.CompanyPreview

interface OnCompanyListener {
    fun onCompanyClick(company: CompanyPreview)
}