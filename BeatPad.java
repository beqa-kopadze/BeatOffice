import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BeatPad {
    JPanel mainPanel;
    ArrayList<ColorChangeButton> buttonList;
    Synthesizer synth;
    MidiChannel channel;
    JFrame theFrame;

    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public JPanel getMainPanel() {
        if (mainPanel == null) {
            buildGUI();
        }
        return mainPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BeatPad().buildGUI());
    }

    public void buildGUI() {
        theFrame = new JFrame("BeatOffice");
        theFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        theFrame.setSize(800, 800);
        theFrame.setResizable(false);

        JPanel background = new JPanel(new BorderLayout());
        background.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        background.setBackground(Color.DARK_GRAY);

        buttonList = new ArrayList<>();

        theFrame.getContentPane().add(background);

        mainPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        mainPanel.setBackground(Color.DARK_GRAY);
        background.add(BorderLayout.CENTER, mainPanel);

        for (int i = 0; i < 16; i++) {
            ColorChangeButton b = new ColorChangeButton(instruments[i]);
            buttonList.add(b);
            mainPanel.add(b);
        }

        setUpMidi();

        theFrame.setLocationRelativeTo(null);
        theFrame.setVisible(true);
    }

    public void setUpMidi() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            channel = synth.getChannels()[9];
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void playSound(int note) {
        channel.noteOn(note, 100);
    }

    class ColorChangeButton extends JButton {
        private final Color BUTTON_COLOR = Color.BLACK;
        private final Color PRESSED_COLOR = Color.YELLOW;
        private final int note;

        public ColorChangeButton(int note) {
            this.note = note;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setBackground(PRESSED_COLOR);
                    playSound(note);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    setBackground(BUTTON_COLOR);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isPressed()) {
                g.setColor(PRESSED_COLOR);
            } else {
                g.setColor(BUTTON_COLOR);
            }
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            super.paintComponent(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(100, 100);
        }
    }
}