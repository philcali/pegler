package pegler
package dynamodb

import annotation.tailrec
import collection.JavaConversions.iterableAsScalaIterable

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.Date

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.S3Link
import com.amazonaws.services.s3.model.Region

case class S3Config(
  bucket: String,
  region: Region = Region.US_Standard
)

case class SpriteStoreDynamo(
  s3config: S3Config,
  dynamo: DynamoDBMapper
) extends SpriteStore {

  def save(owner: String, name: String, bytes: Array[Byte]) = {
    val s3Link = dynamo.createS3Link(
      s3config.region,
      s3config.bucket,
      s"${owner}/${name}")
    val model = new SpriteModel(
      owner,
      name,
      new Date(),
      s3Link)
    s3Link.uploadFrom(bytes)
    dynamo.save(model)
    model
  }

  def readOwner(owner: String) = dynamo.query(classOf[SpriteModel], new DynamoDBQueryExpression[SpriteModel]()
    .withConsistentRead(true)
    .withHashKeyValues(new SpriteModel(owner = owner))).toList

  def readByName(owner: String, name: String) = dynamo.load(classOf[SpriteModel], owner, name) match {
    case null => None
    case model => Some(model)
  }

  def deleteByName(owner: String, name: String) = readByName(owner, name).foreach {
    case model: SpriteModel =>
      model
        .location
        .getAmazonS3Client()
        .deleteObject(s3config.bucket, model.location.getKey())
      dynamo.delete(model)
  }
}


@DynamoDBTable(tableName="Sprites")
class SpriteModel(
  @DynamoDBHashKey
  var owner: String = "",
  @DynamoDBRangeKey
  var name: String = "",
  @DynamoDBAttribute
  var modified: Date = new Date(),
  @DynamoDBAttribute
  var location: S3Link = null
) extends SpriteLike {
  override def bytes = {
    val bos = new ByteArrayOutputStream()
    try {
      location.downloadTo(bos)
      bos.toByteArray
    } finally {
      bos.close
    }
  }
}
