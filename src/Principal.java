import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Principal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JLabel label;
	JLabel sacoLixo;
	JLabel labelPts;
	int pontuacao = 0;
	ThreadSacola threadSacola;
	boolean pontuou = false;
	int vidas = 3;
	private JLabel labelDerrota;
	static Principal frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Principal() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				String songPa = "pa.mp3";
				if (e.getKeyCode() == 37) {
					if (label.getX() > 10) {
						label.setBounds((label.getX() - 50), (contentPane.getHeight() - 65), 64, 64);
						// System.out.println("37 position x " + label.getX());
						playSong("pa.mp3");
					}
				}

				if (e.getKeyCode() == 39) {
					// System.out.println("tamanho pane " + contentPane.getWidth());
					if (label.getX() <= (contentPane.getWidth() - 64)) {
						System.out.println(contentPane.getWidth());
						label.setBounds((label.getX() + 55), (contentPane.getHeight() - 65), 64, 64);
						// System.out.println("37 position x " + label.getX());
						playSong("pa.mp3");
					}
				}
				if (e.getKeyCode() == 67) {
					if (threadSacola.isThreadLixoAtiva()) {
						threadSacola.setThreadLixoAtiva(false);
						threadSacola.suspend();
					} else {
						threadSacola.setThreadLixoAtiva(true);
						threadSacola.resume();
					}
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 823, 575);
		contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Image img = Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("wallpaper.png"));
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			}

		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		label = new JLabel();
		label.setBounds(307, 476, 40, 40);
		label.setIcon(new ImageIcon("src/pa.png"));

		JPanel panel = new JPanel();
		panel.setBounds(704, 72, 93, 34);

		labelPts = new JLabel("Pontos: ");
		labelPts.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(labelPts)
					.addGap(42))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(labelPts)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(704, 26, 95, 36);

		JLabel labelVidas = new JLabel("Vidas: 3");
		labelVidas.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(labelVidas)
					.addGap(43))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap(11, Short.MAX_VALUE)
					.addComponent(labelVidas)
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);

		labelDerrota = new JLabel("Voc\u00EA perdeu! :(");
		labelDerrota.setBounds(261, 230, 295, 48);
		labelDerrota.setFont(new Font("Tahoma", Font.BOLD, 18));
		labelDerrota.setHorizontalAlignment(SwingConstants.CENTER);

		contentPane.setBorder(new LineBorder(Color.BLACK, 2));

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(40, 40, 41, 22);

		JMenu mnNewMenu = new JMenu("Jogar");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Novo jogo");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				threadSacola.novoJogo();
				threadSacola.resume();

			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		labelDerrota.setVisible(false);
		threadSacola = new ThreadSacola(sacoLixo, label, contentPane, labelVidas, frame, labelPts, labelDerrota);
		contentPane.setLayout(null);
		contentPane.add(menuBar);
		contentPane.add(panel_1);
		contentPane.add(panel);
		contentPane.add(label);
		contentPane.add(labelDerrota);
		threadSacola.setThreadLixoAtiva(true);
		threadSacola.start();
		playSong("song.mp3");

	}

	private void playSong(String pathSong) {
		new Thread(() -> {
			String path = pathSong;
			try {
				FileInputStream fileInputStream = new FileInputStream(path);
				Player player = new Player(fileInputStream);
				player.play();
			
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (JavaLayerException e1) {
				e1.printStackTrace();
			}
		}).start();
	}
}
