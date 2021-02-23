package Code_FXML;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import utils.ConnectionUtil;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;


public class Pay_Menu_Controller implements Initializable {
    @FXML
    private TextField txtsearch;
    @FXML
    private Button btnadd;
    @FXML
    private AnchorPane pane;
    @FXML
    private AnchorPane pane2;
    @FXML
    private ListView<String> listproduct;
    @FXML
    private TableColumn<Product,Integer> tb_colstt;
    @FXML
    private TableColumn<Product,String> tb_colname;
    @FXML
    private TableColumn<Product,Integer> tb_colcostalone;
    @FXML
            private TableColumn<Product,Integer> tb_colcostsum;
    @FXML
            private TableColumn<Product,Product> tb_colquantity;
    @FXML
            private TableView<Product> tvpay;
    @FXML
            private Button btnpay;
    @FXML
            private Label lbsummoney;
    @FXML
            private Label lbmoneygive;
    @FXML
            private TextField txtmoneytake;
    @FXML
            private TextField txtsearch2;
    @FXML
            private ListView<String> lvcustomer;
    int stt=1;
    private ObservableList<String> listnameproduct;
    private ObservableList<String> listcustomer;
    private Image img_botot = new Image("/Image_product/tuongotHaehandle.png");
    private Image img_sampa = new Image("/Image_product/sampa.jpg");
    private Image[] listimage = {img_botot, img_sampa};
    private int vitri = 0;
    Connection con=null;
    PreparedStatement ps=null;
    ResultSet rs=null;
    ObservableList<Product> oslistproduct;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        oslistproduct=FXCollections.observableArrayList();
        con = (Connection) ConnectionUtil.conDB();
        String sqlselect = "SELECT * FROM pay";
        listnameproduct = FXCollections.observableArrayList();
        try {
            rs = con.createStatement().executeQuery(sqlselect);
            while (rs.next()) {
                String s = "";
                s = rs.getString(1) + " | Giá: " + rs.getInt(2) + " VND";
                listnameproduct.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listproduct.setItems(listnameproduct);
        filllvcustomer();
        lvcustomer.setItems(listcustomer);
        listproduct.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(img_botot);
                    setText(name);
                    setGraphic(imageView);
                }
            }
        });
        listproduct.setVisible(false);
        txtmoneytake.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                lbmoneygive.setText((Integer.valueOf(t1)-Integer.valueOf(lbsummoney.getText()))+"");
            }
        });
        txtsearch.textProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue observable, Object oldVal, Object newVal) {
                listproduct.setVisible(true);
                 if (txtsearch.getText().equals("")) {
                     listproduct.setVisible(false);
                 }
                search((String) oldVal, (String) newVal);
            }
        });
        //
        // search customer
        lvcustomer.setVisible(false);
        txtsearch2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                lvcustomer.setVisible(true);
                if(txtsearch2.getText().isEmpty()){
                    lvcustomer.setVisible(false);
                }
                search2(oldVal,newVal);
            }
        });
        //
        //
        listproduct.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fetTBVIEW(listproduct.getSelectionModel().getSelectedItem());
            }
        });
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listproduct.setVisible(false);
                lvcustomer.setVisible(false);
            }
        });
        pane2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                listproduct.setVisible(false);
                lvcustomer.setVisible(false);
            }
        });
        tb_colstt.setCellValueFactory(new PropertyValueFactory<Product,Integer>("stt"));
       tb_colname.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));
       tb_colcostalone.setCellValueFactory(new PropertyValueFactory<Product,Integer>("cost"));
       tb_colcostsum.setCellValueFactory(new PropertyValueFactory<Product,Integer>("costsum"));
        tb_colquantity.setCellValueFactory(param -> new SimpleObjectProperty<Product>(param.getValue()));
        tb_colquantity.setCellFactory(param->new TableCell<Product,Product>(){
            @Override
            protected void updateItem(Product item,boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setText(null);
                }else{
                    Spinner<Integer> spinner=new Spinner<>();
                    spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10,item.getQuantity()));
                    spinner.valueProperty().addListener(/*(ChangeListener<Integer>)*/(Product,oldVal,newVal)->{
                        item.quantityProperty().set(newVal);
                        item.setCostsum(newVal*item.getCost());
                                int row=getTableRow().getIndex();
                        System.out.println(row);
                        lbsummoney.setText(String.valueOf(Integer.valueOf(lbsummoney.getText())+item.getCost()));
                        tvpay.getItems().set(row,item);
                    });
                    setGraphic(spinner);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }

            }
        });
        /*tb_colquantity.setCellFactory(new Callback<TableColumn<Product, Integer>, TableCell<Product, Integer>>() {
            @Override
            public TableCell<Product, Integer> call(TableColumn<Product, Integer> productIntegerTableColumn) {
                TableCell<Product,Integer> cell=new TableCell<Product,Integer>(){
                    @Override
                    public void updateItem(Integer item,boolean empty){
                        if(item!=null){
                            Spinner<Integer> spinner=new Spinner<>(1,10,1);
                            setGraphic(spinner);
                            spinner.valueProperty().addListener((ChangeListener<Integer>)(Product,oldVal,newVal)->{
                                spinner.getValueFactory().
                        });
                        }
                    }
                };
                return cell;
            }
        });*/

    }
    /* Action listview customer
    public void action_search_customer(KeyEvent e){
          lvcustomer.setVisible(true);
    }
    */
    public void search(String oldVal, String newVal) {
        if (oldVal != null && (newVal.length() < oldVal.length())) {
            listproduct.setItems(listnameproduct);
        }
        String value = newVal.toUpperCase();
        ObservableList<String> subentries = FXCollections.observableArrayList();
        for (Object entry : listproduct.getItems()) {
            boolean match = true;
            String entryText = (String) entry;
            if (!entryText.toUpperCase().contains(value)) {
                match = false;
            }
            if (match) {
                subentries.add(entryText);
            }
        }
        listproduct.setItems(subentries);
    }
    public void filllvcustomer(){
        String sqlfill="SELECT * FROM customer";
        listcustomer=FXCollections.observableArrayList();
        try{
            rs=con.createStatement().executeQuery(sqlfill);
            while(rs.next()) {
                listcustomer.add(rs.getString(1)+"\n"+rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void search2(String oldVal, String newVal){
        if(oldVal!=null&&(newVal.length()<oldVal.length())){
            lvcustomer.setItems(listcustomer);
        }
        String value = newVal.toUpperCase();
        ObservableList<String> subentries = FXCollections.observableArrayList();
        for (String entry : lvcustomer.getItems()) {
            boolean match = true;
            String entryText = (String) entry;
            if (!entryText.toUpperCase().contains(value)) {
                match = false;
            }
            if (match) {
                subentries.add(entryText);
            }
        }
        lvcustomer.setItems(subentries);
    }
    public void fetTBVIEW(String item_selected){
        String name="";
        for(int i=0;i<item_selected.length();i++){
            if(item_selected.charAt(i)=='|'){
                break;
            }
            else{
                name=name+item_selected.charAt(i);
            }
        }
        name=name.trim();
        System.out.println(name);
        int have=0;
        for (int i=0;i<oslistproduct.size();i++){
            if(oslistproduct.get(i).getName().equals(name)){
                have=1;
                oslistproduct.get(i).quantityProperty().set(oslistproduct.get(i).quantityProperty().get()+1);
                oslistproduct.get(i).setCostsum(oslistproduct.get(i).getCost()+oslistproduct.get(i).getCostsum());
                tvpay.getItems().set(i,oslistproduct.get(i));
                lbsummoney.setText(String.valueOf(Integer.valueOf(lbsummoney.getText())+oslistproduct.get(i).getCost()));
            }
        }
        if(have==0){
        int cost;
        String sqlselect_name="SELECT * FROM pay WHERE name = ?";
        try {
            ps=con.prepareStatement(sqlselect_name);
            ps.setString(1,name);
            rs=ps.executeQuery();
            rs.next();
            cost=rs.getInt(2);
            Product product=new Product(stt,name,cost,cost);
            product.setQuantity(1);
            oslistproduct.add(product);
            tvpay.getItems().add(product);
            lbsummoney.setText(String.valueOf(Integer.valueOf(lbsummoney.getText())+product.getCost()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
      ++stt;}
    }
    public void deleteAll(){
        try {
            String deleteall = "DELETE FROM pay2";
            ps.executeUpdate(deleteall);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void insertData(){
        deleteAll();
        try{
            String sqlinsert="INSERT INTO pay2(name,cost,quantity,costsum) VALUE(?,?,?,?)";
            ps=con.prepareStatement(sqlinsert);
            for(int i=0;i<oslistproduct.size();i++){
                ps.setString(1,oslistproduct.get(i).getName());
                ps.setInt(2,oslistproduct.get(i).getCost());
                ps.setInt(3,oslistproduct.get(i).getQuantity());
                ps.setInt(4,oslistproduct.get(i).getCostsum());
                ps.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    public void princeInvoice(MouseEvent e) throws IOException {
        if(e.getSource()==btnpay) {
            insertData();
            try {
                Map<String, Object> para = new HashMap<>();
                para.put("name", "Minh Duc");
                para.put("costsum2", lbsummoney.getText());
                para.put("moneytake", txtmoneytake.getText());
                para.put("moneygive", lbmoneygive.getText());
                JasperReport jr = JasperCompileManager.compileReport("C:\\Users\\MinhDuc\\IdeaProjects\\Appbanhang\\src\\report\\receipt.jrxml");
                JasperPrint jp = JasperFillManager.fillReport(jr, para, con);
                JasperViewer.viewReport(jp, false);
                //    OutputStream printout=new FileOutputStream(new )
                // OutputStream os=new FileOutputStream(new File("C:\\Users\\MinhDuc\\IdeaProjects\\Appbanhang\\src\\report\\report1.jrxml"));
                JasperExportManager.exportReportToPdfFile(jp, "C:\\Users\\MinhDuc\\IdeaProjects\\Appbanhang\\src\\report\\receipt2.pdf");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if(e.getSource()==btnadd){
            try{
            Stage stage=new Stage();
            Scene scene=new Scene(FXMLLoader.load(this.getClass().getResource("Add_CUSTOMER.fxml")));
            stage.setScene(scene);
            stage.show();} catch (IOException IOEx){
                IOEx.printStackTrace();
            }
        }

    }

}
