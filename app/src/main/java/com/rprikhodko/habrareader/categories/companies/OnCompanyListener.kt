package com.rprikhodko.habrareader.categories.companies

import com.rprikhodko.habrareader.categories.companies.data.CompanyPreview

interface OnCompanyListener {
    fun onCompanyClick(company: CompanyPreview)
}