import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class ThreadSacola extends Thread {
	private JLabel sacoLixo;
	private JLabel pah;
	private JPanel contentPane;
	private int vidas = 3;
	private boolean pontuou = false;
	private boolean threadLixoAtiva = true;
	private JLabel labelVidas;
	private JFrame frame;
	private Rectangle saco;
	private Rectangle recPah;
	private JLabel labelPts;
	private JLabel labelDerrota;
	int pontuacao = 0;

	public ThreadSacola(JLabel sacoLixo, JLabel labelPah, JPanel contentPane, JLabel labelVidas, JFrame frame,
			JLabel labelPts, JLabel labelDerrota) {
		super();
		this.sacoLixo = sacoLixo;
		this.pah = labelPah;
		this.contentPane = contentPane;
		this.labelVidas = labelVidas;
		this.frame = frame;
		this.labelPts = labelPts;
		this.labelDerrota = labelDerrota;
	}

	@Override
	public void run() {
		try {
			System.out.println("ThreadLixo funcionando...");
			while (threadLixoAtiva) {
				System.out.println("While threadLixoAtiva funcionando...");

				criarSacolaLixo();
				while (sacoLixo.getY() < contentPane.getHeight()) {
					definirPosicaoJogo();
					if (recPah.intersects(saco)) {
						System.out.println("Entrou: Lixo x " + sacoLixo.getX() + " Pa X " + pah.getX());
						removerSacolaImpacto();
						criarExplosaoImpacto();
						setarPontos();
					} else {
						pontuou = false;
					}
					if (sacoLixo.getY() >= contentPane.getHeight()) {
						removerSaco();
					}
					if (!pontuou && sacoLixo.getY() > contentPane.getHeight()) {
						diminuirVidas();
					}
					if (vidas == 2) {
						definirDerrota();
					}
					Thread.sleep(400);
					contentPane.repaint();
				}
				// Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void definirDerrota() {
		labelDerrota.setVisible(true);
		// this.threadLixoAtiva = false;
		// pauseSacola = true;
		this.suspend();
	}

	private void diminuirVidas() {
		vidas -= 1;
		labelVidas.setText("Vidas: " + vidas);
	}

	private void removerSaco() {
		contentPane.remove(sacoLixo);
		contentPane.validate();
		contentPane.repaint();
	}

	private void setarPontos() {
		pontuacao += 10;
		labelPts.setText("Pontos: " + pontuacao);
		contentPane.validate();
		contentPane.repaint();
		pontuou = true;
	}

	private void criarExplosaoImpacto() throws InterruptedException {
		JLabel explosao = new JLabel();
		explosao.setIcon(new ImageIcon("src/explosao.png"));
		explosao.setBounds(pah.getX(), pah.getY(), 64, 64);
		contentPane.add(explosao);
		contentPane.repaint();
		pah.repaint();
		playSong("saco-estouro.mpeg");
		Thread.sleep(200);
		contentPane.remove(explosao);
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

	private void removerSacolaImpacto() {
		contentPane.remove(sacoLixo);
		sacoLixo.setBounds(-100, contentPane.getHeight() + 100, 0, 0);
		contentPane.validate();
		contentPane.repaint();
	}

	private void definirPosicaoJogo() {
		int randomNum = 0;
		randomNum += ThreadLocalRandom.current().nextInt(-30, 30);
		System.out.println("ContentPane width: " + contentPane.getWidth() + " sacoX: " + sacoLixo.getX()
				+ " randomNumber: " + randomNum);
		if (randomNum < 0) {
			sacoLixo.setBounds(
					sacoLixo.getX() < 64 ? (sacoLixo.getX() + (randomNum * (-1))) : sacoLixo.getX() + randomNum,
					(sacoLixo.getY() + 20), 64, 64);
		} else {
			sacoLixo.setBounds((sacoLixo.getX() > (contentPane.getWidth() - 80) ? sacoLixo.getX() - randomNum
					: sacoLixo.getX() + randomNum), (sacoLixo.getY() + 20), 64, 64);
		}
		// velocidade da sacola caindo
		// System.out.println("Saco X :" + sacoLixo.getX() + " Pa X :" + pah.getX());
		// System.out.println("Saco Y :" + sacoLixo.getY() + " Pa Y :" + pah.getY());
		saco = new Rectangle();
		saco.setBounds(sacoLixo.getX(), sacoLixo.getY(), 64, 64);
		recPah = new Rectangle();
		recPah.setBounds(pah.getX(), pah.getY(), 64, 64);
	}

	private void criarSacolaLixo() {
		sacoLixo = new JLabel();
		sacoLixo.setIcon(new ImageIcon("src/saco-de-lixo.png"));
		sacoLixo.setBounds(contentPane.getWidth() / 2, 0, 64, 64);
		// sacoLixo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		contentPane.add(sacoLixo);
		contentPane.repaint();
	}

	public JLabel getSacoLixo() {
		return sacoLixo;
	}

	public void setSacoLixo(JLabel sacoLixo) {
		this.sacoLixo = sacoLixo;
	}

	public JLabel getPah() {
		return pah;
	}

	public void setPah(JLabel pah) {
		this.pah = pah;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}

	public boolean isPontuou() {
		return pontuou;
	}

	public void setPontuou(boolean pontuou) {
		this.pontuou = pontuou;
	}

	public boolean isThreadLixoAtiva() {
		return threadLixoAtiva;
	}

	public void setThreadLixoAtiva(boolean threadLixoAtiva) {
		this.threadLixoAtiva = threadLixoAtiva;
	}

	public JLabel getLabelVidas() {
		return labelVidas;
	}

	public void setLabelVidas(JLabel labelVidas) {
		this.labelVidas = labelVidas;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public Rectangle getSaco() {
		return saco;
	}

	public void setSaco(Rectangle saco) {
		this.saco = saco;
	}

	public Rectangle getRecPah() {
		return recPah;
	}

	public void setRecPah(Rectangle recPah) {
		this.recPah = recPah;
	}

	public JLabel getLabelPts() {
		return labelPts;
	}

	public void setLabelPts(JLabel labelPts) {
		this.labelPts = labelPts;
	}

	public JLabel getLabelDerrota() {
		return labelDerrota;
	}

	public void setLabelDerrota(JLabel labelDerrota) {
		this.labelDerrota = labelDerrota;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public void novoJogo() {
		pontuacao = 0;
		vidas = 3;
		labelVidas.setText("Vidas: " + vidas);
		labelDerrota.setVisible(false);
		labelPts.setText("Pontos: " + pontuacao);
	}

}
