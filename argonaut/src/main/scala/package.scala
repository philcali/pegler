package pegler
package json

import argonaut._, Argonaut._

package object encode {
  implicit def SpriteLikeEncodeJson: EncodeJson[SpriteLike] =
    EncodeJson((sprite: SpriteLike) =>
      ("owner" := sprite.owner) ->:
      ("name" := sprite.name) ->:
      ("modified" := sprite.modified.getTime) ->: jEmptyObject)

  implicit def PixelEncodeJson: EncodeJson[Pixel] =
    EncodeJson((pixel: Pixel) =>
      ("x" := pixel.x) ->:
      ("y" := pixel.y) ->:
      ("color" := pixel.color.rgba) ->: jEmptyObject)

  implicit def SpriteInfoEncodeJson: EncodeJson[SpriteInfo] =
    EncodeJson((info: SpriteInfo) =>
      ("counts" := info.counts.map({ case (color, n) => color.rgba -> n })) ->:
      ("pixels" := info.pixels) ->: jEmptyObject)

  implicit def SpriteEncodeJson: EncodeJson[Sprite] =
    EncodeJson((sprite: Sprite) =>
      ("width" := sprite.width) ->:
      ("height" := sprite.height) ->:
      ("info" := sprite.info) ->: jEmptyObject)

  implicit class ArgonautSprite(sprite: Sprite) {
    def asJson = SpriteEncodeJson(sprite)
  }
}
