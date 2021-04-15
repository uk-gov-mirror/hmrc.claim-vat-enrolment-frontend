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

import java.time.Instant

import play.api.libs.json.Json
import play.api.test.Helpers._
import reactivemongo.play.json._
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.TestConstants._
import uk.gov.hmrc.claimvatenrolmentfrontend.controllers.errorPages.{routes => errorRoutes}
import uk.gov.hmrc.claimvatenrolmentfrontend.models.AllocateEnrolmentResponseHttpParser.MultipleEnrolmentsInvalidKey
import uk.gov.hmrc.claimvatenrolmentfrontend.repositories.JourneyDataRepository.claimVatEnrolmentModelWrites
import uk.gov.hmrc.claimvatenrolmentfrontend.stubs.{AllocationEnrolmentStub, AuthStub, EnrolmentStoreProxyStub}
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.CheckYourAnswersViewTests

import scala.concurrent.ExecutionContext.Implicits.global


class CheckYourAnswersControllerISpec extends ComponentSpecHelper with CheckYourAnswersViewTests with AuthStub with AllocationEnrolmentStub with EnrolmentStoreProxyStub {

  override def beforeEach(): Unit = {
    await(journeyDataRepository.drop)
    super.beforeEach()
  }

  s"GET /$testJourneyId/check-your-answers-vat" when {
    "there is a full ClaimVatEnrolmentModel stored in the database" should {
      lazy val result = {
        await(journeyDataRepository.collection.insert(true).one(
          Json.obj(
            "_id" -> testJourneyId,
            "authInternalId" -> testInternalId,
            "creationTimestamp" -> Json.obj("$date" -> Instant.now.toEpochMilli)
          ) ++ Json.toJsObject(testFullClaimVatEnrolmentModel)
        ))
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        get(s"/$testJourneyId/check-your-answers-vat")
      }
      "return OK" in {
        result.status mustBe OK
      }
      testCheckYourAnswersViewFull(result)
    }

    "there is a ClaimVatEnrolmentModel with no postcode stored in the database" should {
      lazy val result = {
        await(journeyDataRepository.collection.insert(true).one(
          Json.obj(
            "_id" -> testJourneyId,
            "authInternalId" -> testInternalId,
            "creationTimestamp" -> Json.obj("$date" -> Instant.now.toEpochMilli)
          ) ++ Json.toJsObject(testClaimVatEnrolmentModelNoPostcode)
        ))
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        get(s"/$testJourneyId/check-your-answers-vat")
      }

      "return OK" in {
        result.status mustBe OK
      }

      testCheckYourAnswersViewNoPostcode(result)
    }

    "there is a ClaimVatEnrolmentModel with no returns stored in the database" should {
      lazy val result = {
        await(journeyDataRepository.collection.insert(true).one(
          Json.obj(
            "_id" -> testJourneyId,
            "authInternalId" -> testInternalId,
            "creationTimestamp" -> Json.obj("$date" -> Instant.now.toEpochMilli)
          ) ++ Json.toJsObject(testClaimVatEnrolmentModelNoReturns)
        ))
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        get(s"/$testJourneyId/check-your-answers-vat")
      }

      "return OK" in {
        result.status mustBe OK
      }

      testCheckYourAnswersViewNoReturnsInformation(result)
    }

    "there is a ClaimVatEnrolmentModel with no returns and no postcode stored in the database" should {
      lazy val result = {
        await(journeyDataRepository.collection.insert(true).one(
          Json.obj(
            "_id" -> testJourneyId,
            "authInternalId" -> testInternalId,
            "creationTimestamp" -> Json.obj("$date" -> Instant.now.toEpochMilli)
          ) ++ Json.toJsObject(testClaimVatEnrolmentModelNoReturnsNoPostcode)
        ))
        stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
        get(s"/$testJourneyId/check-your-answers-vat")
      }

      "return OK" in {
        result.status mustBe OK
      }

      testCheckYourAnswersViewNoReturnsNoPostcode(result)
    }
  }

  s"POST /$testJourneyId/check-your-answers-vat" should {
    "redirect to the continue url when the allocation was successfully created" in {
      stubAuth(OK, successfulAuthResponse(Some(testGroupId), Some(testInternalId)))
      await(journeyDataRepository.collection.insert(true).one(
        Json.obj(
          "_id" -> testJourneyId,
          "authInternalId" -> testInternalId,
          "creationTimestamp" -> Json.obj("$date" -> Instant.now.toEpochMilli)
        ) ++ Json.toJsObject(testFullClaimVatEnrolmentModel)
      ))
      await(insertJourneyConfig(testJourneyId, testContinueUrl))
      stubAllocateEnrolment(testFullClaimVatEnrolmentModel, testCredentialId, testGroupId)(CREATED, Json.obj())

      lazy val result = post(s"/$testJourneyId/check-your-answers-vat")()

      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(testContinueUrl)
      )
    }

