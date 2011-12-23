package com.pandawork.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public abstract class Crumbs<T> {
	private final List<T> contentList;
	private TreeNode root;
	private Field member;
	private Field childMember;
	
	public Crumbs(List<T> list) {
		contentList = list;
	}
	
	public final void toTree(){
		root = new TreeNode("root", NodeType.ROOT, null,getFirstCondition(),null);
		doToTree(root,getFirstCondition());
	}
	
	public Class<?> getElementClass(){
		return contentList.getClass().getComponentType();
	}
	
	public TreeNode getTreeNodeList(Object value){
		return root.contains(value);
	}
	
	protected abstract void setFirstCondition(Object value);
	
	protected abstract Object getFirstCondition();
	
	protected abstract void setConditionProperty(String propertyName) throws NoSuchFieldException;
	
	protected abstract String getConditionProperty();
	
	protected abstract String getNamedProperty();
	
	protected abstract String getChildConditionProperty();
	
	protected abstract String getUrlProperty();
	
	private void doToTree(final TreeNode parent,final Object condition){
		Field member = doGetKeyMember(contentList.get(0));
		Field childMember = doGetChildMember(contentList.get(0));
		TreeNode node = null;
		for(T t : contentList){
			Object maincondition = ReflectionHelper.getMemberValue(t, member);
			Object childcondition = ReflectionHelper.getMemberValue(t, childMember);
			if(maincondition != null && condition.equals(maincondition)){
				if(!checkChildrenExit(member,childcondition)){
					 node = new TreeNode(getNamedValue(t), NodeType.LEAF, parent,maincondition,getUrlValue(t));
				}
				else{
					 node = new TreeNode(getNamedValue(t), NodeType.COMBINATION, parent,maincondition,getUrlValue(t));
					 doToTree(node,childcondition);
				}
			}
		}
	}
	
	private boolean checkChildrenExit(Field member,Object value){
		for(T t: contentList){
			if(ReflectionHelper.getMemberValue(t, member).equals(value)){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private Field doGetKeyMember(T t){
		if(member == null){
			Field result = null;
			Class<T> clazz = (Class<T>) t.getClass();
			try {
				result = clazz.getField(getConditionProperty());
				ReflectionHelper.setAccessible(result);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			member = result;
		}
		return member;
	}
	
	private Field doGetChildMember(T t){
		if(childMember == null){
			Field result = null;
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) t.getClass();
			try {
				result = clazz.getDeclaredField(getChildConditionProperty());
				ReflectionHelper.setAccessible(result);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			childMember = result;
		}
		return childMember;
	}
	
	private String getNamedValue(T t){
		Field nameField = null;
		try {
			nameField = t.getClass().getField(getNamedProperty());
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
		return (String)ReflectionHelper.getMemberValue(t, nameField);
	}
	
	private String getUrlValue(T t){
		Field urlField = null;
		try {
			urlField = t.getClass().getField(getUrlProperty());
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
		return (String)ReflectionHelper.getMemberValue(t, urlField);
	}

	static final class TreeNode {
		private final Object condition;
		private final String name;
		private final NodeType type;
		private final TreeNode parent;
		private final String url;
		private List<TreeNode> children;
		
		public TreeNode (String _name,NodeType _type,TreeNode _parent,Object _condition,String _url){
			name = _name;
			type = _type;
			parent = _parent;
			condition = _condition;
			url = _url;
			
			if(parent != null){
				parent.addChild(this);
			}
		}
		
		public void createChildren(int size){
			if(size <= 0){
				this.children = new ArrayList<TreeNode>();
			}else{
				this.children = new ArrayList<TreeNode>(size);
			}
		}
		
		public List<TreeNode> getChildren(){
			if(type.equals(NodeType.LEAF)){
				return null;
			}
			return children;
		}
		
		public void addChild(TreeNode node){
			if(children == null){
				createChildren(0);
			}
			children.add(node);
		}
		
		public int getChildrenSize(){
			return this.children.size();
		}
		
		public TreeNode contains(Object value){
			if(condition != null && condition.equals(value)){
				return this;
			}
			
			if(children != null){
				for(TreeNode node : children){
					if(node.contains(value) != null){
						return node.contains(value);
					}
				}
			}
			
			return null;
		}
		
		
		private StringBuilder toStringDfs(TreeNode currentNode, StringBuilder sb,int depth)
		{
			for(int i = 0; i < depth; ++i)
				sb.append("        ");
			sb.append(currentNode.name);
			if(currentNode.children != null){
				for(int index = 0; index <currentNode.children.size() ; index++ ){
					sb.append('\n');
					toStringDfs(currentNode.children.get(index), sb, depth+1);
				}	
			}
			return sb;
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			return toStringDfs(this, sb, 0).toString();
		}
		
		public boolean equals(Object other){
			//没有匹配children
			if(this == other){
				return true;
			}
			if(!(other instanceof TreeNode)){
				return false;
			}else{
				TreeNode node = (TreeNode)other;
				return (name.equals(node.getName())) && (type.equals(node.getType())); 
			}
		}

		public String getName() {
			return name;
		}

		public NodeType getType() {
			return type;
		}

		public TreeNode getParent() {
			return parent;
		}
		
		public String getUrl(){
			return url;
		}
	}
	
	static enum NodeType{
		ROOT,
		COMBINATION,
		LEAF
	}
}
