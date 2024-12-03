package com.ltb.orderfoodapp.data.model

import com.ltb.orderfoodapp.data.api.zalopay.constant.AppInfo
import com.ltb.orderfoodapp.data.api.zalopay.helper.Helpers
import java.util.Date

data class ZaloPayOrder(
    var appId: String = AppInfo.APP_ID.toString(),
    var appUser: String = "OrderFoodApp",
    var appTime: String = Date().time.toString(),
    var amount: String,
    var appTransId: String = Helpers.getAppTransId(),
    var embedData: String = "{\"promotioninfo\":\"\",\"merchantinfo\":\"embeddata123\"}",
    var items: String = "[{\"itemid\":\"knb\",\"itemname\":\"kim nguyen bao\",\"itemprice\":198400,\"itemquantity\":1}]",
    var bankCode: String = "zalopayapp",
    var description: String = "Merchant pay for order #${Helpers.getAppTransId()}",
    var mac: String = Helpers.getMac(
        AppInfo.MAC_KEY,
        data = "$appId|$appTransId|$appUser|$amount|$appTime|$embedData|$items"
    )
)