    "redirect to UnmatchedUser if the user group already has a matching enrolment, but the user does not" in {
      stubAuth(OK, successfulAuthResponse(Some(testGroupId), Some(testInternalId)))
      await(journeyDataRepository.collection.insert(true).one(
        Json.obj(
          "_id" -> testJourneyId,
          "authInternalId" -> testInternalId,
          "creationTimestamp" -> Json.obj("$date" -> Instant.now.toEpochMilli)
        ) ++ Json.toJsObject(testFullClaimVatEnrolmentModel)
      ))
      await(insertJourneyConfig(testJourneyId, testContinueUrl))
      stubAllocateEnrolment(testFullClaimVatEnrolmentModel, testCredentialId, testGroupId)(CONFLICT, Json.obj("code" -> MultipleEnrolmentsInvalidKey))

      lazy val result = post(s"/$testJourneyId/check-your-answers-vat")()

      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(errorRoutes.UnmatchedUserErrorController.show().url)
      )
    }

    "redirect to KnownFactsMismatch if the enrolment fails" in {
      stubAuth(OK, successfulAuthResponse(Some(testGroupId), Some(testInternalId)))
      await(journeyDataRepository.collection.insert(true).one(
        Json.obj(
          "_id" -> testJourneyId,
          "authInternalId" -> testInternalId,
          "creationTimestamp" -> Json.obj("$date" -> Instant.now.toEpochMilli)
        ) ++ Json.toJsObject(testFullClaimVatEnrolmentModel)
      ))
      await(insertJourneyConfig(testJourneyId, testContinueUrl))
      stubAllocateEnrolment(testFullClaimVatEnrolmentModel, testCredentialId, testGroupId)(BAD_REQUEST, Json.obj())

      lazy val result = post(s"/$testJourneyId/check-your-answers-vat")()


      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(errorRoutes.KnownFactsMismatchController.show().url)
      )
    }

    "redirect to EnrolmentAlreadyAllocated error page when enrolment fails " in {
      stubAuth(OK, successfulAuthResponse(Some(testGroupId), Some(testInternalId)))
      await(journeyDataRepository.collection.insert(true).one(
        Json.obj(
          "_id" -> testJourneyId,
          "authInternalId" -> testInternalId,
          "creationTimestamp" -> Json.obj("$date" -> Instant.now.toEpochMilli)
        ) ++ Json.toJsObject(testFullClaimVatEnrolmentModel)
      ))
      await(insertJourneyConfig(testJourneyId, testContinueUrl))
      stubAllocateEnrolment(testFullClaimVatEnrolmentModel, testCredentialId, testGroupId)(INTERNAL_SERVER_ERROR, Json.obj())
      stubGetUserIds(testVatNumber)(OK)


      lazy val result = post(s"/$testJourneyId/check-your-answers-vat")()

      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(errorRoutes.EnrolmentAlreadyAllocatedController.show().url)
      )
    }

    "throw an exception when no userIds are connected with the vatNumber" in {
      stubAuth(OK, successfulAuthResponse(Some(testGroupId), Some(testInternalId)))
      await(journeyDataRepository.collection.insert(true).one(
        Json.obj(
          "_id" -> testJourneyId,
          "authInternalId" -> testInternalId,
          "creationTimestamp" -> Json.obj("$date" -> Instant.now.toEpochMilli)
        ) ++ Json.toJsObject(testFullClaimVatEnrolmentModel)
      ))
      await(insertJourneyConfig(testJourneyId, testContinueUrl))
      stubAllocateEnrolment(testFullClaimVatEnrolmentModel, testCredentialId, testGroupId)(INTERNAL_SERVER_ERROR, Json.obj())
      stubGetUserIds(testVatNumber)(NO_CONTENT)

      lazy val result = post(s"/$testJourneyId/check-your-answers-vat")()

      result must have(
        httpStatus(INTERNAL_SERVER_ERROR)
      )
    }


    "return UNAUTHORISED when no credentials or groupId are retrieved from Auth" in {
      stubAuth(OK, successfulAuthResponse(Some(testInternalId)))

      lazy val result = post(s"/$testJourneyId/check-your-answers-vat")()

      result.status mustBe UNAUTHORIZED
    }

  }

}
