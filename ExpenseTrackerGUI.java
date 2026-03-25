import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class ExpenseTrackerGUI extends JFrame {
    private ExpenseManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JTextField txtAmount, txtDate, txtDesc, txtSearch;
    private JComboBox<String> cbCategory;
    private JLabel lblTotal, lblCount, lblTopCategory;

    public ExpenseTrackerGUI() {
        manager = new ExpenseManager();
        manager.setExpenses(FileHandler.loadFromFile());

        setTitle("Personal Expense Tracker Pro");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new BorderLayout(15, 15));

        // --- COLORS ---
        Color primaryBlue = new Color(41, 128, 185);
        Color successGreen = new Color(39, 174, 96);
        Color dangerRed = new Color(192, 57, 43);

        // --- DASHBOARD PANEL ---
        JPanel dashboard = new JPanel(new GridLayout(1, 3, 20, 0));
        dashboard.setBackground(primaryBlue);
        dashboard.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        lblTotal = createDashLabel("Total Spending: 0.00");
        lblCount = createDashLabel("Total Entries: 0");
        lblTopCategory = createDashLabel("Top Category: N/A");

        dashboard.add(lblTotal);
        dashboard.add(lblCount);
        dashboard.add(lblTopCategory);
        add(dashboard, BorderLayout.NORTH);

        // --- MAIN CENTER CONTENT ---
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 1. LEFT FORM PANEL
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setPreferredSize(new Dimension(350, 0));
        formWrapper.setOpaque(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Anchor to top-left

        // Form Fields
        txtAmount = new JTextField();
        String[] categories = {"Food", "Transport", "Rent", "Shopping", "Entertainment", "Health", "Other"};
        cbCategory = new JComboBox<>(categories);
        txtDate = new JTextField(java.time.LocalDate.now().toString());
        txtDesc = new JTextField();

        addLabel(formPanel, "Amount :", gbc, 0);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(txtAmount, gbc);

        addLabel(formPanel, "Category:", gbc, 1);
        gbc.gridx = 1;
        formPanel.add(cbCategory, gbc);

        addLabel(formPanel, "Date:", gbc, 2);
        gbc.gridx = 1;
        formPanel.add(txtDate, gbc);

        addLabel(formPanel, "Description:", gbc, 3);
        gbc.gridx = 1;
        formPanel.add(txtDesc, gbc);

        // Button Group inside Form
        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        btnGrid.setOpaque(false);
        JButton btnAdd = createStyledButton("Add", successGreen);
        JButton btnUpdate = createStyledButton("Update", primaryBlue);
        JButton btnDelete = createStyledButton("Delete", dangerRed);
        JButton btnClear = createStyledButton("Clear", Color.DARK_GRAY);

        btnGrid.add(btnAdd);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnDelete);
        btnGrid.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.weighty = 1.0; // Pushes everything above it to the top
        formPanel.add(btnGrid, gbc);

        formWrapper.add(formPanel, BorderLayout.NORTH);
        mainContent.add(formWrapper, BorderLayout.WEST);

        // 2. RIGHT TABLE PANEL
        tableModel = new DefaultTableModel(new String[]{"ID", "Amount", "Category", "Date", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainContent.add(scrollPane, BorderLayout.CENTER);

        add(mainContent, BorderLayout.CENTER);

        // --- BOTTOM SEARCH BAR ---
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        bottomBar.setOpaque(false);

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchBox.setOpaque(false);
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search Description");
        JButton btnExport = new JButton("Export to CSV");

        searchBox.add(new JLabel("Quick Search:"));
        searchBox.add(txtSearch);
        searchBox.add(btnSearch);
        searchBox.add(btnExport);

        bottomBar.add(searchBox, BorderLayout.WEST);
        add(bottomBar, BorderLayout.SOUTH);

        // --- LOGIC / EVENTS ---
        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> handleSearch());
        btnExport.addActionListener(e -> {
            FileHandler.saveToFile(manager.getAllExpenses());
            JOptionPane.showMessageDialog(this, "Data exported to expenses.csv");
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtAmount.setText(table.getValueAt(row, 1).toString());
                cbCategory.setSelectedItem(table.getValueAt(row, 2).toString());
                txtDate.setText(table.getValueAt(row, 3).toString());
                txtDesc.setText(table.getValueAt(row, 4).toString());
            }
        });

        refreshUI();
    }

    private void addLabel(JPanel p, String text, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        p.add(new JLabel(text), gbc);
    }

    private JLabel createDashLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        return label;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Fix for macOS rendering:
        b.setContentAreaFilled(true); 
        return b;
    }

    private void handleAdd() {
        try {
            double amt = Double.parseDouble(txtAmount.getText());
            String cat = cbCategory.getSelectedItem().toString();
            String date = txtDate.getText();
            String desc = txtDesc.getText();
            if (desc.isEmpty()) throw new Exception("Description required");
            manager.addExpense(amt, cat, date, desc);
            saveAndRefresh();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void handleUpdate() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) table.getValueAt(row, 0);
        manager.updateExpense(id, Double.parseDouble(txtAmount.getText()), 
            cbCategory.getSelectedItem().toString(), txtDate.getText(), txtDesc.getText());
        saveAndRefresh();
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) table.getValueAt(row, 0);
        manager.deleteExpense(id);
        saveAndRefresh();
        clearFields();
    }

    private void handleSearch() {
        populateTable(manager.search(txtSearch.getText()));
    }

    private void saveAndRefresh() {
        FileHandler.saveToFile(manager.getAllExpenses());
        refreshUI();
    }

    private void refreshUI() {
        populateTable(manager.getAllExpenses());
        lblTotal.setText(String.format("Total Spending: %.2f", manager.getTotalExpenses()));
        lblCount.setText("Total Entries: " + manager.getAllExpenses().size());
        lblTopCategory.setText("Top Category: " + manager.getTopCategory());
    }

    private void populateTable(List<Expense> list) {
        tableModel.setRowCount(0);
        for (Expense e : list) {
            tableModel.addRow(new Object[]{e.getId(), e.getAmount(), e.getCategory(), e.getDate(), e.getDescription()});
        }
    }

    private void clearFields() {
        txtAmount.setText("");
        txtDesc.setText("");
        txtDate.setText(java.time.LocalDate.now().toString());
        table.clearSelection();
    }

    public static void main(String[] args) {
        // Try to use System Look and Feel
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new ExpenseTrackerGUI().setVisible(true));
    }
}