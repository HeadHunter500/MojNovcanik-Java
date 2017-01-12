package matko.horvat.seminar.info;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Info extends JFrame {
//Neke informacije o aplikaciji i slièno...
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void Informacije() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Info frame = new Info();
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
	public Info() {
		setTitle("Moj Nov\u010Danik - Informacije");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(500, 200, 390, 309);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblImeAplikacijeMoj = new JLabel("Ime aplikacije: Moj Nov\u010Danik");
		lblImeAplikacijeMoj.setBounds(108, 30, 212, 14);
		contentPane.add(lblImeAplikacijeMoj);
		
		JLabel lblIzradioMatkoHorvat = new JLabel("Izradio: Matko Horvat");
		lblIzradioMatkoHorvat.setBounds(118, 54, 128, 14);
		contentPane.add(lblIzradioMatkoHorvat);
		
		JLabel lblAplikacijamojNovanik = new JLabel("<html>Aplikacija 'Moj Novèanik' je desktop aplikacija izraðena u programskom jeziku Java."
				+ "Svrha ove aplikacije je voðenje vlastitog budžeta kako bi imali detaljan uvid u svoje troškove.</html>");
		lblAplikacijamojNovanik.setHorizontalAlignment(SwingConstants.CENTER);
		lblAplikacijamojNovanik.setBounds(20, 90, 333, 91);
		contentPane.add(lblAplikacijamojNovanik);
		
		JButton btnZatvori = new JButton("Zatvori");
		btnZatvori.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnZatvori.setBounds(144, 213, 89, 23);
		contentPane.add(btnZatvori);
	}
}
