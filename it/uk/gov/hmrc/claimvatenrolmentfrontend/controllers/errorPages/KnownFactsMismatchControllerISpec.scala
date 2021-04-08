
package uk.gov.hmrc.claimvatenrolmentfrontend.controllers.errorPages

import play.api.test.Helpers._
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.TestConstants.testInternalId
import uk.gov.hmrc.claimvatenrolmentfrontend.stubs.AuthStub
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.errorPages.KnownFactsMismatchViewTests

class KnownFactsMismatchControllerISpec extends ComponentSpecHelper with KnownFactsMismatchViewTests with AuthStub {

  s"GET /can-not-confirm-business" should {
    lazy val result = {
      stubAuth(OK, successfulAuthResponse(Some(testInternalId)))
      get("/can-not-confirm-business")
    }

    "return OK" in {
      result.status mustBe OK
    }

    testKnownFactsMismatchView(result)
  }

}
