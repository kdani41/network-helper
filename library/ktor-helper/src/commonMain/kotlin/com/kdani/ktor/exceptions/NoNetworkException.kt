package com.kdani.ktor.exceptions

import io.ktor.utils.io.errors.IOException

/**
 * Happens when unable to connect to network.
 */
internal class NoNetworkException :
    IOException("Unable to reach network ATM!!\nPlease try again later")