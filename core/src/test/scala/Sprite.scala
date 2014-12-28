package pegler
package test

import java.io.ByteArrayOutputStream

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class SpriteTest extends FlatSpec with Matchers {
  def character = getClass.getResourceAsStream("/character.png")

  "Sprite" should "load from an input stream" in {
    val sprite = Sprite.load(character)
    sprite.width should be (21)
    sprite.height should be (24)
  }

  it should "load from a byte array" in {
    val bin = character
    val bos = new ByteArrayOutputStream()
    var byte = bin.read()
    while (byte > -1) {
      bos.write(byte)
      byte = bin.read()
    }
    bin.close()
    val sprite = Sprite.load(bos.toByteArray())
    bos.close()
    sprite.width should be (21)
    sprite.height should be (24)
  }

  it should "contain pixel info" in {
    val sprite = Sprite.load(character)
    sprite.info.counts.size should be (8)
    sprite.info.counts(RGBColor.transparent) should be (154)
    sprite.info.pixels.size should be (21 * 24)
    sprite.info.pixels(0) should be (Pixel(0, 0, RGBColor.transparent))
    sprite.info.rgba(0, 0) should be (RGBColor.transparent)
  }
}
