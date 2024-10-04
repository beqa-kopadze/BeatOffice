import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class BeatBoard
{
    JPanel mainPanel;
    ArrayList<JToggleButton> buttonList;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;

    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat",
            "Acoustic Snare", "CrashCymbal", "Hand Clap",
            "Hight Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
            "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};
    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};

    public JPanel getMainPanel() {
        if (mainPanel == null) {
            buildGUI();
        }
        return mainPanel;
    }

    public static void main(String[] args) {
        new BeatBoard().buildGUI();
    }

    public void buildGUI(){
        theFrame = new JFrame("BeatOffice");
        theFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        theFrame.setSize(498, 313);
        theFrame.setResizable(false);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        background.setBackground(Color.GRAY);

        buttonList = new ArrayList<JToggleButton>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        start.setBackground(Color.DARK_GRAY);
        start.setForeground(Color.WHITE);
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        stop.setBackground(Color.DARK_GRAY);
        stop.setForeground(Color.WHITE);
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(new MyUpTempoListener());
        upTempo.setBackground(Color.DARK_GRAY);
        upTempo.setForeground(Color.WHITE);
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Tempo Down");
        downTempo.addActionListener(new MyDownTempoListener());
        downTempo.setBackground(Color.DARK_GRAY);
        downTempo.setForeground(Color.WHITE);
        buttonBox.add(downTempo);

        JButton save = new JButton("Save");
        save.addActionListener(new MySendListener());
        save.setBackground(Color.DARK_GRAY);
        save.setForeground(Color.WHITE);
        buttonBox.add(save);

        JButton load = new JButton("Load");
        load.addActionListener(new MyReadListener());
        load.setBackground(Color.DARK_GRAY);
        load.setForeground(Color.WHITE);
        buttonBox.add(load);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for(int i = 0; i<16; i++){
            JLabel label = new JLabel(instrumentNames[i]);
            label.setForeground(Color.WHITE);
            nameBox.add(label);
        }

        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);

        theFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16,16);
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel = new JPanel(grid);
        mainPanel.setBackground(Color.GRAY);
        background.add(BorderLayout.CENTER, mainPanel);

        for (int i = 0; i < 256; i ++){
            JToggleButton b = new JToggleButton() {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(15, 15);
                }
            };
            b.setBackground(Color.DARK_GRAY);
            b.setForeground(Color.WHITE);
            b.setContentAreaFilled(false);
            b.setOpaque(true);
            b.addActionListener(e -> {
                JToggleButton button = (JToggleButton) e.getSource();
                button.setBackground(button.isSelected() ? Color.YELLOW : Color.DARK_GRAY);
            });
            buttonList.add(b);
            mainPanel.add(b);
        }

        setUpMidi();

        theFrame.setBounds(50,50,300,300);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    public void setUpMidi(){
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        }catch(Exception e){e.printStackTrace();}
    }

    public void buildTrackAndStart(){
        int[] trackList = null;

        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < 16; i++){
            trackList = new int[16];

            int key = instruments[i];

            for(int j = 0; j < 16; j++){
                JToggleButton jb = buttonList.get(j+16*i);
                if(jb.isSelected()){
                    trackList[j] = key;
                }else{
                    trackList[j] = 0;
                }
            }

            makeTrack(trackList);
            track.add(makeEvent(176,1,127,0,16));
        }

        track.add(makeEvent(192,9,1,0,15));
        try{
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        }catch(Exception e) {e.printStackTrace();}
    }

    public class MyStartListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            buildTrackAndStart();
        }
    }

    public class MyStopListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            sequencer.stop();
        }
    }

    public class MyUpTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor * 1.03));
        }
    }

    public class MyDownTempoListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float)(tempoFactor * .97));
        }
    }

    public class MySendListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(theFrame);

            boolean[] buttonState = new boolean[256];
            for(int i = 0; i < 256; i++){
                JToggleButton button = buttonList.get(i);
                buttonState[i] = button.isSelected();
            }

            try{
                FileOutputStream fileStream = new FileOutputStream(fileSave.getSelectedFile() + ".ser");
                ObjectOutputStream os = new ObjectOutputStream(fileStream);
                os.writeObject(buttonState);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public class MyReadListener implements ActionListener{
        public void actionPerformed(ActionEvent a){
            boolean[] buttonState = null;
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(theFrame);
            try{
                FileInputStream fileIn = new FileInputStream(fileOpen.getSelectedFile());
                ObjectInputStream is = new ObjectInputStream(fileIn);
                buttonState = (boolean[]) is.readObject();
                is.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }

            for(int i = 0; i < 256; i++){
                JToggleButton button = buttonList.get(i);
                button.setSelected(buttonState[i]);
                button.setBackground(buttonState[i] ? Color.YELLOW : Color.BLACK);
            }

            sequencer.stop();
            buildTrackAndStart();
        }
    }

    public void makeTrack(int[] list){
        for(int i = 0; i < 16; i++){
            int key = list[i];

            if (key != 0){
                track.add(makeEvent(144,9,key,100, i));
                track.add(makeEvent(128,9,key,100, i+1));
            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick){
        MidiEvent event = null;
        try{
            ShortMessage a = new ShortMessage();
            a.setMessage(comd,chan,one,two);
            event = new MidiEvent(a, tick);
        }catch (Exception e){e.printStackTrace();}
        return event;
    }
}