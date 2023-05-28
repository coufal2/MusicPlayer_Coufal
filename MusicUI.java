import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MusicUI extends JFrame implements ActionListener{


    Clip clip = AudioSystem.getClip();
    boolean isPaused;
    boolean isPlaying;
    boolean isRepeated;
    JList<String> playlist;
    DefaultListModel<String> playlistModel;
    String currentSongPath;
    private Thread playbackThread;


    MusicUI() throws LineUnavailableException {

        JFrame frame = new JFrame("Music Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Upper panel
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout());

        // Add upper components
        JLabel titleLabel = new JLabel("Music Player");
        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton stopButton = new JButton("Stop");
        upperPanel.add(titleLabel);
        upperPanel.add(playButton);
        upperPanel.add(pauseButton);
        upperPanel.add(stopButton);
        frame.add(upperPanel, BorderLayout.NORTH);

        // Playlist panel
        JPanel playlistPanel = new JPanel();
        playlistPanel.setLayout(new GridLayout(0, 1));

        // Add playlist items
        // Create the playlist model
        playlistModel = new DefaultListModel<>();
        loadPlaylist();

        // Create the playlist JList with the model
        playlist = new JList<>(playlistModel);
        JScrollPane playlistScrollPane = new JScrollPane(playlist);
        playlistPanel.add(playlistScrollPane);
        frame.add(playlistPanel, BorderLayout.WEST);


        // Player controls panel
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new FlowLayout());

        // Add player controls
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(1);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int volume = volumeSlider.getValue();
                System.out.println("Volume: " + volume);
                // Update music volume or perform any other action
            }
        });


        JButton shuffleButton = new JButton("Shuffle");
        JButton repeatButton = new JButton("Repeat");
        controlsPanel.add(volumeSlider);
        controlsPanel.add(shuffleButton);
        controlsPanel.add(repeatButton);
        frame.add(controlsPanel, BorderLayout.SOUTH);



        // Visualization panel
        JPanel visualizationPanel = new JPanel();
        visualizationPanel.setBackground(Color.BLACK);

        // Add visualization components
        frame.add(visualizationPanel, BorderLayout.CENTER);

        // Album art panel
        JPanel albumArtPanel = new JPanel();
        albumArtPanel.setLayout(new BorderLayout());

        // Add album art component
        JLabel albumArtLabel = new JLabel(new ImageIcon("idk.png"));
        albumArtLabel.setHorizontalAlignment(SwingConstants.CENTER);
        albumArtPanel.add(albumArtLabel, BorderLayout.CENTER);
        frame.add(albumArtPanel, BorderLayout.EAST);

        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        stopButton.addActionListener(this);
        shuffleButton.addActionListener(this);
        repeatButton.addActionListener(this);

        frame.setVisible(true);



    }

    void loadPlaylist() {

        String directoryPath = "C:\\Users\\tomco\\eclipse\\java-2021-09\\eclipse\\IT\\Druhák\\Music Player\\src\\Songs\\";

        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));

        if (files != null) {
            for (File file : files) {
                playlistModel.addElement(file.getName());
            }
        }
    }

    public void play() {
        // Check if a song is already playing
        if (isPlaying) {
            stop();
        }

        String selectedSong = playlist.getSelectedValue();
        if (selectedSong != null) {
            String audioFilePath = "C:\\Users\\tomco\\eclipse\\java-2021-09\\eclipse\\IT\\Druhák\\Music Player\\src\\Songs\\" + selectedSong;
            File audioFile = new File(audioFilePath);

            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                AudioFormat format = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                currentSongPath = audioFilePath;

                playbackThread = new Thread(() -> {
                    try {
                        byte[] buffer = new byte[4096];
                        int bytesRead;

                        while ((bytesRead = audioStream.read(buffer)) != -1) {
                            if (currentSongPath.equals(audioFilePath) && !isPaused) {
                                line.write(buffer,0 , bytesRead);
                            } else {
                                break;
                            }
                        }

                        line.drain();
                        line.stop();
                        line.close();
                        audioStream.close();

                        if (isPlaying && !isPaused) {
                            // Song playback has finished
                            System.out.println("Song finished: " + selectedSong);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                isPlaying = true;
                isPaused = false;
                playbackThread.start();

                System.out.println("Playing music: " + selectedSong);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("Error playing music: " + e.getMessage());
            }
        } else {
            System.out.println("No song selected.");
        }
    }

    public void pause() {
        if (isPlaying && !isPaused) {
            clip.stop();
            isPaused = true;
            System.out.println("Music paused.");
        }
    }




    public void stop() {
        if (isPlaying) {
            clip.stop();
            clip.close();
            isPlaying = false;
            isPaused = false;
            System.out.println("Music stopped.");
        }
    }

    public void repeat() {
        isRepeated = !isRepeated;
        clip.loop(isRepeated ? Clip.LOOP_CONTINUOUSLY : 0);
        System.out.println("Looping: " + isRepeated);
    }
    public void shuffle() {

        // Get the elements from the playlist model
        ArrayList<String> songs = new ArrayList<>();
        for (int i = 0; i < playlistModel.getSize(); i++) {
            songs.add(playlistModel.getElementAt(i));
        }

        // Shuffle the songs
        Collections.shuffle(songs);

        // Clear the playlist model
        playlistModel.clear();

        // Add the shuffled songs back to the playlist model
        for (String song : songs) {
            playlistModel.addElement(song);
        }

        System.out.println("Shuffling playlist...");

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Play" -> play();
            case "Pause" -> pause();
            case "Stop" -> stop();
            case "Shuffle" -> shuffle();
            case "Repeat" -> repeat();
        }

    }
}








