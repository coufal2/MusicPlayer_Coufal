import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class MusicUITest {
    private MusicUI musicUI;

    @BeforeEach
    public void setUp() throws LineUnavailableException {
        musicUI = new MusicUI();
    }

    @Test
    public void testLoadPlaylist() {
        // Perform the loadPlaylist() method
        musicUI.loadPlaylist();

        // Verify the playlist model contains the expected number of elements
        DefaultListModel<String> playlistModel = musicUI.playlistModel;
        assertEquals(16, playlistModel.getSize());

        // Verify that specific songs exist in the playlist model
        assertTrue(playlistModel.contains("Eine kleine Nachtmusic.wav"));
        assertTrue(playlistModel.contains("Eye of the Tiger.wav"));
        assertTrue(playlistModel.contains("Highway To Hell.wav"));
        assertTrue(playlistModel.contains("Iron Maiden - Aces High.wav"));
        assertTrue(playlistModel.contains("KABÁT - Malá dáma.wav"));
        assertTrue(playlistModel.contains("Máma táta.wav"));
        assertTrue(playlistModel.contains("Wohnout - Svaz českých bohémů.wav"));
        assertTrue(playlistModel.contains("Škwor - Síla starejch vín.wav"));
       
    }

    @Test
    public void testPlay() {
        // Perform the play() method
        musicUI.play();

        // Verify that isPlaying is set to true
        assertFalse(musicUI.isPlaying);
    }

    @Test
    public void testPause() {
        // Set up the initial state
        musicUI.isPlaying = true;
        musicUI.isPaused = false;

        // Perform the pause() method
        musicUI.pause();

        // Verify that isPaused is set to true
        assertTrue(musicUI.isPaused);
    }

    @Test
    public void testStop() {
        // Set up the initial state
        musicUI.isPlaying = true;
        musicUI.isPaused = false;

        // Perform the stop() method
        musicUI.stop();

        // Verify that isPlaying and isPaused are set to false
        assertFalse(musicUI.isPlaying);
        assertFalse(musicUI.isPaused);
    }

    @Test
    public void testRepeat() {
        // Set up the initial state
        musicUI.isRepeated = false;

        // Perform the repeat() method
        musicUI.repeat();

        // Verify that isRepeated is set to true
        assertTrue(musicUI.isRepeated);
    }

    @Test
    public void testShuffle() {
        // Set up the initial state
        DefaultListModel<String> playlistModel = new DefaultListModel<>();
        playlistModel.addElement("Eine kleine Nachtmusic.wav");
        playlistModel.addElement("Eye of the Tiger.wav");
        playlistModel.addElement("Highway To Hell.wav");
        playlistModel.addElement("Iron Mainden - Aces High.wav");
        playlistModel.addElement("KABÁT - Malá dáma.wav");
        playlistModel.addElement("Máma táta.wav");
        playlistModel.addElement("Škwor - Síla starejch vín.wav");
	  playlistModel.addElement("Wohnout - Svaz českých bohémů.wav");


        musicUI.playlist.setModel(playlistModel);

        // Perform the shuffle() method
        musicUI.shuffle();

        // Get the shuffled playlist model
        DefaultListModel<String> shuffledModel = musicUI.playlistModel;

        // Verify that the shuffled model contains the same elements as the original model
        assertEquals(8, shuffledModel.getSize());
        assertTrue(shuffledModel.contains("Eine kleine Nachtmusic.wav"));
        assertTrue(shuffledModel.contains("Eye of the Tiger.wav"));
        assertTrue(shuffledModel.contains("Highway To Hell.wav"));
        assertTrue(shuffledModel.contains("Iron Maiden - Aces High.wav"));
        assertTrue(shuffledModel.contains("KABÁT - Malá dáma.wav"));
        assertTrue(shuffledModel.contains("Máma táta.wav"));
	  assertTrue(shuffledModel.contains("Wohnout - Svaz českých bohémů.wav"));
        assertTrue(shuffledModel.contains("Škwor - Síla starejch vín.wav"));

        // Verify that the shuffled model is different from the original model
        assertNotEquals(playlistModel.getElementAt(0), shuffledModel.getElementAt(0));
        assertNotEquals(playlistModel.getElementAt(1), shuffledModel.getElementAt(1));
        assertNotEquals(playlistModel.getElementAt(2), shuffledModel.getElementAt(2));
        assertNotEquals(playlistModel.getElementAt(3), shuffledModel.getElementAt(3));
        assertNotEquals(playlistModel.getElementAt(4), shuffledModel.getElementAt(4));
        assertNotEquals(playlistModel.getElementAt(5), shuffledModel.getElementAt(5));
        assertNotEquals(playlistModel.getElementAt(6), shuffledModel.getElementAt(6));
        assertNotEquals(playlistModel.getElementAt(7), shuffledModel.getElementAt(7));

    }
}
