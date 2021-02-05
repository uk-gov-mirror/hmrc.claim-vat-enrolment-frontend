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
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.CaptureBusinessPostcodeViewTests

class CaptureBusinessPostcodeControllerISpec extends ComponentSpecHelper with CaptureBusinessPostcodeViewTests {

  "GET /business-postcode" should {
    "return OK" in {
      lazy val result = get("/business-postcode")

      result.status mustBe OK
    }
    "return a view" should {
      lazy val result = get("/business-postcode")

      testCaptureBusinessPostcodeViewTests(result)
    }
  }

  "POST /business-postcode" should {
    "redirect to CaptureSubmittedVatReturn" in {
      lazy val result = post("/business-postcode")(
        "business_postcode" -> "ZZ1 1ZZ"
      )

      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(routes.CaptureSubmittedVatReturnController.show().url)
      )
    }

    "return a BAD_REQUEST if the postcode is missing" in {
      lazy val result = post("/business-postcode")(
        "business_postcode" -> ""
      )

      result.status mustBe BAD_REQUEST
    }

    "return a BAD_REQUEST if the postcode is invalid" in {
      lazy val result = post("/business-postcode")(
        "business_postcode" -> "111 111"
      )

      result.status mustBe BAD_REQUEST
      }
  }

}
