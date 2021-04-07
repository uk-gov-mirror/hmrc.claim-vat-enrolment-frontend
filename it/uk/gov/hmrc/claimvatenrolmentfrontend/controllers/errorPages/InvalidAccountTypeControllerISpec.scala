
package uk.gov.hmrc.claimvatenrolmentfrontend.controllers.errorPages

import play.api.test.Helpers._
import uk.gov.hmrc.claimvatenrolmentfrontend.stubs.AuthStub
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.errorPages.InvalidAccountTypeViewTests

class InvalidAccountTypeControllerISpec extends ComponentSpecHelper with InvalidAccountTypeViewTests with AuthStub {

  s"GET /can-not-use-this-service" should {
    lazy val result = get("/can-not-use-this-service")

    "return OK" in {
      result.status mustBe OK
    }

    testInvalidAccountTypeView(result)
  }

}
