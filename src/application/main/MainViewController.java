package application.main;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import application.treeComponents.TreeItems;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

public class MainViewController {
	
	public MainViewController() {
		// TODO Auto-generated constructor stub
	}

   public static Map<String,TreeItems> itemList = new HashMap<String,TreeItems>();
   
   public static void addToList(TreeItems t) {
	
	itemList.put(t.getTopic(), t);
	
}

private TreeItem<String> root = new TreeItem<String>("Broker");

    @FXML
	void initialize() {
		   assert serverSelector != null : "fx:id=\"serverSelector\" was not injected: check your FXML file 'mainView.fxml'.";
	       assert btnConnect != null : "fx:id=\"btnConnect\" was not injected: check your FXML file 'mainView.fxml'.";
	       assert tree != null : "fx:id=\"tree\" was not injected: check your FXML file 'mainView.fxml'.";
	       assert btnRefresh != null : "fx:id=\"btnRefresh\" was not injected: check your FXML file 'mainView.fxml'.";

//	       ObservableList<String> options = 
//	    		    FXCollections.observableArrayList(
//	    		        "tcp://srv.pumpcontroller.xyx:1883",
//	    		        "tcp://127.0.0.1:1883"
//	    		        
//	    		    );
	       
	    serverSelector.getItems().add(0, "tcp://srv.pumpcontroller.xyx:1883");
	    serverSelector.getItems().add(1, "tcp://127.0.0.1:1883");
	  
	    
	    tree.setRoot(root);
	    root.setExpanded(true);
	 
	    loadTree();
	}

	@FXML
	private TreeView<String> tree;

	@FXML
    void treeEdited(ActionEvent event) {

    }

    @FXML
    void treeMouseClick(MouseEvent event) {

    }

    @FXML
    void treeMouseEntered(MouseEvent event) {

    }

    private void loadTree(){
		
	}

	@FXML
    private ComboBox<String> serverSelector;

    

	@FXML
	void getLastServers(ActionEvent event) {
	
	}

	@FXML
    private Button btnConnect;
    
    
    
    @FXML
    void connectToServer(ActionEvent event) {
    	try {
			Launcher.mqtt.initialize(serverSelector.getValue());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    @FXML
    private Button btnRefresh;
    
    @FXML
    void refreshTree(ActionEvent event) {
    	
    	tree.getRoot().getChildren().clear();
    	
    	for(Entry<String, TreeItems> t : itemList.entrySet()){
    		t.getValue().addToTree(tree);
    	}
    	
    	tree.getRoot().getChildren().sort( new Comparator<TreeItem<String>>()
        {
           

			
			public int compare(TreeItem<String> o1, TreeItem<String> o2) {
				return o1.getValue().compareTo(o2.getValue());
				
			}        
        });
    }
}
