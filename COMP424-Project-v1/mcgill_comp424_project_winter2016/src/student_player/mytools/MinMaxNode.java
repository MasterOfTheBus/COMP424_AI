package student_player.mytools;

import java.util.ArrayList;
import java.util.List;

public class MinMaxNode<T> {

	List<MinMaxNode<T>> children = new ArrayList<MinMaxNode<T>>();
	MinMaxNode<T> parent;
	T data;
	int depth;
	public int score = 0;
	private boolean max;
	
	public MinMaxNode(T data, boolean max) {
		this.data = data;
		depth = 0;
		this.max = max;
	}
	
	public MinMaxNode(T data, int depth, boolean max) {
		this.data = data;
		this.depth = depth;
		this.max = max;
	}
	
	public MinMaxNode<T> addChild(T data) {
		MinMaxNode<T> child = new MinMaxNode<T>(data, this.depth+1, !(this.max));
		child.parent = this;
		this.children.add(child);
		return child;
	}
	
	public void addChild(MinMaxNode<T> child) {
		child.parent = this;
		this.children.add(child);
	}

	public void setParent(MinMaxNode<T> parent) {
		parent.children.add(this);
		this.parent = parent;
		
	}
	
	public void makeRoot() {
		this.parent = null;
	}
	
	public boolean isRoot() {
		return (parent == null);
	}
	
	public boolean isLeaf() {
		return (children.size() == 0);
	}
	
	public boolean isMax() {
		return max;
	}
	
	public boolean isMin() {
		return !max;
	}
	
}
