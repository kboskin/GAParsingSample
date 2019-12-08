package cn.meest.mobile.sample

import android.Manifest
import android.accounts.AccountManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startFlowButton.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.GET_ACCOUNTS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat
                        .requestPermissions(
                            this,
                            arrayOf(Manifest.permission.GET_ACCOUNTS),
                            REQUEST_ACCOUNTS_CODE
                        )
                } else {
                    getGoogleEmails()
                }
            } else {
                val intent = AccountManager.newChooseAccountIntent(
                    null,
                    null,
                    arrayOf(GOOGLE_PACKAGE_PREFIX),
                    false,
                    null,
                    null,
                    null,
                    null)
                startActivityForResult(intent, ACCOUNT_PICK)
            }
        }
    }

    fun getGoogleEmails() {
        val possibleEmails = mutableListOf<String>()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val manager = AccountManager.get(this)
            val accounts = manager.getAccountsByType(GOOGLE_PACKAGE_PREFIX)

            for (account in accounts) {
                possibleEmails.add(account.name)
            }
        }

        // TODO implement analytics sending here
        Log.d(ACCOUNT_LOG_TAG, possibleEmails.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ACCOUNT_PICK) {
            val accountName = data?.extras?.get(AccountManager.KEY_ACCOUNT_NAME) ?: "NONE"

            // TODO filter NONE and implement analytics sending here
            Log.d(ACCOUNT_LOG_TAG, "Selected account: $accountName")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_ACCOUNTS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGoogleEmails()
            } else {
                Toast.makeText(this, "Permission must be granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private companion object {
        private const val ACCOUNT_PICK = 10
        private const val REQUEST_ACCOUNTS_CODE = 999
        private const val GOOGLE_PACKAGE_PREFIX = "com.google"
        private const val ACCOUNT_LOG_TAG = "SelectedAccountLog"
    }
}
