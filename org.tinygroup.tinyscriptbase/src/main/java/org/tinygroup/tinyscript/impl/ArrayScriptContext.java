package org.tinygroup.tinyscript.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.tinyscript.ScriptContext;

/**
 * 多维数组初始化的上下文
 * 
 * @author yancheng11334
 * 
 */
public class ArrayScriptContext extends ContextImpl implements
		ScriptContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8021808644510481934L;
	
	
	private int length = -1;
    private int order = -1;
	public ArrayScriptContext(Context context,int length){
		super();
		this.setParent(context);
		this.setLength(length);
	}
	
	
	/**
	 * 获得根节点至当前节点的全部维度
	 * @return
	 */
	public Integer[] getDimsFromRoot() {
		List<Integer> dimList = new ArrayList<Integer>();
		ArrayScriptContext parent = this;
		while(parent!=null && parent.getOrder()>=0){
			dimList.add(0,parent.getOrder());
			parent = getParent(parent);
		}
		Integer[] dims = new Integer[dimList.size()];
		return dimList.toArray(dims);
	}
	
	private ArrayScriptContext getParent(ArrayScriptContext context){
		Context parent = context.getParent();
		if(parent instanceof ArrayScriptContext){
			return (ArrayScriptContext) parent;
		}else{
			return null;
		}
	}
	/**
	 * 获得当前节点至子节点的全部维度
	 * 
	 * @return
	 */
	public Integer[] getDimsToLeaf() {
		List<Integer> dimList = new ArrayList<Integer>();
		dealDimList(dimList, this);
		Integer[] dims = new Integer[dimList.size()];
		return dimList.toArray(dims);
	}

	private void dealDimList(List<Integer> dimList,
			ArrayScriptContext context) {
		dimList.add(context.getLength());
		Map<String, Context> subContexts = context.getSubContextMap();
		
		if(!subContexts.isEmpty()){
			ArrayScriptContext maxLengthContext = null;
			// 遍历子数组,获取最大长度的那个继续递归
			for (Context sub : subContexts.values()) {
				ArrayScriptContext subArray = (ArrayScriptContext) sub;
				if (maxLengthContext == null) {
					maxLengthContext = subArray;
				} else if (maxLengthContext.getLength() < subArray.getLength()) {
					maxLengthContext = subArray;
				}
			}
			dealDimList(dimList, maxLengthContext);	
		}
		
	}
	
	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * 得到当前层次的子元素数目
	 * 
	 * @return
	 */
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * 遍历层次的元素
	 * 
	 * @return
	 */
	public Iterator<Element> iterator() {
		List<Element> elementList = new ArrayList<Element>();
		addElements(elementList,this);
		return elementList.iterator();
	}

	/**
	 * 递归处理节点的子元素
	 * @param elementList
	 * @param context
	 */
	protected void addElements(List<Element> elementList,ArrayScriptContext context) {
		elementList.addAll(context.getElements()); 
		Map<String, Context> subContexts = context.getSubContextMap();
		if (!subContexts.isEmpty()) {
		   for(Context subContext:subContexts.values()){
			   ArrayScriptContext arrayScriptContext = (ArrayScriptContext) subContext;
			   arrayScriptContext.addElements(elementList, arrayScriptContext);
		   }
		} 
	}

	/**
	 * 包装当前节点的子元素
	 * @return
	 */
	protected  List<Element> getElements(){
		List<Element> elementList = new ArrayList<Element>();
		Integer[] dims = getDimsFromRoot();
		for(Entry<String, Object> entry:getItemMap().entrySet()){
			int index = Integer.parseInt(entry.getKey());
			Element element = new ObjectElement(dims,index,entry.getValue());
			elementList.add(element);
		}
		return elementList;
	}

	public boolean exist(String name) {
		return getItemMap().containsKey(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T) getItemMap().get(name);
	}

	public interface Element {
		Integer[] getItems(); // 得到所在下标

		int getIndex(); //得到元素所在的索引下标
		
		Object getValue(); //得到值
	}
	
	class ObjectElement implements Element{

		private Integer[] items;
		private int index;
		private Object value;
		
		public ObjectElement(Integer[] items, int index, Object value) {
			super();
			this.items = items;
			this.index = index;
			this.value = value;
		}

		public Integer[] getItems() {
			return items;
		}

		public int getIndex() {
			return index;
		}

		public Object getValue() {
			return value;
		}
		
	}
}
