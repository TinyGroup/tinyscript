package org.tinygroup.tinyscript.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 脚本流程节点配置项
 * @author yancheng11334
 *
 */
@XStreamAlias("node")
public class ScriptFlowNodeConfig {

	/**
	 * 节点ID
	 */
	@XStreamAsAttribute
	@XStreamAlias("node-id")
	private String nodeId;
	
	/**
	 * 绑定的组件ID
	 */
	@XStreamAsAttribute
	@XStreamAlias("component-id")
	private String componentId;
	
	/**
	 * 要保存的变量名列表，采用英文逗号分隔
	 */
	@XStreamAlias("save-name")
	private String saveName;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	
	public String[] getSaveNames(){
		return saveName==null?null:saveName.split(",");
	}
}
