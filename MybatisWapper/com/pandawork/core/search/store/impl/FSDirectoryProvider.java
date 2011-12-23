package com.pandawork.core.search.store.impl;


import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pandawork.core.search.cfg.ConfigBean;
import com.pandawork.core.search.exception.SearchException;
import com.pandawork.core.search.store.DirectoryProvider;
import com.pandawork.core.search.store.DirectoryProviderHelper;


public class FSDirectoryProvider implements DirectoryProvider<FSDirectory> {
    private static final Logger log = LoggerFactory.getLogger(FSDirectoryProvider.class);
    
    private FSDirectory directory;
    private String indexName;

//    public void initialize(String directoryProviderName, Properties properties, SearchFactoryImplementor searchFactoryImplementor) {
//        // on "manual" indexing skip read-write check on index directory
//        boolean manual = searchFactoryImplementor.getIndexingStrategy().equals( "manual" );
//        File indexDir = getVerifiedIndexDir( directoryProviderName, properties, ! manual );
//        try {
//            indexName = indexDir.getCanonicalPath();
//            
//            //this is cheap so it's not done in start()
//            directory = DirectoryProviderHelper.createFSIndex( indexDir );
//        }
//        catch (IOException e) {
//            throw new SearchException( "Unable to initialize index: " + directoryProviderName, e );
//        }
//    }

    public void start() {
        //all the process is done in initialize
    }

    public void stop() {
        try {
            directory.close();
        }
        catch (Exception e) {
            log.error( "Unable to properly close Lucene directory {}" + directory.getFile(), e );
        }
    }

    public FSDirectory getDirectory() {
        return directory;
    }

    @Override
    public boolean equals(Object obj) {
        // this code is actually broken since the value change after initialize call
        // but from a practical POV this is fine since we only call this method
        // after initialize call
        if ( obj == this ) return true;
        if ( obj == null || !( obj instanceof FSDirectoryProvider ) ) return false;
        return indexName.equals( ( (FSDirectoryProvider) obj ).indexName );
    }

    @Override
    public int hashCode() {
        // this code is actually broken since the value change after initialize call
        // but from a practical POV this is fine since we only call this method
        // after initialize call
        int hash = 11;
        return 37 * hash + indexName.hashCode();
    }
    
    public static File getVerifiedIndexDir(String annotatedIndexName, Properties properties, boolean verifyIsWritable) {
        String indexBase = properties.getProperty( "indexBase", "." );
        String indexName = properties.getProperty( "indexName", annotatedIndexName );
        File baseIndexDir = new File( indexBase );
        makeSanityCheckedDirectory( baseIndexDir, indexName, verifyIsWritable );
        File indexDir = new File( baseIndexDir, indexName );
        makeSanityCheckedDirectory( indexDir, indexName, verifyIsWritable );
        return indexDir;
    }
    
    /**
     * @param directory The directory to create or verify
     * @param indexName To label exceptions
     * @param verifyIsWritable Verify the directory is writable
     * @throws SearchException
     */
    private static void makeSanityCheckedDirectory(File directory, String indexName, boolean verifyIsWritable) {
        if ( ! directory.exists() ) {
            log.warn( "Index directory not found, creating: '" + directory.getAbsolutePath() + "'" );
            //if not existing, create the full path
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@directory.mkdirs:" + directory.getAbsolutePath());
            System.out.println("@@@@@@@@@@@@@@@@@@@@@directory.canWrite():" + directory.isFile());
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            
            if(!directory.exists())
                directory.mkdirs();
           
        }
    }

	@Override
	public void initialize(String directoryProviderName, Properties properties,
			ConfigBean context) {
		 File indexDir = getVerifiedIndexDir( directoryProviderName, properties, false );
		 try{
			 indexName = indexDir.getCanonicalPath();
			 directory = DirectoryProviderHelper.createFSIndex( indexDir,properties );
		 }catch(IOException e){
			 throw new SearchException( "Unable to initialize index: " + directoryProviderName, e );
		 }
	}
}
