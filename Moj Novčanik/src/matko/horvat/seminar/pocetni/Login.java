package matko.horvat.seminar.pocetni;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;

import org.apache.commons.codec.digest.DigestUtils;

import matko.horvat.seminar.glavni.Glavni;
import matko.horvat.seminar.info.Info;
import matko.horvat.seminar.registracija.Registracija;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.*;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField username;
	private JPasswordField pass;
	
	

	// podaci za bazu
	final static String url = "jdbc:mysql://localhost:3306/";
	final static String dbName = "novcanik";
	final static String driver = "com.mysql.jdbc.Driver";
	final static String userName = "root";
	final static String password = "6669";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Spajanje na bazu

		// -----------------------------

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					
					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					konekcija.close();

					// ---------------------
					Login frame = new Login();
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
	public Login() {
		setResizable(false);
		setTitle("Moj Nov\u010Danik ");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(500, 200, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblDobrodoli = new JLabel("Dobrodo\u0161li!");
		lblDobrodoli.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDobrodoli.setBounds(158, 34, 109, 39);
		contentPane.add(lblDobrodoli);

		JLabel lblIzradioMatkoHorvat = new JLabel("Izradio: Matko Horvat");
		lblIzradioMatkoHorvat.setBounds(315, 237, 119, 14);
		contentPane.add(lblIzradioMatkoHorvat);

		JLabel label = new JLabel("2014.");
		label.setBounds(10, 237, 46, 14);
		contentPane.add(label);

		JLabel lblKorisnikoIme = new JLabel("Korisni\u010Dko ime:");
		lblKorisnikoIme.setBounds(88, 97, 91, 14);
		contentPane.add(lblKorisnikoIme);

		JLabel lblLozinka = new JLabel("Lozinka:");
		lblLozinka.setBounds(106, 122, 77, 14);
		contentPane.add(lblLozinka);
		//polje za korisnièko ime
		username = new JTextField();
		username.setBounds(181, 94, 86, 20);
		contentPane.add(username);
		username.setColumns(10);
		//polje za lozinku
		pass = new JPasswordField();
		pass.setBounds(181, 119, 86, 20);
		contentPane.add(pass);
		pass.setColumns(10);
		//gumb za prijavu
		JButton btnPrijava = new JButton("Prijava");
		btnPrijava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				char[] input = pass.getPassword();
				String sifra = new String(input);
				String hashLozinka= DigestUtils.md5Hex(sifra);
				
				if(username.getText().equals("") || sifra.equals("")){
					JOptionPane
					.showMessageDialog(null,
							"Niste ispunili sve podatke do kraja!");
				}

				
				else{
				// BAZA

				try {

					// spajanje na bazu
					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);
//provjerava podudara li se uneseno korisnièko ime i lozinka sa nekim korisnikom iz baze
					Statement upit = konekcija.createStatement();
					ResultSet rezultat = upit
							.executeQuery("SELECT ime, lozinka FROM korisnici WHERE ime='"
									+ username.getText()
									+ "' AND lozinka='"
									+  hashLozinka  + "'");
					int pomocna = 0;
					while (rezultat.next()) {
						
						dispose();
						String korisnik = username.getText();
						Glavni glavni = new Glavni(korisnik);
						glavni.glavni();
						pomocna++;
						
					}

					if (pomocna == 0) {
						JOptionPane
								.showMessageDialog(null,
										"Unijeli ste krivo ili nepostojeæe korisnièko ime ili lozinku!");

						username.setText("");
						pass.setText("");
					}
					
					konekcija.close();

					

				} catch (Exception a) {
					a.printStackTrace();
				}
				
				}

				
			}
		});
		btnPrijava.setBounds(178, 175, 89, 23);
		contentPane.add(btnPrijava);
		//gumb koji vodi do prozora za registraciju
		JButton btnNapraviRaun = new JButton("Napravi ra\u010Dun");
		btnNapraviRaun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Registracija reg = new Registracija();
				reg.registracija();

			}
		});
		btnNapraviRaun.setBounds(24, 175, 126, 23);
		contentPane.add(btnNapraviRaun);
//gumb za izlaz
		JButton btnIzlaz = new JButton("Izlaz");
		btnIzlaz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				Object[] options = { "Da", "Ne" };
				int n =	JOptionPane.showOptionDialog(null,
							"Jeste li sigurni da želite izaæi iz programa?",
							"Izlazak", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, 
																
							options, 
							options[0]); 
	//ako se stisne DA, prozor se zatvara
				if(n == JOptionPane.YES_OPTION) {
					dispose();
				}
				
				
			}
		});
		btnIzlaz.setBounds(302, 175, 89, 23);
		contentPane.add(btnIzlaz);
//gumb koji poziva info prozor u kojem se nalaze neke opæenite informacije o aplikaciji (autor, verzija...)
		JButton btnInfo = new JButton("Info");
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Info info = new Info();
				info.Informacije();
			}
		});
		btnInfo.setBounds(178, 233, 89, 23);
		contentPane.add(btnInfo);
	}
}
