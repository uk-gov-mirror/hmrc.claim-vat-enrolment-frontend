
/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.claimvatenrolmentfrontend.views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.MessageLookup.{Base, BetaBanner, Header, CheckYourAnswers => messages}
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.TestConstants._
import uk.gov.hmrc.claimvatenrolmentfrontend.controllers.routes
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ViewSpecHelper._

import scala.collection.JavaConverters._


trait CheckYourAnswersViewTests {
  this: ComponentSpecHelper =>

  def testCheckYourAnswersView(result: => WSResponse): Unit = {

    lazy val doc: Document = Jsoup.parse(result.body)

    "have a sign out link in the header" in {
      doc.getSignOutText mustBe Header.signOut
    }
    "have the correct beta banner" in {
      doc.getBanner.text mustBe BetaBanner.title
    }

    "have the correct title" in {
      doc.title mustBe messages.title
    }

    "have the correct heading" in {
      doc.getH1Elements.text mustBe messages.heading
    }

    "have a summary list which" should {
      lazy val summaryListRows = doc.getSummaryListRows.iterator().asScala.toList

      "have 6 rows" in {
        summaryListRows.size mustBe 6
      }

      "have a vat number row" in {
        val vatNumberRow = summaryListRows.head

        vatNumberRow.getSummaryListQuestion mustBe messages.vatNumberRow
        vatNumberRow.getSummaryListAnswer mustBe testVatNumber //TODO update when page is built
        vatNumberRow.getSummaryListChangeLink mustBe "/"
        vatNumberRow.getSummaryListChangeText mustBe s"${Base.change} ${messages.vatNumberRow}"
      }

      "have a vat registration date row" in {
        val vatRegDateRow = summaryListRows(1)

        vatRegDateRow.getSummaryListQuestion mustBe messages.vatRegDateRow
        vatRegDateRow.getSummaryListAnswer mustBe testVatRegDate
        vatRegDateRow.getSummaryListChangeLink mustBe routes.CaptureVatRegistrationDateController.show().url
        vatRegDateRow.getSummaryListChangeText mustBe s"${Base.change} ${messages.vatRegDateRow}"
      }

      "have a business postcode row" in {
        val businessPostcodeRow = summaryListRows(2)

        businessPostcodeRow.getSummaryListQuestion mustBe messages.businessPostcodeRow
        businessPostcodeRow.getSummaryListAnswer mustBe testBusinessPostcode
        businessPostcodeRow.getSummaryListChangeLink mustBe routes.CaptureBusinessPostcodeController.show().url
        businessPostcodeRow.getSummaryListChangeText mustBe s"${Base.change} ${messages.businessPostcodeRow}"
      }

      "have a vat return row" in {
        val vatReturnRow = summaryListRows(3)

        vatReturnRow.getSummaryListQuestion mustBe messages.vatReturnsRow
        vatReturnRow.getSummaryListAnswer mustBe Base.yes
        vatReturnRow.getSummaryListChangeLink mustBe routes.CaptureSubmittedVatReturnController.show().url
        vatReturnRow.getSummaryListChangeText mustBe s"${Base.change} ${messages.vatReturnsRow}"
      }

      "have a box five row" in {
        val boxFiveRow = summaryListRows(4)

        boxFiveRow.getSummaryListQuestion mustBe messages.boxFiveRow
        boxFiveRow.getSummaryListAnswer mustBe testBoxFive
        boxFiveRow.getSummaryListChangeLink mustBe routes.CaptureBox5FigureController.show().url
        boxFiveRow.getSummaryListChangeText mustBe s"${Base.change} ${messages.boxFiveRow}"
      }

      "have a last return month row" in {
        val lastReturnMonthRow = summaryListRows.last

        lastReturnMonthRow.getSummaryListQuestion mustBe messages.lastReturnMonthRow
        lastReturnMonthRow.getSummaryListAnswer mustBe testLastReturnMonth
        lastReturnMonthRow.getSummaryListChangeLink mustBe "/" //TODO update when page is built
        lastReturnMonthRow.getSummaryListChangeText mustBe s"${Base.change} ${messages.lastReturnMonthRow}"
      }
    }

    "have a continue and confirm button" in {
      doc.getSubmitButton.first.text mustBe Base.continue
    }
  }

}
