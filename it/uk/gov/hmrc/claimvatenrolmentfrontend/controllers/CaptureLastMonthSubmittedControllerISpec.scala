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
import uk.gov.hmrc.claimvatenrolmentfrontend.views.CaptureLastMonthSubmittedViewTests

class CaptureLastMonthSubmittedControllerISpec extends ComponentSpecHelper with CaptureLastMonthSubmittedViewTests {

  s"GET /$testJourneyId/last-vat-return-date" should {
    "return OK" in {
      lazy val result = get(s"/$testJourneyId/last-vat-return-date")

      result.status mustBe OK
    }
    "return the correct view" should {
      lazy val result = get(s"/$testJourneyId/last-vat-return-date")

      testCaptureLastMonthSubmittedViewTests(result)
    }
  }

  "POST /last-vat-return-date" should {
    "redirect to Check Your Answers page if a month is selected" in {
      lazy val result = post(s"/$testJourneyId/last-vat-return-date")("return_date" -> "January")

      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(routes.CheckYourAnswersController.show(testJourneyId).url)
      )
    }
    "return BAD_REQUEST if no month is selected" in {
      lazy val result = post(s"/$testJourneyId/last-vat-return-date")()

      result.status mustBe BAD_REQUEST
    }
    "return the correct view with error messages" should {
      lazy val result = post(s"/$testJourneyId/last-vat-return-date")()

      testCaptureLastMonthSubmittedErrorViewTests(result)
    }
  }


}
