import javax.swing.*;

import Math.*;

import java.util.ArrayList;


public class SmartWindow extends JDialog {
    private JPanel contentPane;
    private JTextField textFieldRadius;
    private JTextField textFieldFinishAngle;
    private JTextField textFieldStartAngle;
    private JButton buttonSet;
    private JPanel panelMain;
    private DrawPanel drawPanel;
    private JButton buttonClear;
    private JButton buttonDelete;

    public SmartWindow() {
        setContentPane(contentPane);
        setModal(true);
        updateParameters();
        buttonSet.addActionListener(e -> {
            updateParameters();
        });
        buttonDelete.addActionListener(e ->{
            delete();
        });
        buttonClear.addActionListener(e -> {
            drawPanel.setSegments(new ArrayList<>());
            repaint();
        });
    }

    private void updateParameters() {
        SegmentOfCircle active = drawPanel.getActive();
        if (active != null) {
            active.setRadius(parseRadius(textFieldRadius.getText(), textFieldRadius));
            active.setStartAngle(parseAngle(textFieldStartAngle.getText(), textFieldStartAngle));
            active.setFinishAngle(parseAngle(textFieldFinishAngle.getText(), textFieldFinishAngle));
        }
        SegmentOfCircle curSegment = drawPanel.getCurSegment();
        curSegment.setRadius(parseRadius(textFieldRadius.getText(), textFieldRadius));
        curSegment.setStartAngle(parseAngle(textFieldStartAngle.getText(), textFieldStartAngle));
        curSegment.setFinishAngle(parseAngle(textFieldFinishAngle.getText(), textFieldFinishAngle));
        drawPanel.repaint();
    }

    private void delete() {
        SegmentOfCircle active = drawPanel.getActive();
        if (active != null) {
            drawPanel.getSegments().remove(active);
        }
        drawPanel.repaint();
    }

    private int parseAngle(String s, JTextField textField) {
        int res;
        try {
            res = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            res = 0;
            textField.setText(String.valueOf(res));
        }
        return res;
    }

    private double parseRadius(String s, JTextField textField) {
        int res;
        try {
            res = Integer.parseInt(s);
            res = res >= 0 ? res : 0;
        } catch (NumberFormatException e) {
            res = 0;
        }
        textField.setText(String.valueOf(res));
        return res;
    }

    public static void main(String[] args) {
        SmartWindow dialog = new SmartWindow();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
