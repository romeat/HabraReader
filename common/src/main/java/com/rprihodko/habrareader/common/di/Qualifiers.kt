package com.rprihodko.habrareader.common.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HubCategory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthorCategory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CompanyCategory