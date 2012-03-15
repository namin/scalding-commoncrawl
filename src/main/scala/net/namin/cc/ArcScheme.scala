package net.namin.cc

import cascading.scheme.Scheme
import cascading.flow.FlowProcess
import com.twitter.scalding.Source
import com.twitter.scalding.FixedPathSource

trait ArcFileScheme {
  val awsCredentials: String
  val awsSecret: String
  val bucket: String
  val inputPrefixes: String
  
  //doesn't support local mode yet
  def localScheme = null
  def hdfsScheme = {
    new CHArcScheme(awsCredentials, awsSecret, bucket, inputPrefixes).asInstanceOf[Scheme[_ <: FlowProcess[_],_,_,_,_,_]]
  }
}

case class ArcFile(awsCredentials: String, awsSecret: String, bucket: String, inputPrefixes: String, scratch: String) extends FixedPathSource(scratch) with ArcFileScheme
