package pegler
package app
package oauth

import unfiltered.oauth2._
import unfiltered.request._

object PeglerAuthentication extends AuthSource {
  def authenticateToken[T](token: AccessToken, request: HttpRequest[T]) = ???
}
