package com.loc.ecommerapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Hilt'in kod üretimini başlatan zorunlu anotasyon
@HiltAndroidApp
class EcommerceApplication : Application()