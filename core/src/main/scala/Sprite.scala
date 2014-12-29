package pegler

import java.awt.Color
import java.awt.Transparency
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.BufferedInputStream
import java.io.InputStream
import javax.imageio.ImageIO

object Sprite {
  def load(in: InputStream) = try {
    new Sprite(ImageIO.read(new BufferedInputStream(in)))
  } finally {
    in.close
  }
  def load(bytes: Array[Byte]): Sprite = load(new ByteArrayInputStream(bytes))
}

class Sprite(image: BufferedImage) {
  def height = image.getHeight
  def width = image.getWidth
  lazy val info = new SpriteInfo(image)
}

class SpriteInfo(image: BufferedImage) {
  private val colorMap = collection.mutable.Map.empty[Color, Int]
  private val pointBuffer = collection.mutable.ListBuffer.empty[Pixel]
  private def color(rgb: Int) = {
    val c = new Color(rgb, image.getTransparency > Transparency.OPAQUE)
    if (c.getAlpha == 0) {
      RGBColor.transparent
    } else {
      c
    }
  }

  for {
    x <- (0 until image.getWidth)
    y <- (0 until image.getHeight)
  } {
    val pixel = Pixel(x, y, rgba(x, y))
    pointBuffer += pixel
    colorMap.get(pixel.color).orElse(Some(0)).foreach {
      case n => colorMap += pixel.color -> (n + 1)
    }
  }

  def rgba(x: Int, y: Int) = color(image.getRGB(x, y))
  lazy val pixels = pointBuffer.toList
  lazy val counts = colorMap.toMap
}

case class Pixel(x: Int, y: Int, color: Color)
