package org.tinygroup.tinyscript.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 脚本关联配置
 * @author yancheng11334
 *
 */
@XStreamAlias("link")
public class ScriptLinkConfig {

	@XStreamAsAttribute
	@XStreamAlias("source-id")
	private String sourceId;
	
	@XStreamAsAttribute
	@XStreamAlias("target-id")
	private String targetId;
	
	@XStreamImplicit
	private List<ScriptLinkItemConfig> itemList;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public List<ScriptLinkItemConfig> getItemList() {
		return itemList;
	}

	public void setItemList(List<ScriptLinkItemConfig> itemList) {
		this.itemList = itemList;
	}
	
}
