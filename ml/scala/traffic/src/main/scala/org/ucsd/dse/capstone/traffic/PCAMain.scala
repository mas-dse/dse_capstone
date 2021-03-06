package org.ucsd.dse.capstone.traffic

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client

/**
 * @author dyerke
 */
object PCAMain {

  def main(args: Array[String]) {
    val template: SparkTemplate = new DefaultSparkTemplate()
    //
    template.execute { sc => do_execute(sc) }
  }

  def do_execute(sc: SparkContext) = {
    val sqlContext: SQLContext = new SQLContext(sc)
    //
    val paths = List[String]("/var/tmp/test_output2")
    val output_parameter = new OutputParameter("test", "/var/tmp/test_results")
    //
    val output_aws_id = null // replace with access id
    val output_aws_secret_key = null // replace with secret key
    val cred: AWSCredentials = new BasicAWSCredentials(output_aws_id, output_aws_secret_key)
    val client: AmazonS3 = new AmazonS3Client(cred)
    val bucket_name: String = "dse-team2-2014"
    val s3_param = new S3Parameter(client, bucket_name)
    //
    val executor: Executor[PCAResults] = new PCAExecutor(paths, output_parameter, s3_param)
    executor.execute(sc, sqlContext)
  }
}
