import swingdates.JDateChooser;

/**
 * MainApp
 */
public class MainApp {

    public static void main(String[] args) {
        JDateChooser d = new JDateChooser(dt->{
            System.out.println(dt);
        });
        d.setVisible(true);
    }
}