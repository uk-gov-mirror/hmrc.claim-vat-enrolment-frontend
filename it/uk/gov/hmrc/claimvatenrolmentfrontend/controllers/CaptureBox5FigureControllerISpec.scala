
package uk.gov.hmrc.claimvatenrolmentfrontend.controllers

import play.api.test.Helpers._
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.TestConstants.testJourneyId
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.views.CaptureBox5FigureViewTests

class CaptureBox5FigureControllerISpec extends ComponentSpecHelper with CaptureBox5FigureViewTests {

  s"GET /$testJourneyId/box-5-figure" should {
    "return OK" in {
      lazy val result = get(s"/$testJourneyId/box-5-figure")

      result.status mustBe OK
    }

    "return a view" should {
      lazy val result = get(s"/$testJourneyId/box-5-figure")

      testCaptureBox5FigureViewTests(result)
    }
  }

  s"POST /$testJourneyId/box-5-figure" should {
    "redirect to CaptureLastMonthSubmitted" in {
      lazy val result = post(s"/$testJourneyId/box-5-figure")(
        "box5_figure" -> "1234.56"
      )

      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(routes.CaptureLastMonthSubmittedController.show(testJourneyId).url)
      )
    }

    "return a BAD_REQUEST if the box 5 figure is missing" in {
      lazy val result = post(s"/$testJourneyId/box-5-figure")(
        "box5_figure" -> ""
      )

      result.status mustBe BAD_REQUEST
    }

    "return a BAD_REQUEST if the box 5 figure has invalid format" in {
      lazy val result = post(s"/$testJourneyId/box-5-figure")(
        "box5_figure" -> "1234.5"
      )

      result.status mustBe BAD_REQUEST
    }

    "return a BAD_REQUEST if the box 5 figure has invalid characters" in {
      lazy val result = post(s"/$testJourneyId/box-5-figure")(
        "box5_figure" -> "1234.oo"
      )

      result.status mustBe BAD_REQUEST
    }

    "redirect to CaptureLastMonthSubmitted if the box 5 figure has a correct value " in {
      lazy val result = post(s"/$testJourneyId/box-5-figure")(
        "box5_figure" -> "-100.00"
      )

      result must have(
        httpStatus(SEE_OTHER),
        redirectUri(routes.CaptureLastMonthSubmittedController.show(testJourneyId).url)
      )
    }

    "return a BAD_REQUEST if the box 5 figure has more than 14 digits" in {
      lazy val result = post(s"/$testJourneyId/box-5-figure")(
        "box5_figure" -> "0123456789012345"
      )

      result.status mustBe BAD_REQUEST
    }

    "return a BAD_REQUEST if the box 5 figure has more a negative value" in {
      lazy val result = post(s"/$testJourneyId/box-5-figure")(
        "box5_figure" -> "-100000000000000000000.00"
      )

      result.status mustBe BAD_REQUEST
    }
  }

}

