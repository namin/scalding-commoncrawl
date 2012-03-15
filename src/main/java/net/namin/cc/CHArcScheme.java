package net.namin.cc;

import java.io.IOException;

import cascading.flow.hadoop.HadoopFlowProcess;
import cascading.scheme.SourceCall;
import cascading.scheme.hadoop.SequenceFile;
import cascading.tap.Tap;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;

import org.commoncrawl.hadoop.io.ARCInputFormat;
import org.commoncrawl.hadoop.io.JetS3tARCSource;
import org.commoncrawl.protocol.shared.ArcFileItem;

public class CHArcScheme extends SequenceFile {
    public static final Fields FIELDS = new Fields( "uri", "mimeType", "item" );
    
    public final String awsCredentials;
    public final String awsSecret;
    public final String bucket;
    public final String inputPrefixes;
    
    public CHArcScheme(String awsCredentials, String awsSecret, String bucket, String inputPrefixes) {
        super( FIELDS );
        this.awsCredentials = awsCredentials;
        this.awsSecret = awsSecret;
        this.bucket = bucket;
        this.inputPrefixes = inputPrefixes;
    }
	
    @Override
    public void sourceConfInit(HadoopFlowProcess flowProcess, Tap tap, JobConf conf) {
        conf.set(JetS3tARCSource.P_AWS_ACCESS_KEY_ID, awsCredentials);
        conf.set(JetS3tARCSource.P_AWS_SECRET_ACCESS_KEY, awsSecret);
        conf.set(JetS3tARCSource.P_BUCKET_NAME, bucket);
        conf.set(JetS3tARCSource.P_INPUT_PREFIXES, inputPrefixes);
	    
        ARCInputFormat.setARCSourceClass(conf, JetS3tARCSource.class);
        ARCInputFormat inputFormat = new ARCInputFormat();
        inputFormat.configure(conf);
        conf.setInputFormat(ARCInputFormat.class);
    }

    @Override
    public boolean source( HadoopFlowProcess flowProcess, SourceCall<Object[], RecordReader> sourceCall ) throws IOException {
        Text key = (Text) sourceCall.getContext()[ 0 ];
        ArcFileItem value = (ArcFileItem) sourceCall.getContext()[ 1 ];
        boolean result = sourceCall.getInput().next( key, value );

        if( !result )
            return false;

        Tuple tuple = sourceCall.getIncomingEntry().getTuple();
        int index = 0;
	    
        tuple.set(index++, value.getUri());
        tuple.set(index++, value.getMimeType());
        tuple.set(index++, value);
	    
        return true;
    }

    @Override
    public void sourceCleanup( HadoopFlowProcess flowProcess, SourceCall<Object[], RecordReader> sourceCall ) {
        sourceCall.setContext( null );
    }
	  
    @Override
    public boolean isSink() {
        return false;
    }
}
