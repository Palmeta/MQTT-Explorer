package application.treeComponents.treeViewDataSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class Items extends TreeItem<Items> implements HierarchyData<Items>{

	private ObservableList<Items> children = FXCollections.observableArrayList();
	private String name;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ObservableList getChildren() {
		// TODO Auto-generated method stub
		return children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	

}
