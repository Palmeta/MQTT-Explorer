package application.treeComponents;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeItems{
	
	private String topic;
	private String data;
	
	
	
	
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
	public void addToTree(TreeView<String> tree){
		
		String[] topicArray = topic.split("/");
		
		TreeItem<String> activeLevel = tree.getRoot();
		
		for (int i = 0; i < topicArray.length; i++) {
			
			TreeItem<String> current = new TreeItem<String>(topicArray[i]);
			boolean b = false;
			for(TreeItem<String> t: activeLevel.getChildren()){
				if(t.getValue().equals(current.getValue())){
					activeLevel = t;
					b = true;
					break;
				}
			}
			if(b){
				continue;
			}
			
			
				activeLevel.getChildren().add(current);
				activeLevel = current;
			
		}
		activeLevel.setValue(activeLevel.getValue() + " -> " + data);
		tree.refresh();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj.getClass().equals(this.getClass())&& ((TreeItems)obj).data.equals(this.data) && ((TreeItems)obj).topic.equals(this.topic);
	}
}
