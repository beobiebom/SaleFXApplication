package Code_FXML;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
     private ImageView imgpay;
    @FXML
    private ImageView imgcustomer;
    @FXML
    private Label lbname;
    @FXML
    private Label lbphone;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void show(String name, String phone){
        lbname.setText(name);
        lbphone.setText(phone);
    }
    @FXML
    public void handleimgaction(MouseEvent e){
        if(e.getSource()==imgpay){
            try{
                Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
            Scene scene=new Scene(FXMLLoader.load(this.getClass().getResource("Pay_Menu.fxml")));
            stage.setTitle("Pay_Menu");
            stage.setScene(scene);
            stage.show();}catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
