import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;


public class MainUI {
    private JFrame frame;
    private JPanel rootPanel;
    private JTextField query;
    private Object[][] lastQueriedData;
    private JButton refresh;
    private JTable logTable;
    private JLabel LabelOC;
    private int oldCount;
    private JLabel LabelNC;
    private int newCount;
    private JButton clear;
    private JButton search;
    private JButton export;
    private int interrupt;
    public int isInterrupted() {
        return interrupt;
    }
    public void setOldCount(int count) {
        oldCount = count;
        LabelOC.setText(Integer.toString(oldCount));
    }
    private void setNewCount(int count) {
        newCount = count;
        LabelNC.setText(Integer.toString(newCount));
    }
    public void incrementNC() {
        setNewCount(++newCount);
    }
    private void setTable(Fetcher data) {
        class ButtonRenderer extends JButton implements TableCellRenderer {
            public ButtonRenderer() {
                setOpaque(true);
            }
            @Override
            public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
                setText("View");
                return this;
            }
        }
        class ButtonEditor extends DefaultCellEditor {
            protected JButton button;
            private String label;
            private Boolean isClicked;
            public ButtonEditor(JTextField txt) {
                super(txt);
                button = new JButton();
                button.setOpaque(true);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fireEditingStopped();
                    }
                });
            }
            @Override
            public Component getTableCellEditorComponent(JTable table, Object object, boolean isSelected, int row, int column) {
                label = (object==null) ? "" : object.toString();
                button.setText("View");
                isClicked = true;
                return button;
            }
            @Override
            public Object getCellEditorValue() {
                if(isClicked) {
                    new DetailUI(data.detailData[Integer.parseInt(label)]);
                }
                isClicked = false;
                return new String(label);
            }
            @Override
            public boolean stopCellEditing() {
                isClicked = false;
                return super.stopCellEditing();
            }
            @Override
            protected void fireEditingStopped() {
                super.fireEditingStopped();
            }
        }
        String[] header = {"Level", "Date and Time", "Source", "Event ID", "Task Category", "Details"};
        DefaultTableModel model = new DefaultTableModel(data.tableData, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 5) { return true; }
                else { return false; }
            }
        };
        lastQueriedData = data.detailData;
        logTable.setModel(model);
        logTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        logTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JTextField()));
        logTable.getTableHeader().setReorderingAllowed(false);
    }
    private void initFrame(MainUI mainUI) {
        frame = new JFrame();
        frame.setTitle("LogPad");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\guru\\Videos\\LogPad\\src\\main\\resources\\icon.png"));
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mainUI.interrupt = 1;
            }
        });
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setContentPane(mainUI.rootPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void setCountLabels(MainUI mainUI) {
        var data = new Fetcher("*");
        setOldCount(data.totalHits);
        setNewCount(0);
    }
    private void initActionListeners(MainUI mainUI) {
        query.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var data = new Fetcher(query.getText());
                setTable(data);
                clear.requestFocusInWindow();
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var data = new Fetcher(query.getText());
                setTable(data);
                clear.requestFocusInWindow();
            }
        });
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setOldCount(oldCount + newCount);
                setNewCount(0);
                setTable(new Fetcher(query.getText()));
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTable(new Fetcher(""));
                query.selectAll();
                query.replaceSelection("");
                query.requestFocusInWindow();
            }
        });
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ExportPDF(lastQueriedData);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(new JOptionPane(), "Cannot Export PDF");
                }
            }
        });
    }
    private void initUI(MainUI mainUI) {
        setCountLabels(mainUI);
        setTable(new Fetcher(""));
    }
    public void init(MainUI mainUI) {
        initUI(mainUI);
        initActionListeners(mainUI);
        initFrame(mainUI);
    }
    public MainUI() {
        query.grabFocus();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}