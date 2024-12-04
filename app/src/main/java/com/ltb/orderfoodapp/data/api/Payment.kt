package com.ltb.orderfoodapp.data.api

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ltb.orderfoodapp.data.api.zalopay.api.CreateOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

import com.vnpay.authentication.VNP_AuthenticationActivity
import com.vnpay.authentication.VNP_SdkCompletedCallback
import org.json.JSONObject

class Payment () {
    fun payWithZaloPay(
        amount: Int,
        onError: () -> Unit = {},
        onSuccess: () -> Unit = {},
        onCancel: () -> Unit = {},
        context: Context
    ) {
        val scope = CoroutineScope(Dispatchers.Main)
        ZaloPaySDK.init(553, Environment.SANDBOX)
        val orderApi = CreateOrder()
        scope.launch {
            try {
                val data = orderApi.createOrder(amount.toString())
                val code = data.getString("return_code")
                if (code == "1") {
                    val token = data.getString("zp_trans_token")
                    ZaloPaySDK.getInstance().payOrder(
                        context as Activity,
                        token,
                        "demozpdk://app",
                        object : PayOrderListener {

                            override fun onPaymentCanceled(
                                zpTransToken: String?,
                                appTransID: String?
                            ) {
                                onCancel()
                                Log.d("ZaloPay", "Thanh toán bi cancel ")
                            }

                            override fun onPaymentError(
                                zaloPayError: ZaloPayError?,
                                zpTransToken: String?,
                                appTransID: String?
                            ) {
                                if (zaloPayError == ZaloPayError.PAYMENT_APP_NOT_FOUND) {
                                    ZaloPaySDK.getInstance().navigateToZaloOnStore(context)
                                    ZaloPaySDK.getInstance()
                                        .navigateToZaloPayOnStore(context)
                                }
                                onError()
                                Log.d("ZaloPay", "Thanh toán khong thanh cong với ")
                            }

                            override fun onPaymentSucceeded(
                                transactionId: String,
                                transToken: String,
                                appTransID: String?
                            ) {
                                Log.d("ZaloPay", "Thanh toán thành công với Transaction ID: $transactionId")
                                onSuccess()
                            }
                        }
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ZaloPayError", "Exception: ${e.message}")
            }
        }
    }


}