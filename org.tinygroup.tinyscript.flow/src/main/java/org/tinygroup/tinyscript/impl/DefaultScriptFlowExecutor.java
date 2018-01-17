package org.tinygroup.tinyscript.impl;

import java.util.List;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.config.ScriptComponentConfig;
import org.tinygroup.tinyscript.config.ScriptFlowConfig;
import org.tinygroup.tinyscript.config.ScriptFlowNodeConfig;
import org.tinygroup.tinyscript.config.ScriptInputParameterConfig;
import org.tinygroup.tinyscript.config.ScriptLinkConfig;
import org.tinygroup.tinyscript.config.ScriptLinkItemConfig;
import org.tinygroup.tinyscript.config.ScriptOutputParameterConfig;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 默认的脚本流程执行器
 * @author yancheng11334
 *
 */
public class DefaultScriptFlowExecutor extends AbstractScriptFlowExecutor{

	public Object executeFlow(String flowId, ScriptContext context)
			throws Exception {
		ScriptFlowConfig config = getScriptFlowManager().getScriptFlowConfig(flowId);
		if(config==null){
		   throw new Exception(String.format("根据%s流程ID没有找到对应的流程信息", flowId));
		}
		
		ScriptFlowNodeConfig nodeConfig = config.getRootNode();
		if(nodeConfig==null){
		   throw new Exception(String.format("%s流程ID的流程不存在顶层节点,请检查配置.", flowId));
		}
		
		//递归执行流程节点
		return executeFlowNode(config,nodeConfig,context);
	}
	
	@SuppressWarnings("rawtypes")
	private Object executeFlowNode(ScriptFlowConfig flow,ScriptFlowNodeConfig node, ScriptContext context)
			throws Exception {
		List<ScriptFlowNodeConfig> nodes = flow.getDependentNodeList(node.getNodeId());
		
		
		ScriptContext runContext = new DefaultScriptContext();
		runContext.setParent(context);
		
		if(!CollectionUtil.isEmpty(nodes)){
			
			//先递归执行依赖的节点
			for(ScriptFlowNodeConfig dependentNode:nodes){
				Object result = executeFlowNode(flow,dependentNode,context);
				//获取结果配置
				ScriptLinkConfig linkConfig = flow.getScriptLinkConfig(dependentNode.getNodeId(), node.getNodeId());
				if(linkConfig==null||linkConfig.getItemList()==null||linkConfig.getItemList().isEmpty()){
					throw new Exception(String.format("源节点%s,目标节点%s的结果配置不存在或条目为空.", dependentNode.getNodeId(),node.getNodeId()));
				}
				for(ScriptLinkItemConfig itemConfig:linkConfig.getItemList()){
				    if("order".equals(itemConfig.getType())){
				    	//根据计算结果顺序配置上下文
				    	if(itemConfig.getOrder()>0){
				    	   if(!(result instanceof List)){
				    		   throw new Exception(String.format("执行结果不是List类型,不支持多个输出")); 
				    	   }
				    	   List list = (List) result;
				    	   runContext.put(itemConfig.getParameterName(), list.get(itemConfig.getOrder()));
				    	}else{
				    	   runContext.put(itemConfig.getParameterName(),result);
				    	}
				    }else if("name".equals(itemConfig.getType())){
				    	runContext.put(itemConfig.getParameterName(), context.get(itemConfig.getEnvName()));
				    }else{
				    	throw new Exception(String.format("未知的条目配置类型:%s", itemConfig.getType()));
				    }
				}
			}

		}
		
		//获取非依赖的配置
		ScriptLinkConfig linkConfig = flow.getScriptLinkConfig(null, node.getNodeId());
		if(linkConfig!=null && linkConfig.getItemList()!=null){
			for(ScriptLinkItemConfig itemConfig:linkConfig.getItemList()){
			    if("order".equals(itemConfig.getType())){
			    	//根据初始化参数顺序配置上下文
			    	List list = context.get(FLOW_INPUT_NAME);
			    	runContext.put(itemConfig.getParameterName(), list.get(itemConfig.getOrder()));
			    }else if("name".equals(itemConfig.getType())){
			    	runContext.put(itemConfig.getParameterName(), context.get(itemConfig.getEnvName()));
			    }else{
			    	throw new Exception(String.format("未知的条目配置类型:%s", itemConfig.getType()));
			    }
			}
		}
		
		//构建上下文
		return executeComponent(node.getComponentId(),runContext);
	}

	@SuppressWarnings("rawtypes")
	public Object executeComponent(String componentId, ScriptContext context)
			throws Exception {
		ScriptComponentConfig sc = getScriptFlowManager().getScriptComponentConfig(componentId);
		if(sc==null){
		   throw new Exception(String.format("根据%s组件ID没有找到对应的组件信息", componentId));
		}
		ScriptContext runContext = new DefaultScriptContext();
		runContext.setParent(context);
		
		//输入参数处理
		if(sc.getInputList()!=null){
		   for(ScriptInputParameterConfig input:sc.getInputList()){
		       if(context.exist(input.getName())){
		    	   //用户设置参数类型进行参数检查
		    	   Object value = context.get(input.getName());
		    	   if(value!=null && input.getType()!=null && !"java.lang.Object".equals(input.getType())){
		    		  if(!Class.forName(input.getType()).isInstance(value)){
		    			  throw new Exception(String.format("参数%s的实际类型是%s,和设置类型不匹配!", input.getName(),value.getClass().getName()));
		    		  }
		    	   }
		       }else if(input.getDefaultValue()!=null){
		    	   //参数为空，触发默认值逻辑
		    	   runContext.put(input.getName(), getScriptEngine().execute(ScriptContextUtil.convertExpression(input.getDefaultValue())));
		       }else{
		    	   throw new Exception(String.format("参数%s没有配置到初始化上下文，也没有定义默认值", input.getName()));
		       }
		   }
		}
		Object result = getScriptEngine().execute(sc.getScript(), runContext);
		//输出参数处理
		if(sc.getOutputList()!=null && !sc.getOutputList().isEmpty()){
		   if(sc.getOutputList().size()>1){
			  if(result==null ||!(result instanceof List)){
				 throw new Exception("脚本返回类型不是List,不支持多个输出值!");
			  }
			  List list = (List) result;
			  if(list.size()!=sc.getOutputList().size()){
				 throw new Exception(String.format("脚本实际返回%s个元素,与定义的%个输出结果不匹配", list.size(),sc.getOutputList().size()));
			  }
			  for(int i=0;i<list.size();i++){
				  ScriptOutputParameterConfig output = sc.getOutputList().get(i);
				  Object value = list.get(i);
				  if(value==null && "false".equals(output.getNullable())){
					  throw new Exception(String.format("脚本第%s个输出不支持返回null值", i)); 
				  }
				  if(value!=null && output.getType()!=null && !"java.lang.Object".equals(output.getType())){
					  if(!Class.forName(output.getType()).isInstance(value)){
		    			  throw new Exception(String.format("结果的实际类型是%s,和设置类型不匹配!", value.getClass().getName()));
		    		  }
				  }
			  }
		   }else{
			  ScriptOutputParameterConfig output = sc.getOutputList().get(0);
			  if(result==null && "false".equals(output.getNullable())){
				  throw new Exception("脚本不支持返回null值"); 
			  }
			  if(result!=null && output.getType()!=null && !"java.lang.Object".equals(output.getType())){
				  if(!Class.forName(output.getType()).isInstance(result)){
	    			  throw new Exception(String.format("结果的实际类型是%s,和设置类型不匹配!", result.getClass().getName()));
	    		  }
			  }
		   }
		}
		return result;
	}


}
