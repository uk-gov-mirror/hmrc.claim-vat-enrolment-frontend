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

import play.api.http.Status._
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

sealed trait AllocateEnrolmentResponse

case object EnrolmentSuccess extends AllocateEnrolmentResponse

case class EnrolmentFailure(errorMessage: String) extends AllocateEnrolmentResponse

case object MultipleEnrolmentsInvalid extends AllocateEnrolmentResponse

case object InvalidKnownFacts extends AllocateEnrolmentResponse

object AllocateEnrolmentResponseHttpParser {

  val CodeKey = "code"
  val MultipleEnrolmentsInvalidKey = "MULTIPLE_ENROLMENTS_INVALID"

  implicit object AllocateEnrolmentResponseReads extends HttpReads[AllocateEnrolmentResponse] {
    override def read(method: String, url: String, response: HttpResponse): AllocateEnrolmentResponse = {

      def responseCode: Seq[String] = (response.json \\ CodeKey).map(_.as[String])

      response.status match {
        case CREATED => EnrolmentSuccess
        case CONFLICT if responseCode contains MultipleEnrolmentsInvalidKey => MultipleEnrolmentsInvalid
        case BAD_REQUEST => InvalidKnownFacts
        case _ => EnrolmentFailure(response.body)
      }
    }
  }

}