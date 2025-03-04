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

import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals.internalId
import uk.gov.hmrc.auth.core.{AuthConnector, AuthorisedFunctions}
import uk.gov.hmrc.claimvatenrolmentfrontend.config.AppConfig
import uk.gov.hmrc.claimvatenrolmentfrontend.forms.VatRegistrationDateForm.vatRegistrationDateForm
import uk.gov.hmrc.claimvatenrolmentfrontend.services.StoreVatRegistrationDateService
import uk.gov.hmrc.claimvatenrolmentfrontend.views.html.capture_vat_registration_date_page
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CaptureVatRegistrationDateController @Inject()(mcc: MessagesControllerComponents,
                                                     view: capture_vat_registration_date_page,
                                                     storeVatRegistrationDateService: StoreVatRegistrationDateService,
                                                     val authConnector: AuthConnector
                                                    )(implicit ec: ExecutionContext,
                                                      appConfig: AppConfig) extends FrontendController(mcc) with AuthorisedFunctions {

  def show(journeyId: String): Action[AnyContent] = Action.async {
    implicit request =>
      authorised() {
        Future.successful(
          Ok(view(vatRegistrationDateForm, routes.CaptureVatRegistrationDateController.submit(journeyId)))
        )
      }
  }


  def submit(journeyId: String): Action[AnyContent] = Action.async {
    implicit request =>
      authorised().retrieve(internalId) {
        case Some(authId) =>
          vatRegistrationDateForm.bindFromRequest.fold(
            formWithErrors =>
              Future.successful(
                BadRequest(view(formWithErrors, routes.CaptureVatRegistrationDateController.submit(journeyId)))
              ),
            vatRegistrationDate =>
              storeVatRegistrationDateService.storeVatRegistrationDate(
                journeyId,
                vatRegistrationDate,
                authId
              ).map {
                _ => Redirect(routes.CaptureBusinessPostcodeController.show(journeyId).url)
              }
          )
        case None =>
          Future.successful(Unauthorized)
      }
  }

}
