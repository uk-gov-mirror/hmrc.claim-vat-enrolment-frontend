
package uk.gov.hmrc.claimvatenrolmentfrontend.controllers

import play.api.test.Helpers._
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.TestConstants.testJourneyId
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.KnownFactsMismatchViewTest

class KnownFactsMismatchControllerISpec extends ComponentSpecHelper with KnownFactsMismatchViewTest {

  s"GET /$testJourneyId/can-not-confirm-business" should {
    "return OK" in {
      lazy val result = get(s"/$testJourneyId/can-not-confirm-business")

      result.status mustBe OK
    }

    "return a view" should {
      lazy val result = get(s"/$testJourneyId/can-not-confirm-business")

      testKnownFactsMismatchViewTest(result)
    }
  }

}

