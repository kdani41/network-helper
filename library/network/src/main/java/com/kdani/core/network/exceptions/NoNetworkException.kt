package com.kdani.core.network.exceptions

import java.io.IOException

/**
 * Happens when unable to connect to network.
 */
internal class NoNetworkException: IOException("Unable to reach network ATM!!\nPlease try again later")