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

import play.api.http.Status.{CONFLICT, CREATED}
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

sealed trait AllocateEnrolmentResponse

case object EnrolmentSuccess extends AllocateEnrolmentResponse

case class EnrolmentFailure(errorMessage: String) extends AllocateEnrolmentResponse

case class MultipleEnrolmentsInvalid(errorMessage: String) extends AllocateEnrolmentResponse

object AllocateEnrolmentResponseHttpParser {

  implicit object AllocateEnrolmentResponseReads extends HttpReads[AllocateEnrolmentResponse] {
    override def read(method: String, url: String, response: HttpResponse): AllocateEnrolmentResponse =
      response.status match {
        case CREATED => EnrolmentSuccess
        case CONFLICT => MultipleEnrolmentsInvalid(response.body)
        case _ => EnrolmentFailure(response.body)
      }
  }

}