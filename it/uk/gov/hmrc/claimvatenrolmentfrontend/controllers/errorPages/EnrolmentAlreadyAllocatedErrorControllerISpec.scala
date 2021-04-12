
package uk.gov.hmrc.claimvatenrolmentfrontend.controllers.errorPages

import play.api.test.Helpers._
import uk.gov.hmrc.claimvatenrolmentfrontend.stubs.AuthStub
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.errorPages.{EnrolmentAlreadyAllocatedErrorViewTest}

class EnrolmentAlreadyAllocatedErrorControllerISpec extends ComponentSpecHelper with EnrolmentAlreadyAllocatedErrorViewTest with AuthStub {

  s"GET /can-not-use-this-service-3" should {
    lazy val result = {
      get("/can-not-use-this-service-3")
    }
    "return OK" in {
      result.status mustBe OK
    }
    testEnrolmentAlreadyAllocatedErrorViewTest(result)
  }

}
