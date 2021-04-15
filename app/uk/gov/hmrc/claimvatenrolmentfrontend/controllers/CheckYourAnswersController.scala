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

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals.{credentials, groupIdentifier, internalId}
import uk.gov.hmrc.auth.core.retrieve.{Credentials, ~}
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions}
import uk.gov.hmrc.claimvatenrolmentfrontend.config.AppConfig
import uk.gov.hmrc.claimvatenrolmentfrontend.controllers.errorPages.{routes => errorRoutes}
import uk.gov.hmrc.claimvatenrolmentfrontend.httpparsers.QueryUsersHttpParser.{NoUsersFound, UsersFound}
import uk.gov.hmrc.claimvatenrolmentfrontend.models.{EnrolmentFailure, EnrolmentSuccess, InvalidKnownFacts, MultipleEnrolmentsInvalid}
import uk.gov.hmrc.claimvatenrolmentfrontend.services.{AllocateEnrolmentService, JourneyService}
import uk.gov.hmrc.claimvatenrolmentfrontend.views.html.check_your_answers_page
import uk.gov.hmrc.http.InternalServerException
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CheckYourAnswersController @Inject()(mcc: MessagesControllerComponents,
                                           view: check_your_answers_page,
                                           journeyService: JourneyService,
                                           allocateEnrolmentService: AllocateEnrolmentService,
                                           val authConnector: AuthConnector
                                          )(implicit appConfig: AppConfig, ec: ExecutionContext) extends FrontendController(mcc) with AuthorisedFunctions {

  def show(journeyId: String): Action[AnyContent] = Action.async {
    implicit request =>
      authorised().retrieve(internalId) {
        case Some(authInternalId) =>
          journeyService.retrieveJourneyData(journeyId, authInternalId).map {
            journeyData =>
              Ok(view(routes.CheckYourAnswersController.submit(journeyId), journeyId, journeyData))
          }
        case None => Future.successful(Unauthorized)
      }
  }

  def submit(journeyId: String): Action[AnyContent] = Action.async {
    implicit request =>
      authorised().retrieve(credentials and groupIdentifier and internalId) {
        case Some(Credentials(credentialId, "GovernmentGateway")) ~ Some(groupId) ~ Some(internalId) =>
          journeyService.retrieveJourneyData(journeyId, internalId).flatMap {
            journeyData =>
              allocateEnrolmentService.allocateEnrolment(journeyData, credentialId, groupId).flatMap {
                case EnrolmentSuccess =>
                  journeyService.retrieveJourneyConfig(journeyId).map {
                    journeyConfig => SeeOther(journeyConfig.continueUrl)
                  }
                case MultipleEnrolmentsInvalid =>
                  Future.successful(Redirect(errorRoutes.UnmatchedUserErrorController.show()))
                case InvalidKnownFacts =>
                  Future.successful(Redirect(errorRoutes.KnownFactsMismatchController.show().url))
                case EnrolmentFailure(_) =>
                  allocateEnrolmentService.getUserIds(journeyData.vatNumber).map {
                    case UsersFound =>
                      Redirect(errorRoutes.EnrolmentAlreadyAllocatedController.show().url)
                    case NoUsersFound =>
                      throw new InternalServerException("no users found with specified enrolment")
                  }
              }
          }
        case _ =>
          Future.successful(Unauthorized)
      }
  }

}
