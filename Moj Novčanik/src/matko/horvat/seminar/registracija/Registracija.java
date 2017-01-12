package matko.horvat.seminar.registracija;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.apache.commons.codec.digest.DigestUtils;

import matko.horvat.seminar.pocetni.Login;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Registracija extends JFrame {

	private JPanel contentPane;
	private JTextField username;
	private JPasswordField pass1;
	private JPasswordField pass2;

	// podaci za bazu
	final static String url = "jdbc:mysql://localhost:3306/";
	final static String dbName = "novcanik";
	final static String driver = "com.mysql.jdbc.Driver";
	final static String userName = "root";
	final static String password = "6669";
	
	
	/**
	 * Launch the application.
	 */
	public static void registracija() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registracija frame = new Registracija();
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
	public Registracija() {
		setTitle("Registracija");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(500, 200, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblKorisnikoIme = new JLabel("Korisni\u010Dko ime:");
		lblKorisnikoIme.setBounds(79, 65, 120, 14);
		contentPane.add(lblKorisnikoIme);

		JLabel lblLozinka = new JLabel("Lozinka:");
		lblLozinka.setBounds(79, 105, 74, 14);
		contentPane.add(lblLozinka);

		JLabel lblPonoviteLozinku = new JLabel("Ponovite lozinku:");
		lblPonoviteLozinku.setBounds(79, 149, 120, 14);
		contentPane.add(lblPonoviteLozinku);
//POLJE za unos korisnièkog imena
		username = new JTextField();
		username.setBounds(202, 62, 86, 20);
		contentPane.add(username);
		username.setColumns(10);
//polje za lozinku 1
		pass1 = new JPasswordField();
		pass1.setBounds(202, 102, 86, 20);
		contentPane.add(pass1);
		pass1.setColumns(10);
//polje za lozinku 2
		pass2 = new JPasswordField();
		pass2.setBounds(202, 146, 86, 20);
		contentPane.add(pass2);
		pass2.setColumns(10);
//gumb za registraciju
		JButton btnNapraviRaun = new JButton("Napravi ra\u010Dun");
		btnNapraviRaun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int brojac=0;
				
				char[] unos1 = pass1.getPassword();
				String sifra1 = new String(unos1);
				
				char[] unos2 = pass2.getPassword();
				String sifra2 = new String(unos2);
				
				
				
				if(username.getText().equals("")){
					JOptionPane
					.showMessageDialog(null,
							"Niste unijeli korisnièko ime!");
					
				}
				
				else if(sifra1.equals("") || sifra2.equals("")){
					JOptionPane
					.showMessageDialog(null,
							"Niste unijeli lozinku!");
				}
				
				else{
				
				try {

					// spajanje na bazu
					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					Statement upit = konekcija.createStatement();
					ResultSet rezultat = upit.executeQuery("SELECT ime FROM korisnici WHERE ime='"
									+ username.getText()+"'"); 
					
					while(rezultat.next()){
																	
						brojac++;
					}
							
							konekcija.close();
					
				

				} catch (Exception a) {
					a.printStackTrace();
				}
				
				
				
				if(brojac==1){
					JOptionPane
					.showMessageDialog(null,
							"Unijeli ste korisnièko ime koje je zauzeto!\n   Molimo izaberite neko drugo");
					dispose();
					Registracija frame = new Registracija();
					frame.setVisible(true);
				}
				
				
				else{
					
				if(sifra1.equals(sifra2)){
					//baza
					
					try {

						// spajanje na bazu
						Class.forName(driver).newInstance();
						Connection konekcija = DriverManager.getConnection(url
								+ dbName, userName, password);

						Statement upit = konekcija.createStatement();																					//kriptirana lozinka se unosi u bazu
							int rez = upit.executeUpdate("INSERT INTO korisnici (ime, lozinka, vrijemeRegistracije) VALUES ('"+username.getText()+"','"+ DigestUtils.md5Hex( sifra1 ) +"',NOW());");
						if(rez==1) {
							JOptionPane
							.showMessageDialog(null,
									"Registracija uspješno izvršena!\n\n"
									+ "Vaši podaci:\n"
									+ "Korisnièko ime: "+username.getText());

						}
										dispose();
										Login ponovo = new Login();
										ponovo.main(null);
						konekcija.close();

						// ---------------------

					} catch (Exception a) {
						a.printStackTrace();
					}

					
					
				 
					
					//--------------------
				}
				else JOptionPane
				.showMessageDialog(null,
						"Niste unijeli istu lozinku!");
				pass1.setText("");
				pass2.setText("");
				
				}
			}
			}
		});
		btnNapraviRaun.setBounds(69, 201, 130, 23);
		contentPane.add(btnNapraviRaun);

		JButton btnOdustani = new JButton("Odustani");
		btnOdustani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// JOptionPane.showConfirmDialog(null,
				// "Jeste li sigurni da želite odustati?", "Odustajete?",
				// JOptionPane.YES_NO_OPTION);

				Object[] options = { "Da", "Ne" };
			int n =	JOptionPane.showOptionDialog(null,
						"Jeste li sigurni da želite odustati?",
						"Prekid registracije", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, 
															
						options, 
						options[0]); 
//ako se stisne DA, prozor se zatvara
			if(n == JOptionPane.YES_OPTION) {
				dispose();
	Login login = new Login();
	login.main(null);
}
			
			
			//ako se stisne NE, ostaje se na tom prozoru sa kojeg se pozvala potvrda odustajanja
			if(n == JOptionPane.NO_OPTION) {
				
			}
			
			}
		});
		btnOdustani.setBounds(247, 201, 89, 23);
		contentPane.add(btnOdustani);

		JLabel lblMolimoUnesiteeljene = new JLabel(
				"Molimo unesite \u017Eeljene podatke");
		lblMolimoUnesiteeljene.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblMolimoUnesiteeljene.setBounds(79, 11, 272, 30);
		contentPane.add(lblMolimoUnesiteeljene);
	}

}
