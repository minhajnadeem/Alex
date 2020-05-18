package com.example.datingapp.inappbilling

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.*


class MyBillingClient(val context: Activity) : PurchasesUpdatedListener {

    private val PRODUCT_ID = "android.test.purchased"
    private val billingClient: BillingClient =
        BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build()
    private lateinit var testProduct: SkuDetails

    init {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.d("TAG", "onBillingServiceDisconnected")
            }

            override fun onBillingSetupFinished(result: BillingResult?) {
                if (result!!.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d("TAG", "onBillingSetupFinished OK")
                    loadAllSkus()
                    consumePurchase()

                } else {
                    Log.d("TAG", "onBillingSetupFinished NOT OK " + result.responseCode)

                }
            }

        })
    }

    override fun onPurchasesUpdated(p0: BillingResult?, p1: MutableList<Purchase>?) {

    }

    fun loadAllSkus() {
        val params = SkuDetailsParams.newBuilder().setSkusList(listOf("android.test.purchased"))
            .setType(BillingClient.SkuType.INAPP).build()
        if (billingClient.isReady) {
            billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailList.isNotEmpty()) {
                    for (skuDetail in skuDetailList) {
                        Log.d("TAG", skuDetail.sku)
                        testProduct = skuDetail
                    }
                } else {
                    Log.d("TAG", "loadAllSkus not OK")

                }
            }
        } else {
            Log.d("TAG", "loadAllSkus not ready")

        }
    }

    private fun consumePurchase() {
        billingClient.queryPurchaseHistoryAsync(PRODUCT_ID
        ) { result, purchaseHistory ->
            if (purchaseHistory != null) {
                for (purchase in purchaseHistory) {
                    val consumeParams = ConsumeParams
                        .newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    billingClient.consumeAsync(
                        consumeParams
                    ) { billingResult, s ->
                        //do something..
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            Log.d("TAG", "consume ok")
                        }
                    }
                }
            }
        }

    }

    fun launchPurchaseFlow() {
        if (billingClient.isReady) {
            val newBuilder = BillingFlowParams.newBuilder()
            newBuilder.setSkuDetails(testProduct)
            val billingFlowParams = newBuilder.build()
            billingClient.launchBillingFlow(context, billingFlowParams)
        }
    }
}