import java.awt.Color

package object pegler {
  object RGBColor {
    val hex = "#%02x%02x%02x"
    val rgba = "rgba(%d, %d, %d, %d)"
    def transparent = new Color(0, 0 ,0, 0)
  }

  implicit class RGBColor(c: Color) {
    def transparent = c.getAlpha == 0
    def hex = RGBColor.hex.format(c.getRed, c.getGreen, c.getBlue)
    def rgba = RGBColor.rgba.format(c.getRed, c.getGreen, c.getBlue, c.getAlpha)
  }
}
