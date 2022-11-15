import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private DrawPanel dp;
    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        add(dp = new DrawPanel());
    }
}