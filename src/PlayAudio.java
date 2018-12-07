import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlayAudio {
	private Clip clip;
	
	public void doPlay(final String soundFile) {
		try {
//			stopPlay();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(getClass().getResourceAsStream(soundFile));
			clip = AudioSystem.getClip();
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {
			stopPlay();
			System.err.println(e.getMessage());
		}
	}
	
	public void stopPlay(){
		if (clip != null) {
			clip.stop();
			clip.close();
			clip = null;
		}
	}
}
