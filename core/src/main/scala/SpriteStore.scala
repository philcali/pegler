package pegler

import java.util.Date

// Sprite Model CRUD operations
trait SpriteStore {
  // CU
  def save(owner: String, name: String, bytes: Array[Byte]): SpriteLike
  // R
  def readOwner(owner: String): List[SpriteLike]
  def readByName(owner: String, name: String): Option[SpriteLike]
  // D
  def deleteByName(owner: String, name: String): Unit
}

// Sprite Model interface
trait SpriteLike {
  def owner: String
  def name: String
  def modified: Date
  def bytes: Array[Byte]
  lazy val sprite = Sprite.load(bytes)
}
