package pegler
package json
package encode
package test

import argonaut._, Argonaut._
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ArgonautTest extends FlatSpec with Matchers {
  def character = getClass.getResourceAsStream("/character.png")

  val pointZeroZeroColorLens = jObjectPL >=>
    jsonObjectPL("info") >=>
    jObjectPL >=>
    jsonObjectPL("pixels") >=>
    jArrayPL >=>
    jsonArrayPL(0) >=>
    jObjectPL >=>
    jsonObjectPL("color") >=>
    jStringPL

  val widthLens = jObjectPL >=>
    jsonObjectPL("width") >=>
    jNumberPL

  val heightLens = jObjectPL >=>
    jsonObjectPL("height") >=>
    jNumberPL

  "Argonaut package" should "provide encoders implicitly" in {
    val sprite = Sprite.load(character)
    val json = sprite.asJson
    val transparent = RGBColor.transparent
    pointZeroZeroColorLens.get(json) should be (Some(transparent.rgba))
    widthLens.get(json) should be (Some(21))
    heightLens.get(json) should be (Some(24))
  }
}
