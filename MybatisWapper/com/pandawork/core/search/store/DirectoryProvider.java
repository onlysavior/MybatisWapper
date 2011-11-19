package com.pandawork.core.search.store;

import java.util.Properties;

import org.apache.lucene.store.Directory;

import com.pandawork.core.search.cfg.ConfigBean;


public interface DirectoryProvider<TDirectory extends Directory> {
	/**
	 * get the information to initialize the directory and build its hashCode/equals method
	 */
	void initialize(String directoryProviderName, Properties properties, ConfigBean context);

	/**
	 * Executed after initialize, this method set up the heavy process of starting up the DirectoryProvider
	 * IO processing as well as background processing are expected to be set up here
	 *
	 */
	void start();

	/**
	 * Executed when the search factory is closed. This method should stop any background process as well as
	 * releasing any resource.
	 * This method should avoid raising exceptions and log potential errors instead
	 */
	void stop();

	/**
	 * Returns an initialized Lucene Directory. This method call <b>must</b> be threadsafe
	 */
	TDirectory getDirectory();
}
