package net.namin.cc

import com.twitter.scalding._

class UrlJob(args: Args) extends Job(args) {
  ArcFile(args("awsCredentials"), args("awsSecret"), args("bucket"), args("inputPrefixes"), args("scratch")).
  read.
  project('uri).
  write(TextLine(args("output")))
}
