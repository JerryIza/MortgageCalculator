package com.example.mortgagecalculator

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//All Apps that use Hilt must use this annotation on Application
//triggers Hilt Code Generation serves as the application DI container
@HiltAndroidApp
class BaseApplication : Application ()