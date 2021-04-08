package uk.gov.hmrc.claimvatenrolmentfrontend.views.errorPages

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.claimvatenrolmentfrontend.assets.MessageLookup.{Base, BetaBanner, InvalidAccountType => messages}
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.{ComponentSpecHelper, ViewSpecHelper}

trait InvalidAccountTypeViewTests extends ViewSpecHelper {
  this: ComponentSpecHelper =>

  def testInvalidAccountTypeView(result: => WSResponse): Unit = {

    lazy val doc: Document = {
      Jsoup.parse(result.body)
    }

    "have a view with the correct title" in {
      doc.title mustBe messages.title
    }

    "have the correct beta banner" in {
      doc.getBanner.text mustBe BetaBanner.title
    }

    "have the correct heading" in {
      doc.getH1Elements.first.text mustBe messages.heading
    }

    "have the correct text" in {
      doc.getParagraphs.get(2).text mustBe messages.line_1
    }

    "have a sign out button" in {
      doc.getSubmitButton.first.text mustBe Base.signOut
    }
  }
}
