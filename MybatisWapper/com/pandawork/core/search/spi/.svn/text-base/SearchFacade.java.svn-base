package com.pandawork.core.search.spi;

import java.io.Serializable;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import com.pandawork.core.dao.impl.CommonDaoUtil;
import com.pandawork.core.entity.AbstractEntity;
import com.pandawork.core.search.backend.AddLuceneWork;
import com.pandawork.core.search.backend.DeleteLuceneWork;
import com.pandawork.core.search.backend.LuceneWork;
import com.pandawork.core.search.cfg.ConfigBean;
import com.pandawork.core.search.cfg.ConfigBean.BuildContext;
import com.pandawork.core.search.engine.DocumentBuilderIndexedEntity;

//TODO Update的过程，每次操作完是否都需要优化，线程方式
public class SearchFacade {
	private static ConfigBean config = ConfigBean.getInstanse();

	public static <T> void onPostInsert(T entity) {
		Serializable id = null;
		try {
			 id = (Serializable) CommonDaoUtil.readValue(entity,
					CommonDaoUtil.getPrimKey(entity, entity.getClass()));
		} catch (Exception e) {
			// 不会出错
		}
		
		DocumentBuilderIndexedEntity<?> builder = BuildContext.class2IndexedEntites.get(entity.getClass());
		Document doc = builder.getDocument(entity, id, null);
		LuceneWork work = new AddLuceneWork(id, id.toString(), entity.getClass(), doc, null, false);
		
		IndexWriter writer = config.getBuildContext().getIndexWriter(false);
		work.performWork(writer);
	}

	public static <T> void onPostDelete(T entity) {
		Serializable id = null;
		try {
			 id = (Serializable) CommonDaoUtil.readValue(entity,
					CommonDaoUtil.getPrimKey(entity, entity.getClass()));
		} catch (Exception e) {
			// 不会出错
		}
		
		LuceneWork work = new DeleteLuceneWork(id, id.toString(), entity.getClass());
		
		IndexWriter writer = config.getBuildContext().getIndexWriter(false);
		work.performWork(writer);
	}

	public static <T> void onPostUpdate(T entity) {
		// TODO onPostUpdate 需要研究下
	}
	
	
}
