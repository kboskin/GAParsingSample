package cn.meest.mobile.sample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import java.net.URLDecoder

/**
 * Class that receives campaign tracking intents and broadcasts them
 */
class CampaignTrackingReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val referrer = intent.getStringExtra(REFERRER)
        referrer?.let { referrerString ->
            URLDecoder.decode(referrerString, "utf-8")?.let { decodedReferrer ->
                parseAndStoreReferralParams(decodedReferrer)
            }
        }

        /*
         * Forward the referrer to Google Analytics so we get tracking info in Google Analytics
         * DEPRECATED */
        // val googleAnalyticsCampaignTrackingReceiver =
        //    com.google.android.gms.analytics.CampaignTrackingReceiver()
        // googleAnalyticsCampaignTrackingReceiver.onReceive(context, intent)
    }

    /**
     * Takes in the decoded referral string, splits it into the individual parts,
     * TODO here is to send data and keys in custom ways to desired analytics system
     * Mehtod works as a big loop through string passed as params
     * @param decodedReferrer The decoded referrer
     */
    private fun parseAndStoreReferralParams(decodedReferrer: String) {

        val referralParts =
            decodedReferrer.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (referralParts.isNotEmpty()) {
            // Read out the individual components
            // The referral param key
            var key: String

            // The referral param value
            var value: String

            // The position of the '=' character to read out the campaign parameters
            var positionOfEquals: Int

            for (eachPart in referralParts) {

                positionOfEquals = eachPart.indexOf('=')

                //No sense in saving it if there are no key-value pairs
                if (positionOfEquals >= 1) {

                    key = eachPart.substring(0, positionOfEquals + 1)
                    value = eachPart.substring(positionOfEquals + 1, eachPart.length)

                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        // KEY here means utm_* tagging key
                        // SOURCE here is data

                        // TODO send key and source here to your analytics
                    }
                }

            }
        }
    }

    companion object {
        private const val REFERRER = "referrer"
    }
}
