/*
 * Copyright 2021 HM Revenue & Customs
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

package uk.gov.hmrc.claimvatenrolmentfrontend.controllers

import play.api.test.Helpers._
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.TestConstants.testJourneyId
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.CaptureVatRegistrationDateViewTests

class CaptureVatRegistrationDateControllerISpec extends ComponentSpecHelper with CaptureVatRegistrationDateViewTests {

  s"GET /$testJourneyId/vat-registration-date" should {
    "return OK" in {
      lazy val result = get(s"/$testJourneyId/vat-registration-date")

      result.status mustBe OK
    }

    "return a view" should {
      lazy val result = get(s"/$testJourneyId/vat-registration-date")

      testCaptureVatRegistrationDateViewTests(result)
    }
  }

  s"POST /$testJourneyId/vat-registration-date" should {
    "redirect to CaptureBusinessPostcode if the date is valid" in {
      lazy val result = post(s"/$testJourneyId/vat-registration-date")(
        "date.day" -> "1",
        "date.month" -> "1",
        "date.year" -> "2020"
      )

      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(routes.CaptureBusinessPostcodeController.show(testJourneyId).url)
      )
    }

    "return a BAD_REQUEST if the date is missing and show the correct errors" should {
      lazy val result = post(s"/$testJourneyId/vat-registration-date")()

      result.status mustBe BAD_REQUEST
      testCaptureVatRegistrationDateMissingErrorViewTests(result)
    }

    "return a BAD_REQUEST if the date is invalid and show the correct errors" should {
      lazy val result = post(s"/$testJourneyId/vat-registration-date")(
        "date.day" -> "1",
        "date.month" -> "1",
        "date.year" -> "invalidYear"
      )

      result.status mustBe BAD_REQUEST
      testCaptureVatRegistrationDateInvalidErrorViewTests(result)
    }

    "return a BAD_REQUEST if the year is invalid and show the correct errors" should {
      lazy val result = post(s"/$testJourneyId/vat-registration-date")(
        "date.day" -> "1",
        "date.month" -> "1",
        "date.year" -> "94"
      )

      result.status mustBe BAD_REQUEST
      testCaptureVatRegistrationDateInvalidErrorViewTests(result)
    }

    "return a BAD_REQUEST if the date is in the future and show the correct errors" should {
      lazy val result = post(s"/$testJourneyId/vat-registration-date")(
        "date.day" -> "1",
        "date.month" -> "1",
        "date.year" -> "2100"
      )

      result.status mustBe BAD_REQUEST
      testCaptureVatRegistrationDateFutureErrorViewTests(result)
    }
  }
}