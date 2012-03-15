Scalding CommonCrawlx
====================

Experiments in using [scalding](https://github.com/twitter/scalding)
with the [CommonCrawl](http://www.commoncrawl.org) dataset.

`sbt assembly` creates the jar to be passed to Hadoop or Amazon's
Elastic Map Reduce as a custom jar.

A local run looks like this:
`hadoop jar target/scalding-commoncrawl-assembly-0.1.jar net.namin.cc.UrlJob --hdfs --awsCredentials <awsCredentials> --awsSecret <awsSecret> --bucket <bucket-name> --inputPrefixes <path> --scratch s3n://<awsCredentials>:<awsSecret>@<bucket-name> --output <output-dir>`

For an EMR run, just upload and run the assembly jar as a custom
jar. The arguments may be the same as for a local run, though --output
should be on s3n.