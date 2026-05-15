package org.modelo;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UITheme {

    public static final Color BG_PRIMARY   = new Color(10, 22, 45);
    public static final Color BG_SECONDARY = new Color(16, 30, 58);
    public static final Color BG_CARD      = new Color(22, 40, 72);
    public static final Color BG_HEADER    = new Color(6, 12, 26);

    public static final Color ACCENT_GOLD  = new Color(201, 168, 76);
    public static final Color ACCENT_DIM   = new Color(130, 108, 48);

    public static final Color TEXT_PRIMARY   = Color.WHITE;
    public static final Color TEXT_SECONDARY = new Color(160, 174, 192);

    public static final Color BTN_NAV      = new Color(18, 38, 72);
    public static final Color BTN_PRIMARY  = new Color(201, 168, 76);
    public static final Color BTN_DANGER   = new Color(185, 28, 28);

    // Stadium section colors
    public static final Color SECTION_AVAILABLE = new Color(20, 83, 45);
    public static final Color SECTION_FEW       = new Color(120, 53, 15);
    public static final Color SECTION_FULL      = new Color(127, 29, 29);
    public static final Color CAMPO_BG          = new Color(21, 128, 61);

    public static void setup() {
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 6);
        UIManager.put("TextComponent.arc", 6);

        try {
            FlatDarkLaf.setup();

            UIManager.put("Panel.background", BG_PRIMARY);
            UIManager.put("Label.foreground", TEXT_PRIMARY);
            UIManager.put("Button.background", BTN_NAV);
            UIManager.put("Button.foreground", TEXT_PRIMARY);
            UIManager.put("TextField.background", BG_CARD);
            UIManager.put("TextField.foreground", TEXT_PRIMARY);
            UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
            UIManager.put("Spinner.background", BG_CARD);
            UIManager.put("RadioButton.foreground", TEXT_PRIMARY);
            UIManager.put("CheckBox.foreground", TEXT_PRIMARY);
            UIManager.put("PopupMenu.background", BG_SECONDARY);
            UIManager.put("MenuItem.background", BG_SECONDARY);
            UIManager.put("MenuItem.foreground", TEXT_PRIMARY);
            UIManager.put("MenuItem.selectionBackground", ACCENT_GOLD);
            UIManager.put("MenuItem.selectionForeground", Color.BLACK);
            UIManager.put("OptionPane.background", BG_SECONDARY);
            UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
            UIManager.put("List.background", BG_CARD);
            UIManager.put("List.foreground", TEXT_PRIMARY);
            UIManager.put("ScrollPane.background", BG_PRIMARY);
            UIManager.put("Viewport.background", BG_PRIMARY);
            UIManager.put("TabbedPane.background", BG_SECONDARY);
            UIManager.put("TabbedPane.foreground", TEXT_PRIMARY);
            UIManager.put("TabbedPane.selectedBackground", BG_CARD);
            UIManager.put("TabbedPane.underlineColor", ACCENT_GOLD);
        } catch (Exception e) {
            // fallback to system L&F
        }
    }

    /** Recursively sets backgrounds of all JPanel descendants to BG_PRIMARY and
     *  styles labels, text fields, spinners, checkboxes and radio buttons. */
    public static void applyTheme(Container root) {
        applyTheme(root, BG_PRIMARY);
    }

    private static void applyTheme(Container root, Color bg) {
        if (root instanceof JPanel) {
            root.setBackground(bg);
        }
        for (Component c : root.getComponents()) {
            if (c instanceof JLabel lbl) {
                lbl.setForeground(TEXT_PRIMARY);
            } else if (c instanceof JTextField tf) {
                tf.setBackground(BG_CARD);
                tf.setForeground(TEXT_PRIMARY);
                tf.setCaretColor(TEXT_PRIMARY);
            } else if (c instanceof JSpinner sp) {
                sp.setBackground(BG_CARD);
                sp.setForeground(TEXT_PRIMARY);
            } else if (c instanceof JRadioButton rb) {
                rb.setForeground(TEXT_PRIMARY);
                rb.setOpaque(false);
            } else if (c instanceof JCheckBox cb) {
                cb.setForeground(TEXT_PRIMARY);
                cb.setOpaque(false);
            } else if (c instanceof JList<?> list) {
                list.setBackground(BG_CARD);
                list.setForeground(TEXT_PRIMARY);
            } else if (c instanceof JButton btn) {
                styleNavButton(btn);
            } else if (c instanceof JPanel pnl) {
                // Skip fixed tiny panels (e.g. 10×10 legend color squares)
                Dimension max = pnl.getMaximumSize();
                if (max.width > 20 || max.height > 20) {
                    pnl.setBackground(bg);
                }
                applyTheme(pnl, bg);
            } else if (c instanceof JScrollPane sp2) {
                sp2.setBackground(bg);
                sp2.getViewport().setBackground(BG_CARD);
                applyTheme(sp2, bg);
            }
        }
    }

    /** Recursively sets background of all JPanel descendants to the given color. */
    public static void setBackground(Container root, Color color) {
        if (root instanceof JPanel) {
            root.setBackground(color);
        }
        for (Component c : root.getComponents()) {
            if (c instanceof Container sub && !(c instanceof JButton)
                    && !(c instanceof JTextField) && !(c instanceof JSpinner)) {
                setBackground(sub, color);
            }
        }
    }

    /** Styles a panel as the top navigation bar with a gold bottom border. */
    public static void styleHeader(JPanel header) {
        header.setBackground(BG_HEADER);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_GOLD));
        for (Component c : header.getComponents()) {
            if (c instanceof JLabel lbl) {
                lbl.setForeground(ACCENT_GOLD);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 15f));
            } else if (c instanceof JButton btn) {
                styleNavButton(btn);
            }
        }
    }

    public static void styleNavButton(JButton btn) {
        btn.setBackground(BTN_NAV);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 11f));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_DIM, 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void stylePrimaryButton(JButton btn) {
        btn.setBackground(BTN_PRIMARY);
        btn.setForeground(Color.BLACK);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 13f));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void styleDangerButton(JButton btn) {
        btn.setBackground(BTN_DANGER);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 13f));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void styleSecondaryButton(JButton btn) {
        btn.setBackground(BTN_NAV);
        btn.setForeground(TEXT_SECONDARY);
        btn.setFont(btn.getFont().deriveFont(Font.PLAIN, 13f));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_DIM, 1),
            BorderFactory.createEmptyBorder(5, 14, 5, 14)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void styleTitleLabel(JLabel lbl) {
        lbl.setForeground(ACCENT_GOLD);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 15f));
    }

    /** A card-style panel border with a thin gold accent line on the left. */
    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, ACCENT_GOLD),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }

    /** A subtle rounded box border for section panels. */
    public static Border sectionBorder(Color lineColor) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(lineColor, 2),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        );
    }
}
