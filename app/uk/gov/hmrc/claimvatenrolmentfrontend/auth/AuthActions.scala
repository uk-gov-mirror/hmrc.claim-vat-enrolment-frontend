/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.claimvatenrolmentfrontend.auth

import play.api.mvc.Results.Redirect
import play.api.mvc._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals.{credentialRole, credentials, groupIdentifier}
import uk.gov.hmrc.auth.core.retrieve.{Credentials, ~}
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions, User}
import uk.gov.hmrc.claimvatenrolmentfrontend.controllers.errorPages
import uk.gov.hmrc.claimvatenrolmentfrontend.models.VatKnownFacts
import uk.gov.hmrc.claimvatenrolmentfrontend.repositories.JourneyDataRepository
import uk.gov.hmrc.http.{HeaderCarrier, InternalServerException}
import uk.gov.hmrc.play.http.HeaderCarrierConverter
import utils.LoggingUtil

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

case class IdentifierRequest[A](request: Request[A], journeyId: String, userId: String, credId: String, groupId: String) extends WrappedRequest[A](request)
case class DataRequest[A](request: Request[A], journeyId: String, userId: String, credId: String, groupId: String, journeyData: VatKnownFacts) extends WrappedRequest[A](request)

class AuthenticatedIdentifierAction @Inject()(override val authConnector: AuthConnector,
                                              val parser: BodyParsers.Default)
                                             (implicit val executionContext: ExecutionContext)
  extends ActionBuilder[IdentifierRequest, AnyContent]
    with ActionFunction[Request, IdentifierRequest]
    with AuthorisedFunctions
    with LoggingUtil {

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    val journeyId = request.path.split("/claim-vat-enrolment/")(1).take(36)
    authorised().retrieve(
      Retrievals.internalId and credentialRole and credentials and groupIdentifier
    ) {
      case Some(internalId) ~ Some(User) ~ Some(Credentials(credId, "GovernmentGateway")) ~ Some(groupId) =>
        block(IdentifierRequest(request, journeyId, internalId, credId, groupId))
      case _ =>
        errorLog(
          s"[AuthenticatedIdentifierAction] - Internal ID could not be retrieved from Auth for journey: $journeyId"
        )(implicitly, request)
        throw new InternalServerException(s"Internal ID could not be retrieved from Auth")
    }
  }
}

class JourneyDataRetrievalAction @Inject()(val journeyDataRepository: JourneyDataRepository)
                                         (implicit val executionContext: ExecutionContext)
  extends ActionRefiner[IdentifierRequest, DataRequest] with LoggingUtil {

  override protected def refine[A](request: IdentifierRequest[A]): Future[Either[Result, DataRequest[A]]] = {
    implicit val req: Request[A] = request.request
    journeyDataRepository.getJourneyData(request.journeyId, request.userId).flatMap {
      case Some(journeyData) =>
        Future.successful(Right(DataRequest(request.request, request.journeyId, request.userId, request.credId, request.groupId, journeyData)))
      case None =>
        errorLog(s"[JourneyDataRetrievalAction] - Journey data was not found for journey ID ${request.journeyId}")
        Future.successful(Left(Redirect(errorPages.routes.ServiceTimeoutController.show())))
    }
    .recover {
      case e: Exception =>
        errorLog(s"[JourneyDataRetrievalAction] - Error retrieving journey data for journey ID ${request.journeyId}: ${e.getMessage}")
        Left(Redirect(errorPages.routes.ServiceTimeoutController.show()))
    }
  }
}
