package com.mobatia.nasmanila.common.common_classes

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.splash.SplashActivity
import com.mobatia.nasmanila.common.constants.NaisTabConstants

class PreferenceManager {
    companion object {
        private const val sharedPrefNas = "NAS_MANILA"
        fun getUserCode(context: Context): String? {
            var userCode: String
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            userCode = sharedPreferences.getString("user_code", "").toString()
            return userCode
        }
        fun setUserCode(context: Context): String? {

                val prefs = context.getSharedPreferences(
                    sharedPrefNas,
                    Context.MODE_PRIVATE
                )
                return prefs.getString("user_code", "")

        }

        fun setIsFirstLaunch(context: Context, result: Boolean) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("is_first_launch", result)
            editor.apply()
        }
        fun getIsFirstLaunch(context: Context): Boolean {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("is_first_launch", true)
        }

        fun setAccessToken(context: Context, accessToken: String) {
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("access_token", accessToken)
            editor.apply()
        }
        fun getAccessToken(context: Context?): String {
            val tokenValue: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            tokenValue = sharedPreferences.getString("access_token", "").toString()
            return tokenValue
        }

        fun setUserID(context: Context, userID: String) {
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("userId", userID)
            editor.apply()
        }
        fun getIfHomeItemClickEnabled(context: Context): Boolean {
            val prefs: SharedPreferences = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getBoolean("home_item_click", true)
        }
        fun getButtonOneBg(context: Context): Int {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(
                "btn_one_color",
                context.resources.getColor(R.color.transparent)
            )
        }
        fun setButtonOneBg(context: Context, color: Int) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putInt("btn_one_color", color)
            editor.apply()
        }
        fun getButtonOneTextImage(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_one_pos", NaisTabConstants.TAB_NAS_TODAY) //21
        }
        fun setButtonOneTextImage(context: Context, position: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_one_pos", NaisTabConstants.TAB_NAS_TODAY)
            editor.apply()
        }
        fun getButtonOneTabId(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_one_tab", NaisTabConstants.TAB_NAS_TODAY)
        }
        fun setButtonOneTabId(context: Context, TAB_ID: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_one_tab", TAB_ID)
            editor.apply()
        }
        fun getButtonTwoBg(context: Context): Int {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(
                "btn_two_color",
                context.resources.getColor(R.color.transparent)
            )
        }
        fun setButtonTwoBg(context: Context, color: Int) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putInt("btn_two_color", color)
            editor.commit()
        }
        fun getButtonTwoTextImage(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_two_pos", "4")
        }
        fun setButtonTwoTextImage(context: Context, position: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_two_pos", position)
            editor.commit()
        }
        fun getButtonTwoTabId(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_two_tab", NaisTabConstants.TAB_PARENT_ESSENTIALS_REG)
        }
        fun setButtonTwoTabId(context: Context, TAB_ID: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_two_tab", TAB_ID)
            editor.commit()
        }
        fun getButtonThreeBg(context: Context): Int {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(
                "btn_three_color",
                context.resources.getColor(R.color.transparent)
            )
        }
        fun setButtonThreeBg(context: Context, color: Int) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putInt("btn_three_color", color)
            editor.commit()
        }
        fun getButtonThreeTextImage(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_three_pos", "7") //19
        }
        fun setButtonThreeTextImage(context: Context, position: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_three_pos", position)
            editor.commit()
        }
        fun getButtonThreeTabId(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_three_tab", NaisTabConstants.TAB_SOCIAL_MEDIA_REG)
        }
        fun setButtonThreeTabId(context: Context, TAB_ID: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_three_tab", TAB_ID)
            editor.commit()
        }
        fun getButtonFourBg(context: Context): Int {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(
                "btn_four_color",
                context.resources.getColor(R.color.transparent)
            )
        }
        fun setButtonFourBg(context: Context, color: Int) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putInt("btn_four_color", color)
            editor.commit()
        }
        fun getButtonFourTextImage(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_four_pos", "9")
        }
        fun setButtonFourTextImage(context: Context, position: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_four_pos", position)
            editor.commit()
        }
        fun getButtonFourTabId(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_four_tab", NaisTabConstants.TAB_CONTACT_US_REG)
        }
        fun setButtonFourTabId(context: Context, TAB_ID: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_four_tab", TAB_ID)
            editor.commit()
        }
        fun getButtonFiveBg(context: Context): Int {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(
                "btn_five_color",
                context.resources.getColor(R.color.rel_five)
            )
        }
        fun setButtonFiveBg(context: Context, color: Int) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putInt("btn_five_color", color)
            editor.commit()
        }
        fun getButtonFiveTextImage(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_five_pos", "5")
        }
        fun setButtonFiveTextImage(context: Context, position: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_five_pos", position)
            editor.commit()
        }
        fun getButtonFiveTabId(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_five_tab", NaisTabConstants.TAB_PROGRAMMES_REG)
        }
        fun setButtonFiveTabId(context: Context, TAB_ID: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_five_tab", TAB_ID)
            editor.commit()
        }
        fun getButtonSixBg(context: Context): Int {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(
                "btn_six_color",
                context.resources.getColor(R.color.transparent)
            )
        }
        fun setButtonSixBg(context: Context, color: Int) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putInt("btn_six_color", color)
            editor.commit()
        }
        fun getButtonSixTextImage(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_six_pos", "2")
        }
        fun setButtonSixTextImage(context: Context, position: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_six_pos", position)
            editor.commit()
        }
        fun getButtonSixTabId(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_six_tab", NaisTabConstants.TAB_NOTIFICATIONS_REG)
        }
        fun getButtonSevenBg(context: Context): Int {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(
                "btn_seven_color",
                context.resources.getColor(R.color.transparent)
            )
        }
        fun setButtonSevenBg(context: Context, color: Int) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putInt("btn_seven_color", color)
            editor.commit()
        }
        fun getButtonSevenTextImage(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_seven_pos", "3")
        }
        fun setButtonSevenTextImage(context: Context, position: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_seven_pos", position)
            editor.commit()
        }
        fun getButtonSevenTabId(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_seven_tab", NaisTabConstants.TAB_ABSENCES_REG)
        }
        fun setButtonSevenTabId(context: Context, TAB_ID: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_seven_tab", TAB_ID)
            editor.commit()
        }
        fun getButtonEightBg(context: Context): Int {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(
                "btn_eight_color",
                context.resources.getColor(R.color.transparent)
            )
        }
        fun setButtonEightBg(context: Context, color: Int) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putInt("btn_eight_color", color)
            editor.commit()
        }
        fun getButtonEightTextImage(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_eight_pos", "1") //19
        }
        fun setButtonEightTextImage(context: Context, position: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_eight_pos", position)
            editor.apply()
        }
        fun getButtonEightTabId(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_eight_tab", NaisTabConstants.TAB_CALENDAR_REG)
        }
        fun setButtonEightTabId(context: Context, TAB_ID: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_eight_tab", TAB_ID)
            editor.apply()
        }
        fun getButtonNineBg(context: Context): Int {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getInt(
                "btn_nine_color",
                context.resources.getColor(R.color.transparent)
            )
        }
        fun setButtonNineBg(context: Context, color: Int) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putInt("btn_nine_color", color)
            editor.apply()
        }
        fun getButtonNineTextImage(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_nine_pos", "6")
        }
        fun setButtonNineTextImage(context: Context, position: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_nine_pos", position)
            editor.apply()
        }
        fun getButtonNineTabId(context: Context): String? {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            return prefs.getString("btn_nine_tab", NaisTabConstants.TAB_PARENTS_MEETING_REG)
        }
        fun setButtonNineTabId(context: Context, TAB_ID: String?) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("btn_nine_tab", TAB_ID)
            editor.apply()
        }

        fun getButtonOneGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getInt(
                "btn_one_guest_color", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setButtonOneGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("btn_one_guest_color", color)
        editor.apply()
    }
    fun getButtonOneGuestTextImage(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_one_guest_pos", NaisTabConstants.TAB_NAS_TODAY)
    }
    fun setButtonOneGuestTextImage(
            context: Context,
            position: String?
    ) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_one_guest_pos", position)
        editor.apply()
    }
    fun getButtonOneGuestTabId(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_one_guest_tab", NaisTabConstants.TAB_NAS_TODAY)
    }
    fun setButtonOneGuestTabId(context: Context, TAB_ID: String?) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_one_guest_tab", TAB_ID)
        editor.apply()
    }
    fun getButtonTwoGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getInt(
                "btn_two_guest_color", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setButtonTwoGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("btn_two_guest_color", color)
        editor.apply()
    }
    fun getButtonTwoGuestTextImage(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_two_guest_pos", NaisTabConstants.TAB_SOCIAL_MEDIA_GUEST)
    }
    fun setButtonTwoGuestTextImage(
            context: Context,
            position: String?
    ) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_two_guest_pos", position)
        editor.apply()
    }
    fun getButtonTwoGuestTabId(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_two_guest_tab", NaisTabConstants.TAB_SOCIAL_MEDIA_GUEST)
    }
    fun setButtonTwoGuestTabId(context: Context, TAB_ID: String?) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_two_guest_tab", TAB_ID)
        editor.apply()
    }
    fun getButtonThreeGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getInt(
                "btn_three_guest_color", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setButtonThreeGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("btn_three_guest_color", color)
        editor.apply()
    }
    fun getButtonThreeGuestTextImage(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_three_guest_pos", NaisTabConstants.TAB_PARENT_ESSENTIALS_GUEST)
    }
    fun setButtonThreeGuestTextImage(
            context: Context,
            position: String?
    ) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_three_guest_pos", position)
        editor.apply()
    }
    fun getButtonThreeGuestTabId(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_three_guest_tab", NaisTabConstants.TAB_PARENT_ESSENTIALS_GUEST)
    }
    fun setButtonThreeGuestTabId(context: Context, TAB_ID: String?) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_three_guest_tab", TAB_ID)
        editor.apply()
    }
    fun getButtonFourGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getInt(
                "btn_four_guest_color", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setButtonFourGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("btn_four_guest_color", color)
        editor.apply()
    }
    fun getButtonFourGuestTextImage(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_four_guest_pos", NaisTabConstants.TAB_SETTINGS)
    }
    fun setButtonFourGuestTextImage(
            context: Context,
            position: String?
    ) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_four_guest_pos", position)
        editor.apply()
    }
    fun getButtonFourGuestTabId(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_four_guest_tab", NaisTabConstants.TAB_SETTINGS)
    }
    fun setButtonFourGuestTabId(context: Context, TAB_ID: String?) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_four_guest_tab", TAB_ID)
        editor.apply()
    }
    fun getButtonFiveGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getInt(
                "btn_five_guest_color", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setButtonFiveGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("btn_five_guest_color", color)
        editor.apply()
    }
    fun getButtonFiveGuestTextImage(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_five_guest_pos", NaisTabConstants.TAB_NOTIFICATIONS_GUEST)
    }
    fun setButtonFiveGuestTextImage(
            context: Context,
            position: String?
    ) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_five_guest_pos", position)
        editor.apply()
    }
    fun getButtonFiveGuestTabId(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_five_guest_tab", NaisTabConstants.TAB_NOTIFICATIONS_GUEST)
    }
    fun setButtonFiveGuestTabId(context: Context, TAB_ID: String?) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_five_guest_tab", TAB_ID)
        editor.apply()
    }
    fun getButtonSixGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getInt(
                "btn_six_guest_color", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setButtonSixGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("btn_six_guest_color", color)
        editor.apply()
    }
    fun getButtonSixGuestTextImage(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_six_guest_pos", NaisTabConstants.TAB_PROGRAMMES_GUEST)
    }
    fun setButtonSixGuestTextImage(
            context: Context,
            position: String?
    ) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_six_guest_pos", position)
        editor.apply()
    }
    fun getButtonSixGuestTabId(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_six_guest_tab", NaisTabConstants.TAB_PROGRAMMES_GUEST)
    }
    fun setButtonSixGuestTabId(context: Context, TAB_ID: String?) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_six_guest_tab", TAB_ID)
        editor.apply()
    }
    fun getButtonSevenGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getInt(
                "btn_seven_guest_color", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setButtonSevenGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("btn_seven_guest_color", color)
        editor.apply()
    }
    fun getButtonSevenGuestTextImage(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_seven_guest_pos", NaisTabConstants.TAB_CONTACT_US_GUEST)
    }
    fun setButtonSevenGuestTextImage(
            context: Context,
            position: String?
    ) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_seven_guest_pos", position)
        editor.apply()
    }
    fun getButtonSevenGuestTabId(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_seven_guest_tab", NaisTabConstants.TAB_CONTACT_US_GUEST)
    }
    fun setButtonSevenGuestTabId(context: Context, TAB_ID: String?) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_seven_guest_tab", TAB_ID)
        editor.apply()
    }
    fun getButtonEightGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getInt(
                "btn_eight_guest_color", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setButtonEightGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("btn_eight_guest_color", color)
        editor.apply()
    }
    fun getButtonEightGuestTextImage(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString(
                "btn_eight_guest_pos",
                NaisTabConstants.TAB_ABOUT_US_GUEST
        )
    }
    fun setButtonEightGuestTextImage(
            context: Context,
            position: String?
    ) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_eight_guest_pos", position)
        editor.apply()
    }
    fun getButtonEightGuestTabId(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_eight_guest_tab", NaisTabConstants.TAB_ABOUT_US_GUEST)
    }
    fun setButtonEightGuestTabId(context: Context, TAB_ID: String?) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_eight_guest_tab", TAB_ID)
        editor.apply()
    }
    fun getButtonNineGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getInt(
                "btn_nine_guest_color", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setButtonNineGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("btn_nine_guest_color", color)
        editor.apply()
    }
    fun getButtonNineGuestTextImage(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_nine_guest_pos", NaisTabConstants.TAB_LOGOUT_GUEST)
    }
    fun setButtonNineGuestTextImage(
            context: Context,
            position: String?
    ) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_nine_guest_pos", position)
        editor.apply()
    }
    fun getButtonNineGuestTabId(context: Context): String? {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        return prefs.getString("btn_nine_guest_tab", NaisTabConstants.TAB_LOGOUT_GUEST)
    }
    fun setButtonNineGuestTabId(context: Context, TAB_ID: String?) {
        val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("btn_nine_guest_tab", TAB_ID)
        editor.apply()
    }

        fun getUserEmail(context: Context): Any {
            var userid: String
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            userid = prefs.getString("user_email", "").toString()
            return userid
        }

        fun setGoToSettings(context: Context, mGoToSetting: String) {
            val prefs = context.getSharedPreferences(
                sharedPrefNas,
                Context.MODE_PRIVATE
            )
            val editor = prefs.edit()
            editor.putString("GoToSetting", mGoToSetting)
            editor.apply()
        }


    }
}