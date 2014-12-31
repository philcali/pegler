package pegler
package app

import io.Source.{ fromFile => open }
import java.io.File

import argonaut._, Argonaut._
import dynamodb.S3Config
import dynamodb.SpriteStoreDynamo
import unfiltered.filter.Planify

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.s3.model.Region

case class AppConfig(port: Int = 8080, aws: S3Config)

object Server {
  implicit def AppConfigCodec: CodecJson[AppConfig] =
    casecodec2(AppConfig.apply, AppConfig.unapply)("port", "aws")

  implicit def S3ConfigCodec: CodecJson[S3Config] =
    casecodec2(S3Config.apply, S3Config.unapply)("bucket", "region")

  implicit def RegionCodec: CodecJson[Region] =
    CodecJson(
      (r: Region) => jString(r.name()),
      c => for (region <- c.as[String]) yield Region.valueOf(region))

  def main(args: Array[String]) {
    args match {
      case Array(configPath) if (new File(configPath).exists) =>
      val json = open(configPath).getLines.mkString("\n")
      Parse.decodeEither[AppConfig](json).fold(println _, {
        config =>
        val credentials = new DefaultAWSCredentialsProviderChain()
        val mapper = new DynamoDBMapper(new AmazonDynamoDBClient(credentials), credentials)
        val db = new SpriteStoreDynamo(S3Config("sprites"), mapper)

        unfiltered.jetty.Http(config.port)
          .filter(Planify(ApiWithUploads(db).intent))
          .run {
            server =>
            println(s"Server started {s.url}")
          }
      })
      case _ => println("Provide a valid config json")
    }
  }
}

