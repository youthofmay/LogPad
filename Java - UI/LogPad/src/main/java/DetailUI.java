import javax.swing.*;
import java.awt.*;

public class DetailUI {
    private JPanel rootPanel;
    private JLabel category;
    private JLabel computerName;
    private JLabel eventCode;
    private JLabel eventID;
    private JLabel logFile;
    private JLabel recordNumber;
    private JLabel source;
    private JLabel timeGenerated;
    private JLabel timeWritten;
    private JLabel type;
    private JLabel user;
    private JLabel message;
    private void setFields(Object[] data) {
        category.setText((String) data[1] + " (" + Integer.toString((int)data[0]) + ")");
        computerName.setText((String) data[2]);
        eventCode.setText(Integer.toString((int) data[3]));
        eventID.setText(Integer.toString((int) data[4]));
        logFile.setText((String) data[6]);
        recordNumber.setText(Integer.toString((int) data[8]));
        source.setText((String) data[9]);
        timeGenerated.setText((String) data[10]);
        timeWritten.setText((String) data[11]);
        type.setText((String) data[12] + " (" + Integer.toString((int) data[5]) + ")");
        user.setText((String) data[13]);
        message.setText((String) data[7]);
    }
    public DetailUI(Object[] data) {
        setFields(data);
        JDialog dialog = new JDialog();
        dialog.setTitle("LogPad");
        dialog.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\guru\\Videos\\LogPad\\src\\main\\resources\\icon.png"));
        dialog.setContentPane(rootPanel);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
