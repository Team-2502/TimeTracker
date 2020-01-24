package com.team2502.timetracker.app;

import com.team2502.timetracker.internal.JsonData;

import javax.swing.*;
import java.io.IOException;

@SuppressWarnings("All")
public class App extends JPanel {

    private final JsonData data;
    private final JFrame frame;

    public App(JsonData data) {
        this.data = data;
        this.frame = new JFrame("TimeTracker");
        initUI();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(this);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void initUI() {
        JComboBox<String> dropDown = new JComboBox<>(data.getUsers());
        add(dropDown);

        JButton createUser = new JButton("New User");
        createUser.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Please input Name and Last Initial");
            data.createUser(name);
            dropDown.addItem(name);
            dropDown.setSelectedIndex(dropDown.getItemCount()-1);
        });
        add(createUser);

        JButton login = new JButton();
        login.setText(data.userIsLoggedIn((String)dropDown.getSelectedItem()) ? "Logout" : "Login");
        login.addActionListener(e -> {
            data.toggleUserLogin((String)dropDown.getSelectedItem());
            login.setText(data.userIsLoggedIn((String)dropDown.getSelectedItem()) ? "Logout" : "Login");
            try {data.store();} catch(IOException f) {
                f.printStackTrace();
            }
        });
        add(login);

        dropDown.addActionListener(e -> {
            login.setText(data.userIsLoggedIn((String)dropDown.getSelectedItem()) ? "Logout" : "Login");
        });

        JButton recalc = new JButton("recalculate times");
        recalc.addActionListener(e -> data.recalculateTotalTimes());
        add(recalc);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void destroy() {
        frame.dispose();
    }
}