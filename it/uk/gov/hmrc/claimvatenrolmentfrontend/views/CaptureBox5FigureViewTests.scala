
package uk.gov.hmrc.claimvatenrolmentfrontend.views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.MessageLookup.{Base, BetaBanner, Header, CaptureBox5Figure => messages}
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ComponentSpecHelper
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.ViewSpecHelper.ElementExtensions

trait CaptureBox5FigureViewTests {
  this: ComponentSpecHelper =>

  def testCaptureBox5FigureViewTests(result: => WSResponse): Unit = {

    lazy val doc: Document = {
      Jsoup.parse(result.body)
    }

    "have a sign out link in the header" in {
      doc.getSignOutText mustBe Header.signOut
    }
    "have the correct beta banner" in {
      doc.getBanner.text mustBe BetaBanner.title
    }

    "have a view with the correct title" in {
      doc.title mustBe messages.title
    }

    "have the correct heading" in {
      doc.getH1Elements.first.text mustBe messages.heading
    }

    "have the correct text" in {
      doc.getParagraphs.get(2).text mustBe messages.line_1
    }

    "have the correct paragraph2" in {
      doc.getParagraphs.get(3).text mustBe messages.line_2
    }

    "have a continue button" in {
      doc.getSubmitButton.first.text mustBe Base.continue
    }
  }

}


