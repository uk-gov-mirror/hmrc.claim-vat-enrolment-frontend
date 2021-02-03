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

package uk.gov.hmrc.claimvatenrolmentfrontend.assets

object MessageLookup {

  object Base {
    val continue = "Continue"
  }

  object Header {
    val signOut = "Sign out"
  }

  object CaptureBusinessPostcode {
    val title = "What is the UK postcode where your business is registered for VAT? - Claim VAT Enrolment - GOV.UK"
    val heading = "What is the UK postcode where your business is registered for VAT?"
    val hint = "For example, AB1 2YZ"
    val link_text = "The business does not have a UK postcode"
  }

  object CaptureVatRegistrationDate {
    val title = "When did you become VAT registered? - Claim VAT Enrolment - GOV.UK"
    val heading = "When did you become VAT registered?"
    val para = "You can find this date on your VAT registration certificate."
    val hint = "For example, 6 4 2017"
  }

  object CaptureBox5Figure {
    val title = "What is your latest VAT Return total? - Claim VAT Enrolment - GOV.UK"
    val heading = "What is your latest VAT Return total?"
    val line_1 = "You can find this amount in box number 5 on your latest VAT Return submitted to HMRC."
    val line_2 = "The format of this number needs to be two decimal places, for example £123.00"
    val error = "Enter your latest VAT Return total or Box 5 amount"
  }

  object CaptureSubmittedVATReturn {
    val title = "Are you currently submitting VAT Returns? - Claim VAT Enrolment - GOV.UK"
    val heading = "Are you currently submitting VAT Returns?"
  }

}