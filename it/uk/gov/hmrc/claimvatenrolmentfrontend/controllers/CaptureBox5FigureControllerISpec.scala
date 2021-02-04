
package uk.gov.hmrc.claimvatenrolmentfrontend.controllers

import play.api.test.Helpers._
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.CaptureBox5FigureViewTests

class CaptureBox5FigureControllerISpec extends ComponentSpecHelper with CaptureBox5FigureViewTests {

  "GET /box-5-figure" should {
    "return OK" in {
      lazy val result = get("/box-5-figure")

      result.status mustBe OK
    }

    "return a view" should {
      lazy val result = get("/box-5-figure")

      testCaptureBox5FigureViewTests(result)
    }
  }

  "POST /box-5-figure" should {
    "redirect to CaptureLastMonthSubmitted" in {
      lazy val result = post("/box-5-figure")()

      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(routes.CaptureLastMonthSubmittedController.show().url)
      )
    }
  }

}

