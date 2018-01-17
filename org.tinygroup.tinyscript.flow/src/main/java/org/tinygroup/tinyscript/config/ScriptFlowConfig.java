package org.tinygroup.tinyscript.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 脚本流程配置项
 * @author yancheng11334
 *
 */
@XStreamAlias("flow")
public class ScriptFlowConfig {

	@XStreamAsAttribute
	private String id;
	
	@XStreamAsAttribute
	private String name;
	
	private String description;
	
	@XStreamImplicit
	private List<ScriptFlowNodeConfig> nodeList;
	
	@XStreamImplicit
	private List<ScriptLinkConfig> linkList;
	
	//流程的节点映射表
	@XStreamOmitField
	private Map<String,ScriptFlowNodeConfig> nodeMap;
	
	//定义节点之间的依赖关系
	@XStreamOmitField
	private Map<String,List<String>> dependentMap;
	
	//定义节点之间的跳转关系
	@XStreamOmitField
	private Map<String,String> gotoMap;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ScriptFlowNodeConfig> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<ScriptFlowNodeConfig> nodeList) {
		this.nodeList = nodeList;
		updateNode();
	}

	public List<ScriptLinkConfig> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<ScriptLinkConfig> linkList) {
		this.linkList = linkList;
		updateLink();
	}
	
	/**
	 * 更新node关系
	 */
	private void updateNode(){
		nodeMap = new HashMap<String,ScriptFlowNodeConfig>();
		if(nodeList!=null){
		   for(ScriptFlowNodeConfig config:nodeList){
			   nodeMap.put(config.getNodeId(), config);
		   }
		}
	}
	
	private void updateLink(){
		dependentMap = new HashMap<String,List<String>>();
		if(linkList!=null){
		   for(ScriptLinkConfig config:linkList){
			   List<String> list = dependentMap.get(config.getTargetId());
			   if(list==null){
				  list = new ArrayList<String>(); 
				  dependentMap.put(config.getTargetId(), list);
			   }
			   if(config.getSourceId()!=null && !list.contains(config.getSourceId())){
				  list.add(config.getSourceId());
			   }
		   }
		}
		gotoMap = new HashMap<String,String>();
		if(linkList!=null){
		   for(ScriptLinkConfig config:linkList){
			  if(config.getSourceId()!=null){
				 gotoMap.put(config.getSourceId(), config.getTargetId());
			  }
		   }
		}
	}
	
	private boolean ifInit(){
		return nodeMap!=null && dependentMap!=null && gotoMap!=null;
	}
	
	/**
	 * 获取顶层节点
	 * @return
	 */
	public ScriptFlowNodeConfig getRootNode(){
		if(!ifInit()){
		   updateNode();
		   updateLink();
		}
		if(nodeList!=null && nodeList.size()>0){
		   ScriptFlowNodeConfig node = nodeList.get(0); //随机找一个开始节点
		   if(nodeList.size()>1){
			  while(gotoMap.containsKey(node.getNodeId())){
				 node = nodeMap.get(gotoMap.get(node.getNodeId()));
			  }
		   }
		   return node;
		}
		return null;
	}
	
	/**
	 * 获得节点依赖的其他节点
	 * @param nodeId
	 * @return
	 */
	public List<ScriptFlowNodeConfig> getDependentNodeList(String nodeId){
		if(!ifInit()){
		   updateNode();
		   updateLink();
		}
		List<ScriptFlowNodeConfig> nodes = new ArrayList<ScriptFlowNodeConfig>();
		List<String> nodeIds = dependentMap.get(nodeId);
		if(nodeIds!=null){
		   for(String id:nodeIds){
			   nodes.add(nodeMap.get(id));
		   }
		}
		return nodes;
	}
	
	public List<ScriptLinkConfig> getScriptLinkConfigsByTargerId(String targetId){
		List<ScriptLinkConfig> list = new ArrayList<ScriptLinkConfig>();
		if(linkList!=null){
		   for(ScriptLinkConfig config:linkList){
			  if(config.getTargetId().equals(targetId)){   
				  list.add(config); 
			  }
		   }
		}
		return list;
	}
	
	public ScriptLinkConfig getScriptLinkConfig(String sourceId,String targetId){
		if(linkList!=null){
			for(ScriptLinkConfig config:linkList){
			   if(config.getTargetId().equals(targetId)){
				  if(sourceId==null && config.getSourceId()==null){
					  return config;
				  }else if(sourceId!=null && config.getSourceId()!=null && sourceId.equals(config.getSourceId())){
					  return config;
				  }
			   }
			}
		}
		return null;
	}
}
