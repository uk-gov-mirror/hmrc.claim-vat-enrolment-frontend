package uk.gov.hmrc.claimvatenrolmentfrontend.stubs

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.HeaderNames
import play.api.libs.json.{JsObject, Json, Writes}
import play.api.test.Helpers.UNAUTHORIZED
import uk.gov.hmrc.claimvatenrolmentfrontend.utils.WireMockMethods


trait AuthStub extends WireMockMethods {

  val authUrl = "/auth/authorise"

  def stubAuth[T](status: Int, body: T)(implicit writes: Writes[T]): StubMapping = {
    when(method = POST, uri = authUrl)
      .thenReturn(status = status, body = writes.writes(body))
  }

  def stubAuthFailure(): StubMapping = {
    when(method = POST, uri = authUrl)
      .thenReturn(status = UNAUTHORIZED, headers = Map(HeaderNames.WWW_AUTHENTICATE -> s"""MDTP detail="MissingBearerToken""""))
  }

  def successfulAuthResponse(internalId: Option[String]): JsObject = Json.obj(
    "internalId" -> internalId
  )

}
