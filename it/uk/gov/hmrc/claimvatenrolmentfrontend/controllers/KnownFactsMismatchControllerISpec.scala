
package uk.gov.hmrc.claimvatenrolmentfrontend.controllers

import play.api.test.Helpers._
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.TestConstants.{testInternalId, testJourneyId}
import uk.gov.hmrc.claimvatenrolmentfrontend.stubs.AuthStub
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.KnownFactsMismatchViewTest

class KnownFactsMismatchControllerISpec extends ComponentSpecHelper with KnownFactsMismatchViewTest with AuthStub {

  s"GET /$testJourneyId/can-not-confirm-business" should {
    lazy val result = {
      stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
      get(s"/$testJourneyId/can-not-confirm-business")
    }
    "return OK" in {
      result.status mustBe OK
    }
    testKnownFactsMismatchViewTest(result)
  }


}

