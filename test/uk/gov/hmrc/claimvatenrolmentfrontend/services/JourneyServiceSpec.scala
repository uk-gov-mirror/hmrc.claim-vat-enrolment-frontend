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

package uk.gov.hmrc.claimvatenrolmentfrontend.services

import org.mockito.Mockito.when
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import reactivemongo.api.commands.WriteResult
import uk.gov.hmrc.claimvatenrolmentfrontend.helpers.TestConstants._
import uk.gov.hmrc.claimvatenrolmentfrontend.models.JourneyConfig
import uk.gov.hmrc.claimvatenrolmentfrontend.repositories.mocks.{MockJourneyConfigRepository, MockJourneyDataRepository}
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.UnitSpec
import uk.gov.hmrc.http.{HeaderCarrier, InternalServerException}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class JourneyServiceSpec extends UnitSpec with MockJourneyConfigRepository with MockJourneyDataRepository {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  val mockJourneyIdGenerationService: JourneyIdGenerationService = mock[JourneyIdGenerationService]

  object TestService extends JourneyService(mockJourneyConfigRepository, mockJourneyDataRepository, mockJourneyIdGenerationService)

  val testJourneyConfig: JourneyConfig = JourneyConfig(
    continueUrl = testContinueUrl
  )

  "createJourney" should {
    "return the journeyId and store the Journey Config" in {
      when(mockJourneyIdGenerationService.generateJourneyId()).thenReturn(testJourneyId)
      mockInsertJourneyConfig(testJourneyId, testJourneyConfig)(response = Future.successful(mock[WriteResult]))
      mockInsertJourneyData(testJourneyId, testInternalId, testVatNumber)(response = Future.successful(testJourneyId))

      val result = await(TestService.createJourney(testJourneyConfig, testVatNumber, testInternalId))

      result mustBe testJourneyId
      verifyInsertJourneyConfig(testJourneyId, testJourneyConfig)
    }
  }

  "retrieveJourneyConfig" should {
    "return the Journey Config" in {
      mockFindById(testJourneyId)(Future.successful(Some(testJourneyConfig)))

      val result = await(TestService.retrieveJourneyConfig(testJourneyId))

      result mustBe testJourneyConfig
      verifyFindById(testJourneyId)
    }

    "throw an Internal Server Exception" when {
      "the journey config does not exist in the database" in {
        mockFindById(testJourneyId)(Future.successful(None))

        intercept[InternalServerException] {
          await(TestService.retrieveJourneyConfig(testJourneyId))
        }
        verifyFindById(testJourneyId)
      }
    }
  }

  "retrieveJourneyData" should {
    "return the full Journey Data" in {
      mockGetJourneyData(testJourneyId, testInternalId)(Future.successful(Some(testFullClaimVatEnrolmentModel)))

      val result = await(TestService.retrieveJourneyData(testJourneyId, testInternalId))

      result mustBe testFullClaimVatEnrolmentModel
      verifyGetJourneyData(testJourneyId, testInternalId)
    }

    "throw an Internal Server Exception" when {
      "the journey data does not exist in the database" in {
        mockGetJourneyData(testJourneyId, testInternalId)(Future.successful(None))

        intercept[InternalServerException] {
          await(TestService.retrieveJourneyData(testJourneyId, testInternalId))
          verifyGetJourneyData(testJourneyId, testInternalId)
        }
      }
    }
  }

}
