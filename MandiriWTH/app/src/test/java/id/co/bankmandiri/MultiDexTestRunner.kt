package id.co.bankmandiri

import android.os.Bundle

import androidx.multidex.MultiDex
import androidx.test.runner.AndroidJUnitRunner

/**
 * @author hendrawd on 20 Mar 2019
 */

class MultiDexTestRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle) {
        MultiDex.install(targetContext)
        super.onCreate(arguments)
    }
}
