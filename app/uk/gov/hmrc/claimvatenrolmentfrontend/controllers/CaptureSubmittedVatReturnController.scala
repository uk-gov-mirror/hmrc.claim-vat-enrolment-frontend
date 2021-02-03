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
import uk.gov.hmrc.claimvatenrolmentfrontend.config.AppConfig
import uk.gov.hmrc.claimvatenrolmentfrontend.forms.CaptureSubmittedVatReturnForm
import uk.gov.hmrc.claimvatenrolmentfrontend.views.html.capture_submitted_vat_return_page
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.Inject
import scala.concurrent.Future

class CaptureSubmittedVatReturnController @Inject()(mcc: MessagesControllerComponents,
                                                    view: capture_submitted_vat_return_page
                                                   )(implicit val config: AppConfig) extends FrontendController(mcc) {

  val show: Action[AnyContent] = Action.async {
    implicit request =>
      Future.successful(Ok(view(routes.CaptureSubmittedVatReturnController.submit(), CaptureSubmittedVatReturnForm.form)))
  }

  val submit: Action[AnyContent] = Action.async {
    implicit request =>
      Future.successful(Redirect(routes.CaptureBox5FigureController.show().url))
  }

}
