import javax.swing.*;

public class ChooseDirectory {
    public String directory;
    public ChooseDirectory() {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setDialogTitle("Select Directory");
        folderChooser.setAcceptAllFileFilterUsed(false);
        if(folderChooser.showDialog(folderChooser, "Select") == JFileChooser.APPROVE_OPTION) {
            directory = folderChooser.getSelectedFile().toString();
        }
    }
}
