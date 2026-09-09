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

package uk.gov.hmrc.claimvatenrolmentfrontend.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.mvc.Request
import play.api.test.FakeRequest
import uk.gov.hmrc.claimvatenrolmentfrontend.config.AppConfig
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.language.{Cy, En}
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.userresearchbanner.UserResearchBanner
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContextExecutor

class UrBannerUtilSpec extends AnyWordSpec with GuiceOneAppPerSuite with Matchers {

  implicit val hc: HeaderCarrier    = HeaderCarrier()
  implicit val request: Request[_]  = FakeRequest()
  implicit val appConfig: AppConfig = app.injector.instanceOf[AppConfig]
  implicit val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]
  implicit val messages: Messages = messagesApi.preferred(request)

  implicit val ec: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  "getUrBanner" when {

    "the language is English" should {

      "return a UserResearchBanner with En language and the English URL" in {
        UrBannerUtil.getUrBanner() shouldBe UserResearchBanner(
          language = En,
          url = appConfig.urBannerBaseUrl,
          hideCloseButton = true
        )
      }
    }

    "the language is Welsh" should {

      "return a UserResearchBanner with Cy language and the Welsh URL" in {
        val welshMessages = messagesApi.preferred(Seq(Lang("cy")))
        UrBannerUtil.getUrBanner()(appConfig, welshMessages) shouldBe UserResearchBanner(
          language = Cy,
          url = s"${appConfig.urBannerBaseUrl}&Q_Language=CY",
          hideCloseButton = true
        )
      }
    }

    "hideCloseButton is false" should {

      "return a UserResearchBanner with hideCloseButton set to false" in {
        val result = UrBannerUtil.getUrBanner(hideCloseButton = false)
        result.hideCloseButton shouldBe false
      }
    }
  }
}

