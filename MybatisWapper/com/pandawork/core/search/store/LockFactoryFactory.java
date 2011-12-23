package com.pandawork.core.search.store;

import java.io.File;
import java.util.Properties;

import org.apache.lucene.store.LockFactory;

public interface LockFactoryFactory {
	LockFactory createLockFactory(File indexDir, Properties dirConfiguration);
}
