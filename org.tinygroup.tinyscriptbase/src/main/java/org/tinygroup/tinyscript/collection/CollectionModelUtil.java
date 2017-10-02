package org.tinygroup.tinyscript.collection;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptCollectionModel;

/**
 * 集合模型工具类
 * @author yancheng11334
 *
 */
public final class CollectionModelUtil {

	private  static List<ScriptCollectionModel> modelList = new ArrayList<ScriptCollectionModel>();
	
	static {
		addCollectionModel(new ListModel());
		addCollectionModel(new ArrayModel());
		addCollectionModel(new MapModel());
		addCollectionModel(new SetModel());
	}
	
	public static void  addCollectionModel(ScriptCollectionModel model){

		for(ScriptCollectionModel scriptCollectionModel:modelList){
			if(scriptCollectionModel.equals(model) || scriptCollectionModel.getClass().isInstance(model)){
			   return;
			}
		}
		modelList.add(model);
	}
	
	public static void  removeCollectionModel(ScriptCollectionModel model){
		modelList.remove(model);
	}
	
	public static ScriptCollectionModel getScriptCollectionModel(Object object){
		for(ScriptCollectionModel model:modelList){
			if(model.isCollection(object)){
			   return model;
			}
		}
		return null;
	}
}
