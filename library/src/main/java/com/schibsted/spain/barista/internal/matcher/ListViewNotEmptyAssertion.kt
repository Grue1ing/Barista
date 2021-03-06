package com.schibsted.spain.barista.internal.matcher

import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.view.View
import android.widget.ListView
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not

class ListViewNotEmptyAssertion : ViewAssertion {

  override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
    if (noViewFoundException != null) {
      throw noViewFoundException
    }

    val listView = view as ListView
    val adapter = listView.adapter
    val listViewItemCount = adapter.count
    assertThat<Int>("List item count mismatch. ", listViewItemCount, not<Int>(0))
  }
}