import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class CalcUI extends JFrame implements ActionListener{
    public JPanel rootPanel;
    private JButton calculateButton;
    private JComboBox comboBoxFrom, comboBoxTo, comboBoxCoordTo, comboBoxCoordFrom;
    private JLabel fromBdegMainLabel, fromBdegLabel, fromBminLabel, fromBsecLabel, fromLdegLabel, fromLminLabel, fromLsecLabel, fromLdegMainLabel, XorBLabelFrom, YorLLabelFrom, ZorHLabelFrom, XorBLabelTo, YorLLabelTo, ZorHLabelTo, toBdegMainLabel, toLdegMainLabel, toBdegLabel, toBminLabel, toBsecLabel, toLdegLabel, toLminLabel, toLsecLabel;
    private JTextField fromBsecTextField, fromLsecTextField, xFieldFrom, yFieldFrom, zFieldFrom, resultLabelX, resultLabelY, resultLabelZ, toBdegTextField, toBminTextField, toBsecTextField, toLminTextField, toLsecTextField, fromBdegTextField, fromBminTextField, toLdegTextField, fromLdegTextField, fromLminTextField;
    private ConverterManager converterManager;
    private boolean inUpdate = false;

    public CalcUI() {
        converterManager = new ConverterManager();
        setupUI();
        addButtonListener();
        addComboBoxesListener();
        addBLHUpdater();
    }

    private void addButtonListener(){
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convert();
            }
        });
    }
    private void convert(){
        try {
            double x,y,z;
            if(xFieldFrom.getText().isEmpty()) x = 0; else x = Double.parseDouble(xFieldFrom.getText());
            if(yFieldFrom.getText().isEmpty()) y = 0; else y = Double.parseDouble(yFieldFrom.getText());
            if(zFieldFrom.getText().isEmpty()) z = 0; else z = Double.parseDouble(zFieldFrom.getText());
            // считываем выбранные системы для перевода
            String from = comboBoxFrom.getSelectedItem().toString();
            String to = comboBoxTo.getSelectedItem().toString();
            String fromCoord = comboBoxCoordFrom.getSelectedItem().toString();
            String toCoord = comboBoxCoordTo.getSelectedItem().toString();

            if(fromCoord.equals("B,L,H")){
                // перевести градусы в радианы
                x = x * Math.PI / 180;
                y = y * Math.PI / 180;
            }

            //переводим
            double[] coordinates = converterManager.convert(fromCoord,toCoord, x,y,z, from,to);
            // обновляем Labels
/*            if(toCoord.equals("B,L,H")){
                // переводим в градусы
                coordinates[0] = coordinates[0] * 180 / Math.PI;
                coordinates[1] = coordinates[1] * 180 / Math.PI;
            }*/
            updateCoordinateLabels(coordinates);
        }catch(Exception exception){
        }
    }
    private void addComboBoxesListener(){
        comboBoxCoordFrom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBoxCoordFrom.getSelectedItem().toString().equals("X,Y,Z")){
                    XorBLabelFrom.setText("X");
                    YorLLabelFrom.setText("Y");
                    ZorHLabelFrom.setText("Z");
                    // изменить градусы на метры
                    fromBdegMainLabel.setText("м");
                    fromLdegMainLabel.setText("м");
                    // спрятать градусы / минусы / секунды
                    fromBdegLabel.setVisible(false);
                    fromBdegTextField.setVisible(false);
                    fromBminLabel.setVisible(false);
                    fromBminTextField.setVisible(false);
                    fromBsecLabel.setVisible(false);
                    fromBsecTextField.setVisible(false);
                    fromLdegLabel.setVisible(false);
                    fromLdegTextField.setVisible(false);
                    fromLminLabel.setVisible(false);
                    fromLminTextField.setVisible(false);
                    fromLsecLabel.setVisible(false);
                    fromLsecTextField.setVisible(false);
                }
                if(comboBoxCoordFrom.getSelectedItem().toString().equals("B,L,H")){
                    XorBLabelFrom.setText("B");
                    YorLLabelFrom.setText("L");
                    ZorHLabelFrom.setText("H");
                    // изменить метры на градусы
                    fromBdegMainLabel.setText("°");
                    fromLdegMainLabel.setText("°");
                    // показать градусы / минусы / секунды
                    fromBdegLabel.setVisible(true);
                    fromBdegTextField.setVisible(true);
                    fromBminLabel.setVisible(true);
                    fromBminTextField.setVisible(true);
                    fromBsecLabel.setVisible(true);
                    fromBsecTextField.setVisible(true);
                    fromLdegLabel.setVisible(true);
                    fromLdegTextField.setVisible(true);
                    fromLminLabel.setVisible(true);
                    fromLminTextField.setVisible(true);
                    fromLsecLabel.setVisible(true);
                    fromLsecTextField.setVisible(true);
                    inUpdate = true;
                    BLHUpdate(true, "B");
                    inUpdate = true;
                    BLHUpdate(true, "L");
                }
                convert();
            }
        });
        comboBoxCoordTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBoxCoordTo.getSelectedItem().toString().equals("X,Y,Z")){
                    XorBLabelTo.setText("X");
                    YorLLabelTo.setText("Y");
                    ZorHLabelTo.setText("Z");
                    // изменить градусы на метры
                    toBdegMainLabel.setText("м");
                    toLdegMainLabel.setText("м");
                    // спрятать градусы / минусы / секунды
                    toBdegLabel.setVisible(false);
                    toBdegTextField.setVisible(false);
                    toBminLabel.setVisible(false);
                    toBminTextField.setVisible(false);
                    toBsecLabel.setVisible(false);
                    toBsecTextField.setVisible(false);
                    toLdegLabel.setVisible(false);
                    toLdegTextField.setVisible(false);
                    toLminLabel.setVisible(false);
                    toLminTextField.setVisible(false);
                    toLsecLabel.setVisible(false);
                    toLsecTextField.setVisible(false);
                }
                if(comboBoxCoordTo.getSelectedItem().toString().equals("B,L,H")){
                    XorBLabelTo.setText("B");
                    YorLLabelTo.setText("L");
                    ZorHLabelTo.setText("H");
                    // изменить метры на градусы
                    toBdegMainLabel.setText("°");
                    toLdegMainLabel.setText("°");
                    // показать градусы / минусы / секунды
                    toBdegLabel.setVisible(true);
                    toBdegTextField.setVisible(true);
                    toBminLabel.setVisible(true);
                    toBminTextField.setVisible(true);
                    toBsecLabel.setVisible(true);
                    toBsecTextField.setVisible(true);
                    toLdegLabel.setVisible(true);
                    toLdegTextField.setVisible(true);
                    toLminLabel.setVisible(true);
                    toLminTextField.setVisible(true);
                    toLsecLabel.setVisible(true);
                    toLsecTextField.setVisible(true);
                }
                convert();
            }
        });
        comboBoxFrom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convert();
            }
        });
        comboBoxTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convert();
            }
        });
    }
    private void addBLHUpdater(){
        xFieldFrom.getDocument().addDocumentListener(new DocumentListener() {                // B main deg
            public void changedUpdate(DocumentEvent e) {
                handle();
            }
            public void removeUpdate(DocumentEvent e) {
                handle();
            }
            public void insertUpdate(DocumentEvent e) {
                handle();
            }
            private void handle(){
                if(inUpdate) return;
                inUpdate = true;
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
//                        if(xFieldFrom.getText().isEmpty()) xFieldFrom.setText("0");     // если пустая строка - задать 0
                        if(comboBoxCoordFrom.getSelectedItem().toString().equals("B,L,H")) BLHUpdate(true, "B"); // если выбрано BLH, то обновить другие поля
                        else inUpdate = false;
                    }
                };
                SwingUtilities.invokeLater(update);
            }
        });
        fromBdegTextField.getDocument().addDocumentListener(new DocumentListener() {        // B deg
            public void changedUpdate(DocumentEvent e) {
                handle();
            }
            public void removeUpdate(DocumentEvent e) {
                handle();
            }
            public void insertUpdate(DocumentEvent e) {
                handle();
            }
            private void handle(){
                if(inUpdate) return;
                inUpdate = true;
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
//                        if(fromBdegTextField.getText().isEmpty()) fromBdegTextField.setText("0");     // если пустая строка - задать 0
                        if(comboBoxCoordFrom.getSelectedItem().toString().equals("B,L,H")) BLHUpdate(false, "B"); // если выбрано BLH, то обновить другие поля
                        else inUpdate = false;
                    }
                };
                SwingUtilities.invokeLater(update);
            }
        });
        fromBminTextField.getDocument().addDocumentListener(new DocumentListener() {        // B min
            public void changedUpdate(DocumentEvent e) {
                handle();
            }
            public void removeUpdate(DocumentEvent e) {
                handle();
            }
            public void insertUpdate(DocumentEvent e) {
                handle();
            }
            private void handle(){
                if(inUpdate) return;
                inUpdate = true;
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
//                        if(fromBminTextField.getText().isEmpty()) fromBminTextField.setText("0");     // если пустая строка - задать 0
                        if(comboBoxCoordFrom.getSelectedItem().toString().equals("B,L,H")) BLHUpdate(false, "B"); // если выбрано BLH, то обновить другие поля
                        else inUpdate = false;
                    }
                };
                SwingUtilities.invokeLater(update);
            }
        });
        fromBsecTextField.getDocument().addDocumentListener(new DocumentListener() {        // B sec
            public void changedUpdate(DocumentEvent e) {
                handle();
            }
            public void removeUpdate(DocumentEvent e) {
                handle();
            }
            public void insertUpdate(DocumentEvent e) {
                handle();
            }
            private void handle(){
                if(inUpdate) return;
                inUpdate = true;
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
//                        if(fromBsecTextField.getText().isEmpty()) fromBsecTextField.setText("0");     // если пустая строка - задать 0
                        if(comboBoxCoordFrom.getSelectedItem().toString().equals("B,L,H")) BLHUpdate(false, "B"); // если выбрано BLH, то обновить другие поля
                        else inUpdate = false;
                    }
                };
                SwingUtilities.invokeLater(update);
            }
        });

        yFieldFrom.getDocument().addDocumentListener(new DocumentListener() {                // L main deg
            public void changedUpdate(DocumentEvent e) {
                handle();
            }
            public void removeUpdate(DocumentEvent e) {
                handle();
            }
            public void insertUpdate(DocumentEvent e) {
                handle();
            }
            private void handle(){
                if(inUpdate) return;
                inUpdate = true;
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
//                        if(yFieldFrom.getText().isEmpty()) yFieldFrom.setText("0");     // если пустая строка - задать 0
                        if(comboBoxCoordFrom.getSelectedItem().toString().equals("B,L,H")) BLHUpdate(true, "L"); // если выбрано BLH, то обновить другие поля
                        else inUpdate = false;
                    }
                };
                SwingUtilities.invokeLater(update);
            }
        });
        fromLdegTextField.getDocument().addDocumentListener(new DocumentListener() {        // L deg
            public void changedUpdate(DocumentEvent e) {
                handle();
            }
            public void removeUpdate(DocumentEvent e) {
                handle();
            }
            public void insertUpdate(DocumentEvent e) {
                handle();
            }
            private void handle(){
                if(inUpdate) return;
                inUpdate = true;
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
//                        if(fromLdegTextField.getText().isEmpty()) fromLdegTextField.setText("0");     // если пустая строка - задать 0
                        if(comboBoxCoordFrom.getSelectedItem().toString().equals("B,L,H")) BLHUpdate(false, "L"); // если выбрано BLH, то обновить другие поля
                        else inUpdate = false;
                    }
                };
                SwingUtilities.invokeLater(update);
            }
        });
        fromLminTextField.getDocument().addDocumentListener(new DocumentListener() {        // L min
            public void changedUpdate(DocumentEvent e) {
                handle();
            }
            public void removeUpdate(DocumentEvent e) {
                handle();
            }
            public void insertUpdate(DocumentEvent e) {
                handle();
            }
            private void handle(){
                if(inUpdate) return;
                inUpdate = true;
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
//                        if(fromLminTextField.getText().isEmpty()) fromLminTextField.setText("0");     // если пустая строка - задать 0
                        if(comboBoxCoordFrom.getSelectedItem().toString().equals("B,L,H")) BLHUpdate(false, "L"); // если выбрано BLH, то обновить другие поля
                        else inUpdate = false;
                    }
                };
                SwingUtilities.invokeLater(update);
            }
        });
        fromLsecTextField.getDocument().addDocumentListener(new DocumentListener() {        // L sec
            public void changedUpdate(DocumentEvent e) {
                handle();
            }
            public void removeUpdate(DocumentEvent e) {
                handle();
            }
            public void insertUpdate(DocumentEvent e) {
               handle();
            }
            private void handle(){
                if(inUpdate) return;
                inUpdate = true;
                Runnable update = new Runnable() {
                    @Override
                    public void run() {
//                        if(fromLsecTextField.getText().isEmpty()) fromLsecTextField.setText("0");     // если пустая строка - задать 0
                        if(comboBoxCoordFrom.getSelectedItem().toString().equals("B,L,H")) BLHUpdate(false, "L"); // если выбрано BLH, то обновить другие поля
                        else inUpdate = false;
                    }
                };
                SwingUtilities.invokeLater(update);
            }
        });
    }
    private void BLHUpdate(boolean updatedMain, String updatedLabel){
        // метод вызывается при обновлении числовых значений в полях ввода данных для BLH
        // если обновили доли градусов
        try {
            if(updatedLabel.equals("B")) {
                if (updatedMain) {
                    // рассчитать градусы, минуты, секунды
                    double degMain = Double.parseDouble(xFieldFrom.getText());
                    int deg = (int) degMain;                        // градусы - целая часть
                    int min = (int) ((degMain - deg) * 60);         // минуты - дробная часть, умноженная на 60
                    int sec = (int) ((degMain - (double)deg - (double)min / 60) * 3600); // секунды
                    // обновить поля
                    fromBdegTextField.setText(String.valueOf(deg));
                    fromBminTextField.setText(String.valueOf(min));
                    fromBsecTextField.setText(String.valueOf(sec));
//                    fromBsecTextField.setText(String.valueOf(new DecimalFormat("#0.0000").format(sec)));
                } else {   // иначе если обновили градусы / минуты / секунды
                    // рассчитать доли градусов
                    double deg, min, sec;
                    if (fromBdegTextField.getText().isEmpty()) deg = 0;
                    else deg = Double.parseDouble(fromBdegTextField.getText());
                    if (fromBminTextField.getText().isEmpty()) min = 0;
                    else min = Double.parseDouble(fromBminTextField.getText());
                    if (fromBsecTextField.getText().isEmpty()) sec = 0;
                    else sec = Double.parseDouble(fromBsecTextField.getText());
                    double degMain = deg + min / 60 + sec / 3600;
                    // обновить доли градусов
//                    xFieldFrom.setText(String.valueOf(new DecimalFormat("#0.000000000").format(degMain)));
                    xFieldFrom.setText(String.valueOf(degMain));
                }
            }else if (updatedLabel.equals("L")) {
                if (updatedMain) {
                    // рассчитать градусы, минуты, секунды
                    double degMain = Double.parseDouble(yFieldFrom.getText());
                    int deg = (int) degMain;                        // градусы - целая часть
                    int min = (int) ((degMain - deg) * 60);         // минуты - дробная часть, умноженная на 60
                    int sec = (int) ((degMain - (double)deg - (double)min / 60) * 3600); // секунды
                    // обновить поля
                    fromLdegTextField.setText(String.valueOf(deg));
                    fromLminTextField.setText(String.valueOf(min));
                    fromLsecTextField.setText(String.valueOf(sec));
//                    fromLsecTextField.setText(String.valueOf(new DecimalFormat("#0.0000").format(sec)));
                } else {   // иначе если обновили градусы / минуты / секунды
                    // рассчитать доли градусов
                    double deg, min, sec;
                    if (fromLdegTextField.getText().isEmpty()) deg = 0;
                    else deg = Double.parseDouble(fromLdegTextField.getText());
                    if (fromLminTextField.getText().isEmpty()) min = 0;
                    else min = Double.parseDouble(fromLminTextField.getText());
                    if (fromLsecTextField.getText().isEmpty()) sec = 0;
                    else sec = Double.parseDouble(fromLsecTextField.getText());
                    double degMain = deg + min / 60 + sec / 3600;
                    // обновить доли градусов
//                    yFieldFrom.setText(String.valueOf(new DecimalFormat("#0.000000000").format(degMain)));
                    yFieldFrom.setText(String.valueOf(degMain));
                }
            }
        }catch (Exception e){}
        finally{
            inUpdate = false;
        }
    }
    private void setupUI(){
        // добавить системы координат в UI
        comboBoxFrom.addItem("СК-42");
        comboBoxFrom.addItem("WGS-84");
        comboBoxFrom.addItem("ПЗ-90.11");
        comboBoxFrom.addItem("СК-95");
        comboBoxFrom.addItem("ПЗ-90.02");
        comboBoxFrom.addItem("ПЗ-90");
        comboBoxFrom.addItem("ITRF-2008");
        comboBoxFrom.addItem("ГСК-2011");

        comboBoxTo.addItem("СК-42");
        comboBoxTo.addItem("ПЗ-90.11");
        comboBoxTo.addItem("WGS-84");
        comboBoxTo.addItem("СК-95");
        comboBoxTo.addItem("ПЗ-90.02");
        comboBoxTo.addItem("ПЗ-90");
        comboBoxTo.addItem("ITRF-2008");
        comboBoxTo.addItem("ГСК-2011");
        //////////////////////////////////
        comboBoxCoordFrom.addItem("X,Y,Z");
        comboBoxCoordFrom.addItem("B,L,H");
//        comboBoxCoordFrom.addItem("G,k");
        comboBoxCoordTo.addItem("X,Y,Z");
        comboBoxCoordTo.addItem("B,L,H");
//        comboBoxCoordTo.addItem("G,k");
        // скрыть лишние элементы
        if(comboBoxCoordFrom.getSelectedItem().toString().equals("X,Y,Z")){
            // изменить градусы на метры
            fromBdegMainLabel.setText("м");
            fromLdegMainLabel.setText("м");
            // спрятать градусы / минусы / секунды
            fromBdegLabel.setVisible(false);
            fromBdegTextField.setVisible(false);
            fromBminLabel.setVisible(false);
            fromBminTextField.setVisible(false);
            fromBsecLabel.setVisible(false);
            fromBsecTextField.setVisible(false);
            fromLdegLabel.setVisible(false);
            fromLdegTextField.setVisible(false);
            fromLminLabel.setVisible(false);
            fromLminTextField.setVisible(false);
            fromLsecLabel.setVisible(false);
            fromLsecTextField.setVisible(false);
        }
        if(comboBoxCoordTo.getSelectedItem().toString().equals("X,Y,Z")){
            // изменить градусы на метры
            toBdegMainLabel.setText("м");
            toLdegMainLabel.setText("м");
            // спрятать градусы / минусы / секунды
            toBdegLabel.setVisible(false);
            toBdegTextField.setVisible(false);
            toBminLabel.setVisible(false);
            toBminTextField.setVisible(false);
            toBsecLabel.setVisible(false);
            toBsecTextField.setVisible(false);
            toLdegLabel.setVisible(false);
            toLdegTextField.setVisible(false);
            toLminLabel.setVisible(false);
            toLminTextField.setVisible(false);
            toLsecLabel.setVisible(false);
            toLsecTextField.setVisible(false);
        }
    }
    private void updateCoordinateLabels(double[] coord){
        // Если перевод в BLH, то обновить градусы / минуты / секунды
        if(comboBoxCoordTo.getSelectedItem().toString().equals("B,L,H")){
            resultLabelX.setText(String.valueOf(new DecimalFormat("#0.000000000").format(coord[0])));
            resultLabelY.setText(String.valueOf(new DecimalFormat("#0.000000000").format(coord[1])));
            resultLabelZ.setText(String.valueOf(new DecimalFormat("#0.00").format(coord[2])));
            //B
            int deg = (int) coord[0];                        // градусы - целая часть
            int min = (int) ((coord[0] - deg) * 60);         // минуты - дробная часть, умноженная на 60
            double sec = (coord[0] - deg - min / 60) * 3600; // секунды
            toBdegTextField.setText(String.valueOf(deg));
            toBminTextField.setText(String.valueOf(min));
            toBsecTextField.setText(String.valueOf(new DecimalFormat("#0.0000").format(sec)));
            // L
            deg = (int) coord[1];                        // градусы - целая часть
            min = (int) ((coord[1] - deg) * 60);         // минуты - дробная часть, умноженная на 60
            sec = (coord[1] - deg - min / 60) * 3600;    // секунды
            toLdegTextField.setText(String.valueOf(deg));
            toLminTextField.setText(String.valueOf(min));
            toLsecTextField.setText(String.valueOf(new DecimalFormat("#0.0000").format(sec)));
        }else{
            resultLabelX.setText(String.valueOf(new DecimalFormat("#0.00").format(coord[0])));
            resultLabelY.setText(String.valueOf(new DecimalFormat("#0.00").format(coord[1])));
            resultLabelZ.setText(String.valueOf(new DecimalFormat("#0.00").format(coord[2])));
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // имплементация интерфейса требует реализации метода
    }
}
