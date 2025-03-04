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

package uk.gov.hmrc.claimvatenrolmentfrontend.models

case class Postcode(stringValue: String) {
  import Postcode._

  val sanitisedPostcode: String = stringValue.toUpperCase.filterNot(_.isWhitespace)

  val checkYourAnswersFormat: String = sanitisedPostcode match {
    case standardPostcodeFormat(outwardCode, inwardCode) => outwardCode + " " + inwardCode
    case other => other // should never happen as it is validated in the form
  }

}

object Postcode {
  private[models] val standardPostcodeFormat = "([A-Z]{1,2}[0-9][0-9A-Z]?)([0-9][A-Z]{2})".r
}
