package com.schibsted.spain.barista.interaction

import android.support.annotation.IdRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.RootMatchers.isPlatformPopup
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.schibsted.spain.barista.internal.failurehandler.SpyFailureHandler
import com.schibsted.spain.barista.internal.failurehandler.withFailureHandler
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedAnd
import com.schibsted.spain.barista.internal.matcher.HelperMatchers.menuIdMatcher
import org.hamcrest.Matcher
import org.hamcrest.Matchers.hasToString

object BaristaMenuClickInteractions {

  @JvmStatic
  fun clickMenu(@IdRes id: Int) {
    val spyFailureHandler = SpyFailureHandler()
    try {
      clickDisplayedView(withId(id), spyFailureHandler)
    } catch (noMatchingViewException: NoMatchingViewException) {
      try {
        clickOverflowListMenu(menuIdMatcher(id), spyFailureHandler)
      } catch (error: Exception) {
        spyFailureHandler.resendFirstError("Could not click on menu id, neither as action or overflow")
      }
    }
  }

  @JvmStatic
  fun clickMenu(text: String) {
    val spyFailureHandler = SpyFailureHandler()
    try {
      clickDisplayedView(withText(text), spyFailureHandler)
    } catch (noMatchingViewException: NoMatchingViewException) {
      try {
        clickViewWithDescription(text, spyFailureHandler)
      } catch (noMatchingViewByTextException: NoMatchingViewException) {
        try {
          clickOverflowListMenu(hasToString<String>(text), spyFailureHandler)
        } catch (error: Exception) {
          spyFailureHandler.resendFirstError("Could not click on menu title <$text>, neither as action or overflow")
        }
      }
    }
  }

  private fun clickDisplayedView(matcher: Matcher<View>, spyFailureHandler: SpyFailureHandler) {
    onView(displayedAnd(matcher)).withFailureHandler(spyFailureHandler).perform(click())
  }

  private fun clickViewWithDescription(text: String, spyFailureHandler: SpyFailureHandler) {
    onView(displayedAnd(withContentDescription(text))).withFailureHandler(spyFailureHandler).perform(click())
  }

  private fun clickOverflowListMenu(itemMatcher: Matcher<*>, spyFailureHandler: SpyFailureHandler) {
    openOverflow()
    withFailureHandler(spyFailureHandler) {
      onData(itemMatcher).inRoot(isPlatformPopup()).perform(click())
    }
  }

  private fun openOverflow() {
    openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
  }
}
