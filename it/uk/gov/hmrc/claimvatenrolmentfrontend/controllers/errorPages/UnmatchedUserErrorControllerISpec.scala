
package uk.gov.hmrc.claimvatenrolmentfrontend.controllers.errorPages

import play.api.test.Helpers._
import uk.gov.hmrc.claimvatenrolmentfrontend.stubs.AuthStub
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.errorPages.UnmatchedUserErrorViewTest

class UnmatchedUserErrorControllerISpec extends ComponentSpecHelper with UnmatchedUserErrorViewTest with AuthStub {

  s"GET /can-not-use-this-service-2" should {
    lazy val result = {
      get("/can-not-use-this-service-2")
    }
    "return OK" in {
      result.status mustBe OK
    }
    testUnmatchedUserErrorViewTest(result)
  }

}
