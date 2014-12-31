package pegler
package app
package oauth

import unfiltered.oauth2._

object PeglerAuthorization
  extends AuthorizationServer
  with PeglerClients
  with PeglerTokens
  with PeglerService

trait PeglerClients extends ClientStore {
  def client(clientId: String, secret: Option[String]) = ???
}

trait PeglerTokens extends TokenStore {
  def exchangeAuthorizationCode(codeToken: unfiltered.oauth2.Token): unfiltered.oauth2.Token = ???
  def generateAuthorizationCode(responseTypes: Seq[String],owner: unfiltered.oauth2.ResourceOwner,client: unfiltered.oauth2.Client,scope: Seq[String],redirectUri: String): String = ???
  def generateClientToken(client: unfiltered.oauth2.Client,scope: Seq[String]): unfiltered.oauth2.Token = ???
  def generateImplicitAccessToken(responseTypes: Seq[String],owner: unfiltered.oauth2.ResourceOwner,client: unfiltered.oauth2.Client,scope: Seq[String],redirectUri: String): unfiltered.oauth2.Token = ???
  def generatePasswordToken(owner: unfiltered.oauth2.ResourceOwner,client: unfiltered.oauth2.Client,scope: Seq[String]): unfiltered.oauth2.Token = ???
  def refresh(other: unfiltered.oauth2.Token): unfiltered.oauth2.Token = ???
  def refreshToken(refreshToken: String): Option[unfiltered.oauth2.Token] = ???
  def token(code: String): Option[unfiltered.oauth2.Token] = ???
}

trait PeglerService extends Service {
  def accepted[T](r: unfiltered.request.HttpRequest[T]): Boolean = ???
  def denied[T](r: unfiltered.request.HttpRequest[T]): Boolean = ???
  def errorUri(error: String): Option[String] = ???
  def resourceOwner(userName: String,password: String): Option[unfiltered.oauth2.ResourceOwner] = ???
  def resourceOwner[T](r: unfiltered.request.HttpRequest[T]): Option[unfiltered.oauth2.ResourceOwner] = ???
  def validScopes[T](resourceOwner: unfiltered.oauth2.ResourceOwner,scopes: Seq[String],req: unfiltered.request.HttpRequest[T]): Boolean = ???
  def validScopes(scopes: Seq[String]): Boolean = ???
  def invalidClient[T](req: unfiltered.request.HttpRequest[T]): unfiltered.response.ResponseFunction[Any] = ???
  def invalidRedirectUri[T](req: unfiltered.request.HttpRequest[T],uri: Option[String],client: Option[unfiltered.oauth2.Client]): unfiltered.response.ResponseFunction[Any] = ???
  def login[T](requestBundle: unfiltered.oauth2.RequestBundle[T]): unfiltered.response.ResponseFunction[Any] = ???
  def requestAuthorization[T](requestBundle: unfiltered.oauth2.RequestBundle[T]): unfiltered.response.ResponseFunction[Any] = ???
}
