package matko.horvat.seminar.glavni;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GradientPaint;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import matko.horvat.seminar.pocetni.Login;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.jdbc.JDBCCategoryDataset;

import javax.swing.JScrollBar;

import java.awt.FlowLayout;

import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;

import java.awt.SystemColor;

import javax.swing.DropMode;
import javax.swing.JTextPane;

import java.awt.GridLayout;

public class Glavni extends JFrame {

	private JPanel contentPane;
	private static String username;
	private JTextField uplataIznos;

	private float uplata1;
	private float isplata1;
	private float iznos;

	private float iznosEUR;
	int EUR = 0;
	private float iznosUSD;
	int USD = 0;

	// format iznosa da bude na dvije decimale

	DecimalFormat mojformat = new DecimalFormat("#.##");

	private float iznosU;
	private float iznosI;

	// formatira datum u prikaz dd.MM.yyyy
	Date datum1 = new Date();
	SimpleDateFormat datumF = new SimpleDateFormat("E dd.MM.yyyy");
	String datum = datumF.format(datum1);

	// podaci za spajanje na bazu
	final static String url = "jdbc:mysql://localhost:3306/";
	final static String dbName = "novcanik";
	final static String driver = "com.mysql.jdbc.Driver";
	final static String userName = "root";
	final static String password = "6669";

	private JTextField isplataIznos;
	private JTextField uplataNaziv;
	private JTextField isplataNaziv;

	private JTable tablicaUplata;
	private JTable tablicaIsplata;

	int ukupanBrojUplata;
	int ukupanBrojIsplata;

	public Glavni(String username) {
		this.username = username;
		pokreni();
	}

	/**
	 * Launch the application.
	 */
	public static void glavni() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					Glavni frame = new Glavni(username);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @return
	 */
	public void pokreni() {

		setResizable(false);
		setBackground(Color.WHITE);
		setTitle("Moj Novèanik - " + username);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(500, 200, 666, 416);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTabbedPane tabovi = new JTabbedPane(JTabbedPane.LEFT);
		tabovi.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabovi.setBounds(5, 5, 645, 372);
		contentPane.add(tabovi);

		// tab
		// NOVCANIK-----------------------------------------------------------------------
		JPanel novcanik = new JPanel();
		tabovi.addTab("Novèanik", null, novcanik, null);
		novcanik.setLayout(null);

		JLabel lblStanjeNovcanika = new JLabel("Stanje nov\u010Danika: ");
		lblStanjeNovcanika.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblStanjeNovcanika.setBounds(132, 66, 246, 43);
		novcanik.add(lblStanjeNovcanika);

		// BAZA-------------------------------

		try {

			Class.forName(driver).newInstance();
			Connection konekcija = DriverManager.getConnection(url + dbName,
					userName, password);

			// provjerava da li postoji korisnik u bazi uplata, ako ne, stavlja poèetni
			// iznos na 0
			Statement upit = konekcija.createStatement();
			ResultSet rezultat = upit
					.executeQuery("SELECT COUNT(korisnik) FROM uplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultat.next();
			String broj = rezultat.getString(1);
			int ukBroj = (int) Double.parseDouble(broj);

			if (ukBroj == 0) {
				iznosU = 0;
			}

			else {
				// ukupno za uplate
				Statement upitU = konekcija.createStatement();
				ResultSet rezultatU = upitU
						.executeQuery("SELECT SUM(iznos) FROM uplata WHERE korisnik LIKE '"
								+ username + "'");

				rezultatU.next();
				String sumU = rezultatU.getString(1);
				iznosU = (float) Double.parseDouble(sumU);

			}

			// provjerava dali postoji korisnik u bazi isplata, ako ne stavlja isplate
			// na 0
			Statement upit1 = konekcija.createStatement();
			ResultSet rezultat1 = upit
					.executeQuery("SELECT COUNT(korisnik) FROM isplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultat1.next();
			String broj1 = rezultat1.getString(1);
			int ukBroj1 = (int) Double.parseDouble(broj1);

			if (ukBroj1 == 0) {
				iznosI = 0;
			}
			// ukupno za isplate
			else {
				Statement upitI = konekcija.createStatement();
				ResultSet rezultatI = upitI
						.executeQuery("SELECT SUM(iznos) FROM isplata WHERE korisnik LIKE '"
								+ username + "'");

				rezultatI.next();
				String sumI = rezultatI.getString(1);
				iznosI = (float) Double.parseDouble(sumI);

			}

			// ukupno stanje u novèaniku

			iznos = iznosU - iznosI;

			konekcija.close();

		} catch (Exception a) {
			a.printStackTrace();

		}

		// -----------------------------------

		final JLabel lblIznos = new JLabel(mojformat.format(iznos) + " kn"); // formatira
																				// na
																				// 2
																				// decimale
		lblIznos.setHorizontalAlignment(SwingConstants.CENTER);
		if (iznos < 0) {
			lblIznos.setForeground(Color.RED);// ispis je crvene boje ako iznos
												// ode u minus
		} else
			lblIznos.setForeground(Color.BLACK);
		lblIznos.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblIznos.setBounds(142, 120, 236, 65);
		novcanik.add(lblIznos);

		final JLabel lblDatum = new JLabel(datum);
		lblDatum.setHorizontalAlignment(SwingConstants.CENTER);
		lblDatum.setBounds(413, 11, 89, 14);
		novcanik.add(lblDatum);

		// PRETVARANJE U DRUGE VALUTE----------------------------
		// EURO------------------------------
		JButton btnEuro = new JButton("Euro");
		btnEuro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				iznosEUR = (float) (iznos * 0.131800);

				if (iznos != iznosEUR) {

					if (iznos < 0) {
						lblIznos.setForeground(Color.RED);
					}

					else
						lblIznos.setForeground(Color.BLACK);

					lblIznos.setText(mojformat.format(iznosEUR) + " €");
					EUR++;
					USD = 0;
				}
			}
		});
		btnEuro.setBounds(43, 233, 89, 23);
		novcanik.add(btnEuro);
		// DOLAR-----------------------------------
		JButton btnDolar = new JButton("Dolar");
		btnDolar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				iznosUSD = (float) (iznos * 0.179685);

				if (iznos != iznosUSD) {

					if (iznos < 0) {
						lblIznos.setForeground(Color.RED);
					}

					else
						lblIznos.setForeground(Color.BLACK);

					lblIznos.setText(mojformat.format(iznosUSD) + " $");
					USD++;
					EUR = 0;
				}

			}
		});
		btnDolar.setBounds(207, 233, 89, 23);
		novcanik.add(btnDolar);
		// KUNA-------------------------------------------------
		JButton btnKuna = new JButton("Kuna");
		btnKuna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (EUR == 1) {
					iznos = (float) (iznosEUR * 7.58725);

					if (iznos < 0) {
						lblIznos.setForeground(Color.RED);
					}

					else
						lblIznos.setForeground(Color.BLACK);

					lblIznos.setText(mojformat.format(iznos) + " kn");
					USD = 0;
					EUR = 0;
				}

				else if (USD == 1) {
					iznos = (float) (iznosUSD * 5.56530);

					if (iznos < 0) {
						lblIznos.setForeground(Color.RED);
					}

					else
						lblIznos.setForeground(Color.BLACK);

					lblIznos.setText(mojformat.format(iznos) + " kn");
					USD = 0;
					EUR = 0;
				}

			}
		});
		btnKuna.setBounds(373, 233, 89, 23);
		novcanik.add(btnKuna);

		// tab UPLATA---------------------------------------------------
		final JPanel uplata = new JPanel();
		tabovi.addTab("Uplata", null, uplata, null);
		uplata.setLayout(null);

		JLabel lblIznosUplata = new JLabel(
				"Iznos koji \u017Eelite dodati u svoj nov\u010Danik:");
		lblIznosUplata.setHorizontalAlignment(SwingConstants.CENTER);
		lblIznosUplata.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblIznosUplata.setBounds(103, 93, 306, 53);
		uplata.add(lblIznosUplata);

		uplataIznos = new JTextField();
		uplataIznos.setBounds(212, 157, 86, 20);
		uplata.add(uplataIznos);
		uplataIznos.setColumns(10);

		JButton btnUplati = new JButton("Uplati");
		btnUplati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// BAZA-------------------------------

				try {

					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					// provjerava da li su sva polja ispunjena

					if (uplataNaziv.getText().equals("")
							&& uplataIznos.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Niste unijeli niti jedan podatak!");

					}

					else if (uplataNaziv.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Niste napisali naziv svoje uplate!");

					}

					else if (uplataIznos.getText().equals("")) {

						try {
							Double.parseDouble(uplataIznos.getText());
						} catch (NumberFormatException e) {

							JOptionPane.showMessageDialog(null,
									"Niste napisali iznos!");
						}

					}

					// provjerava pomoæu regularnih izraza da li iznos sadrži
					// slova
					else if (uplataIznos.getText().matches(
							"[a-zA-Z]*[0-9]*[a-zA-Z]+[0-9]*")) {

						try {
							Integer.parseInt(uplataIznos.getText());
						} catch (NumberFormatException e) {

							JOptionPane.showMessageDialog(null,
									"Iznos se mora sastojati samo od brojeva!");
							uplataIznos.setText("");
						}
					}

					else {

						uplata1 = (float) Double.parseDouble(uplataIznos
								.getText());

						// stavlja uplatu u bazu podataka
						Statement upit = konekcija.createStatement();
						int rezultat = upit
								.executeUpdate("INSERT INTO uplata (korisnik, nazivUplate, iznos, vrijemeUplate) VALUES ('"
										+ username
										+ "','"
										+ uplataNaziv.getText()
										+ "',"
										+ uplata1 + ",NOW())");

						if (rezultat == 1) {
							JOptionPane.showMessageDialog(null, "Uplatili ste "
									+ mojformat.format(uplata1)
									+ " kn u svoj novèanik!");
							uplataIznos.setText("");
							uplataNaziv.setText("");

						}

						konekcija.close();

					}

				} catch (Exception a) {
					a.printStackTrace();

				}

				// -----------------------------------

				// sitnice za prikaza stanja na tabu Novèanik
				iznos = iznos + uplata1;
				// iznos je crvene boje ako ode u minus
				if (iznos < 0) {
					lblIznos.setForeground(Color.RED);
				}

				else
					lblIznos.setForeground(Color.BLACK);

				lblIznos.setText(mojformat.format(iznos) + " kn");

			}
		});
		btnUplati.setBounds(212, 231, 89, 23);
		uplata.add(btnUplati);

		JLabel lblNazivUplate = new JLabel(
				"Napi\u0161ite naziv va\u0161e uplate:");
		lblNazivUplate.setHorizontalAlignment(SwingConstants.CENTER);
		lblNazivUplate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNazivUplate.setBounds(103, 11, 306, 53);
		uplata.add(lblNazivUplate);

		uplataNaziv = new JTextField();
		uplataNaziv.setColumns(10);
		uplataNaziv.setBounds(182, 62, 152, 20);
		uplata.add(uplataNaziv);

		// tab
		// ISPLATA------------------------------------------------------------------------------------
		JPanel isplata = new JPanel();
		tabovi.addTab("Isplata", null, isplata, null);
		isplata.setLayout(null);

		JLabel lblIznosIsplate = new JLabel(
				"Iznos koji \u017Eelite maknuti iz svoga nov\u010Danika:");
		lblIznosIsplate.setHorizontalAlignment(SwingConstants.CENTER);
		lblIznosIsplate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblIznosIsplate.setBounds(103, 93, 306, 53);
		isplata.add(lblIznosIsplate);

		isplataIznos = new JTextField();
		isplataIznos.setColumns(10);
		isplataIznos.setBounds(212, 157, 86, 20);
		isplata.add(isplataIznos);

		JButton btnIsplati = new JButton("Isplati");
		btnIsplati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// BAZA-------------------------------

				try {

					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					// provjerava da li su sva polja ispunjena
					if (isplataNaziv.getText().equals("")
							&& isplataIznos.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Niste unijeli niti jedan podatak!");

					}

					else if (isplataNaziv.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Niste napisali naziv svoje isplate!");

					}

					else if (isplataIznos.getText().equals("")) {

						try {
							Double.parseDouble(isplataIznos.getText());
						} catch (NumberFormatException i) {

							JOptionPane.showMessageDialog(null,
									"Niste napisali iznos!");
						}

					}

					// provjerava pomoæu regularnih izraza da li iznos sadrži
					// slova
					else if (isplataIznos.getText().matches(
							"[a-zA-Z]*[0-9]*[a-zA-Z]+[0-9]*")) {

						try {
							Integer.parseInt(isplataIznos.getText());
						} catch (NumberFormatException i) {

							JOptionPane.showMessageDialog(null,
									"Iznos se mora sastojati samo od brojeva!");
							isplataIznos.setText("");
						}
					}

					else {

						isplata1 = (float) Double.parseDouble(isplataIznos
								.getText());

						// stavlja isplatu u bazu
						Statement upit = konekcija.createStatement();
						int rezultat = upit
								.executeUpdate("INSERT INTO isplata (korisnik, nazivIsplate, iznos, vrijemeIsplate) VALUES ('"
										+ username
										+ "','"
										+ isplataNaziv.getText()
										+ "',"
										+ isplata1 + ",NOW())"); 
																	
																	

						if (rezultat == 1) {
							JOptionPane.showMessageDialog(
									null,
									"Isplatili ste "
											+ mojformat.format(isplata1)
											+ " kn iz svoga novèanika!");
							isplataIznos.setText("");
							isplataNaziv.setText("");
						}

						konekcija.close();

					}

				} catch (Exception a) {
					a.printStackTrace();

				}

				// -----------------------------------

				// sitnice za prikaza stanja na tabu Novèanik
				iznos = iznos - isplata1;
				// pokazuje poruku ako iznos ode u minus i odmah ga ispisuje
				// crvenim slovima
				if (iznos < 0) {
					lblIznos.setForeground(Color.RED);
					JOptionPane.showMessageDialog(null,
							"PAŽNJA!\nOtišli ste u minus!");
				}

				else
					lblIznos.setForeground(Color.BLACK);

				lblIznos.setText(mojformat.format(iznos) + " kn");

			}
		});
		btnIsplati.setBounds(212, 231, 89, 23);
		isplata.add(btnIsplati);

		isplataNaziv = new JTextField();
		isplataNaziv.setColumns(10);
		isplataNaziv.setBounds(182, 62, 152, 20);
		isplata.add(isplataNaziv);

		JLabel lblNazivIsplate = new JLabel(
				"Napi\u0161ite naziv va\u0161e isplate:");
		lblNazivIsplate.setHorizontalAlignment(SwingConstants.CENTER);
		lblNazivIsplate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNazivIsplate.setBounds(103, 11, 306, 53);
		isplata.add(lblNazivIsplate);

		// --------------------------------------------------------------------------------------

		// tab PREGLED - zadnje uplate i isplate

		JPanel pregled = new JPanel();
		tabovi.addTab("Zadnje transakcije", null, pregled, null);
		pregled.setLayout(null);

		JLabel lblZadnjaUplata = new JLabel("Zadnja uplata:");
		lblZadnjaUplata.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblZadnjaUplata.setBounds(38, 11, 126, 25);
		pregled.add(lblZadnjaUplata);

		JLabel lblZadnjaIsplata = new JLabel("Zadnja isplata:");
		lblZadnjaIsplata.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblZadnjaIsplata.setBounds(353, 13, 119, 20);
		pregled.add(lblZadnjaIsplata);

		JLabel lblNazivUPLATA = new JLabel("Naziv:");
		lblNazivUPLATA.setForeground(Color.BLUE);
		lblNazivUPLATA.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNazivUPLATA.setBounds(38, 83, 46, 14);
		pregled.add(lblNazivUPLATA);

		JLabel lblIznosUPLATA = new JLabel("Iznos:");
		lblIznosUPLATA.setForeground(Color.BLUE);
		lblIznosUPLATA.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblIznosUPLATA.setBounds(38, 157, 46, 14);
		pregled.add(lblIznosUPLATA);

		JLabel lblDatumUPLATA = new JLabel("Datum:");
		lblDatumUPLATA.setForeground(Color.BLUE);
		lblDatumUPLATA.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDatumUPLATA.setBounds(38, 225, 46, 14);
		pregled.add(lblDatumUPLATA);

		JLabel lblNazivISPLATA = new JLabel("Naziv:");
		lblNazivISPLATA.setForeground(Color.BLUE);
		lblNazivISPLATA.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNazivISPLATA.setBounds(342, 83, 46, 14);
		pregled.add(lblNazivISPLATA);

		JLabel lblIznosISPLATA = new JLabel("Iznos:");
		lblIznosISPLATA.setForeground(Color.BLUE);
		lblIznosISPLATA.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblIznosISPLATA.setBounds(342, 157, 46, 14);
		pregled.add(lblIznosISPLATA);

		JLabel lblDatumISPLATA = new JLabel("Datum:");
		lblDatumISPLATA.setForeground(Color.BLUE);
		lblDatumISPLATA.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDatumISPLATA.setBounds(342, 225, 46, 14);
		pregled.add(lblDatumISPLATA);

		final JLabel lblRezNazivUPLATA = new JLabel("Nema podataka");
		lblRezNazivUPLATA.setBounds(38, 111, 108, 14);
		pregled.add(lblRezNazivUPLATA);

		final JLabel lblRezIznosUPLATA = new JLabel("Nema podataka");
		lblRezIznosUPLATA.setBounds(38, 182, 108, 14);
		pregled.add(lblRezIznosUPLATA);

		final JLabel lblRezDatumUPLATA = new JLabel("Nema podataka");
		lblRezDatumUPLATA.setBounds(38, 250, 108, 14);
		pregled.add(lblRezDatumUPLATA);

		final JLabel lblRezNazivISPLATA = new JLabel("Nema podataka");
		lblRezNazivISPLATA.setBounds(342, 111, 130, 14);
		pregled.add(lblRezNazivISPLATA);

		final JLabel lblRezIznosISPLATA = new JLabel("Nema podataka");
		lblRezIznosISPLATA.setBounds(342, 182, 119, 14);
		pregled.add(lblRezIznosISPLATA);

		final JLabel lblRezDatumISPLATA = new JLabel("Nema podataka");
		lblRezDatumISPLATA.setBounds(342, 250, 130, 14);
		pregled.add(lblRezDatumISPLATA);

		try {

			// spajanje na bazu
			Class.forName(driver).newInstance();
			Connection konekcija = DriverManager.getConnection(url + dbName,
					userName, password);

			// ZADNJA UPLATA
			Statement upit = konekcija.createStatement();
			ResultSet rezultat1 = upit
					.executeQuery("SELECT nazivUplate, iznos, DATE_FORMAT(DATE(vrijemeUplate),'%d%.%m%.%Y')  FROM uplata WHERE korisnik LIKE '"
							+ username
							+ "' ORDER BY vrijemeUplate DESC LIMIT 0,1;");
			rezultat1.next();

			String nazivUplate = rezultat1.getString(1);
			String iznosUplate = rezultat1.getString(2);
			String datumUplate = rezultat1.getString(3);

			lblRezNazivUPLATA.setText(nazivUplate);
			lblRezIznosUPLATA.setText(iznosUplate + " kn");
			lblRezDatumUPLATA.setText(datumUplate);

			// ZADNJA ISPLATA

			ResultSet rezultatNazivIsplate = upit
					.executeQuery("SELECT nazivISplate, iznos, DATE_FORMAT(DATE(vrijemeIsplate),'%d%.%m%.%Y') FROM isplata WHERE korisnik LIKE '"
							+ username
							+ "' ORDER BY vrijemeIsplate DESC LIMIT 0,1;");
			rezultatNazivIsplate.next();

			String nazivIsplate = rezultatNazivIsplate.getString(1);
			String iznosIsplate = rezultatNazivIsplate.getString(2);
			String datumIsplate = rezultatNazivIsplate.getString(3);

			lblRezNazivISPLATA.setText(nazivIsplate);
			lblRezIznosISPLATA.setText(iznosIsplate + " kn");
			lblRezDatumISPLATA.setText(datumIsplate);

			konekcija.close();

		} catch (Exception a) {
			a.printStackTrace();
		}

		JButton btnOsvjezi = new JButton("Osvje\u017Ei");
		btnOsvjezi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					// spajanje na bazu
					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					// ZADNJA UPLATA
					Statement upit = konekcija.createStatement();
					ResultSet rezultat1 = upit
							.executeQuery("SELECT nazivUplate, iznos, DATE_FORMAT(DATE(vrijemeUplate),'%d%.%m%.%Y')  FROM uplata WHERE korisnik LIKE '"
									+ username
									+ "' ORDER BY vrijemeUplate DESC LIMIT 0,1;");
					rezultat1.next();

					String nazivUplate = rezultat1.getString(1);
					String iznosUplate = rezultat1.getString(2);
					String datumUplate = rezultat1.getString(3);

					lblRezNazivUPLATA.setText(nazivUplate);
					lblRezIznosUPLATA.setText(iznosUplate + " kn");
					lblRezDatumUPLATA.setText(datumUplate);

					// ZADNJA ISPLATA

					ResultSet rezultatNazivIsplate = upit
							.executeQuery("SELECT nazivISplate, iznos, DATE_FORMAT(DATE(vrijemeIsplate),'%d%.%m%.%Y') FROM isplata WHERE korisnik LIKE '"
									+ username
									+ "' ORDER BY vrijemeIsplate DESC LIMIT 0,1;");
					rezultatNazivIsplate.next();

					String nazivIsplate = rezultatNazivIsplate.getString(1);
					String iznosIsplate = rezultatNazivIsplate.getString(2);
					String datumIsplate = rezultatNazivIsplate.getString(3);

					lblRezNazivISPLATA.setText(nazivIsplate);
					lblRezIznosISPLATA.setText(iznosIsplate + " kn");
					lblRezDatumISPLATA.setText(datumIsplate);

					konekcija.close();

					// ---------------------

				} catch (Exception a) {
					a.printStackTrace();
				}

			}

		});
		btnOsvjezi.setBounds(197, 294, 89, 23);
		pregled.add(btnOsvjezi);

		// tab POPIS UPLATA tablica ----------------------------------------

		JPanel uplate = new JPanel();
		tabovi.addTab("Popis Uplata", null, uplate, null);
		uplate.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane1.setToolTipText("");
		uplate.add(scrollPane1);

		int red1 = 0;
		Object[][] podaci1 = new Object[red1][3];
		String[] stupac1 = { "Naziv", "Iznos (kn)", "Datum" };

		tablicaUplata = new JTable();
		tablicaUplata.setEnabled(false);
		tablicaUplata.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Naziv", "Iznos (kn)", "Datum" }));

		tablicaUplata.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane1.setViewportView(tablicaUplata);

		JButton gumbTablicaUplata = new JButton("Osvježi");
		gumbTablicaUplata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					// spajanje na bazu
					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					Statement upit = konekcija.createStatement();

					ResultSet broj1 = upit
							.executeQuery("SELECT COUNT(iznos) FROM uplata WHERE korisnik LIKE '"
									+ username + "'");
					broj1.next();
					String ukBroj1 = broj1.getString(1);
					int ukupanBrojUplata1 = (int) Double.parseDouble(ukBroj1);

					if (ukupanBrojUplata1 - ukupanBrojUplata == 0) {
					}

					else {

						int razlika = ukupanBrojUplata1 - ukupanBrojUplata;

						ResultSet rezultat = upit
								.executeQuery("SELECT nazivUplate, iznos, DATE_FORMAT(DATE(vrijemeUplate),'%d%.%m%.%Y') FROM uplata WHERE korisnik LIKE '"
										+ username
										+ "' LIMIT "
										+ ukupanBrojUplata
										+ ","
										+ razlika
										+ ";");

						while (rezultat.next()) {

							String naziv = "";
							String iznos = "";
							String datum = "";

							naziv = rezultat.getString(1);
							iznos = rezultat.getString(2);
							datum = rezultat.getString(3);
							((DefaultTableModel) tablicaUplata.getModel())
									.addRow(new Object[] { naziv, iznos, datum, });

						}

						ukupanBrojUplata = ukupanBrojUplata + razlika;

					}

					konekcija.close();

					// ---------------------

				} catch (Exception a) {
					a.printStackTrace();
				}

			}
		});
		uplate.add(gumbTablicaUplata, BorderLayout.SOUTH);

		try {

			// spajanje na bazu
			Class.forName(driver).newInstance();
			Connection konekcija = DriverManager.getConnection(url + dbName,
					userName, password);

			Statement upit = konekcija.createStatement();

			ResultSet broj = upit
					.executeQuery("SELECT COUNT(iznos) FROM uplata WHERE korisnik LIKE '"
							+ username + "'");
			broj.next();
			String ukBroj = broj.getString(1);
			ukupanBrojUplata = (int) Double.parseDouble(ukBroj);

			ResultSet rezultat = upit
					.executeQuery("SELECT nazivUplate, iznos, DATE_FORMAT(DATE(vrijemeUplate),'%d%.%m%.%Y') FROM uplata WHERE korisnik LIKE '"
							+ username + "';");

			while (rezultat.next()) {

				String naziv = "";
				String iznos = "";
				String datum = "";

				naziv = rezultat.getString(1);
				iznos = rezultat.getString(2);
				datum = rezultat.getString(3);
				((DefaultTableModel) tablicaUplata.getModel())
						.addRow(new Object[] { naziv, iznos, datum, });

				red1++;
			}

			konekcija.close();

		} catch (Exception a) {
			a.printStackTrace();
		}

		// tab POPIS ISPLATA tablica
		// -----------------------------------------------------
		JPanel isplate = new JPanel();
		tabovi.addTab("Popis Isplata", null, isplate, null);
		isplate.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane2.setToolTipText("");
		isplate.add(scrollPane2);

		int red2 = 0;
		Object[][] podaci2 = new Object[red2][3];
		String[] stupac2 = { "Naziv", "Iznos (kn)", "Datum" };

		tablicaIsplata = new JTable();
		tablicaIsplata.setEnabled(false);
		tablicaIsplata.setModel(new DefaultTableModel(podaci2, stupac2));
		tablicaIsplata.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane2.setViewportView(tablicaIsplata);

		JButton gumbTablicaIsplata = new JButton("Osvježi");
		gumbTablicaIsplata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					// spajanje na bazu bazu
					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					Statement upit = konekcija.createStatement();

					ResultSet broj1 = upit
							.executeQuery("SELECT COUNT(iznos) FROM isplata WHERE korisnik LIKE '"
									+ username + "'");
					broj1.next();
					String ukBroj1 = broj1.getString(1);
					int ukupanBrojIsplata1 = (int) Double.parseDouble(ukBroj1);

					if (ukupanBrojIsplata1 - ukupanBrojIsplata == 0) {
					}

					else {

						int razlika = ukupanBrojIsplata1 - ukupanBrojIsplata;

						ResultSet rezultat = upit
								.executeQuery("SELECT nazivIsplate, iznos, DATE_FORMAT(DATE(vrijemeIsplate),'%d%.%m%.%Y') FROM isplata WHERE korisnik LIKE '"
										+ username
										+ "' LIMIT "
										+ ukupanBrojIsplata
										+ ","
										+ razlika
										+ ";");

						while (rezultat.next()) {

							String naziv = "";
							String iznos = "";
							String datum = "";

							naziv = rezultat.getString(1);
							iznos = rezultat.getString(2);
							datum = rezultat.getString(3);
							((DefaultTableModel) tablicaIsplata.getModel())
									.addRow(new Object[] { naziv, iznos, datum, });

						}

						ukupanBrojIsplata = ukupanBrojIsplata + razlika;

					}

					konekcija.close();

				} catch (Exception a) {
					a.printStackTrace();
				}

			}
		});
		isplate.add(gumbTablicaIsplata, BorderLayout.SOUTH);

		try {

			// spajanje na bazu
			Class.forName(driver).newInstance();
			Connection konekcija = DriverManager.getConnection(url + dbName,
					userName, password);

			Statement upit = konekcija.createStatement();

			ResultSet broj = upit
					.executeQuery("SELECT COUNT(iznos) FROM isplata WHERE korisnik LIKE '"
							+ username + "'");
			broj.next();
			String ukBroj = broj.getString(1);
			ukupanBrojIsplata = (int) Double.parseDouble(ukBroj);

			ResultSet rezultat = upit
					.executeQuery("SELECT nazivIsplate, iznos, DATE_FORMAT(DATE(vrijemeIsplate),'%d%.%m%.%Y') FROM isplata WHERE korisnik LIKE '"
							+ username + "';");

			while (rezultat.next()) {

				String naziv = "";
				String iznos = "";
				String datum = "";

				naziv = rezultat.getString(1);
				iznos = rezultat.getString(2);
				datum = rezultat.getString(3);
				((DefaultTableModel) tablicaIsplata.getModel())
						.addRow(new Object[] { naziv, iznos, datum, });

				red2++;
			}

			konekcija.close();

		} catch (Exception a) {
			a.printStackTrace();
		}

		// tab STATISTIKA - max i min vrijednosti, avg
		JPanel statistika = new JPanel();
		statistika.setForeground(Color.BLUE);
		tabovi.addTab("Statistika", null, statistika, null);
		statistika.setLayout(null);

		JLabel lblNajvecaUplata = new JLabel("Najve\u0107a uplata:");
		lblNajvecaUplata.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNajvecaUplata.setForeground(Color.BLUE);
		lblNajvecaUplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblNajvecaUplata.setBounds(45, 11, 102, 14);
		statistika.add(lblNajvecaUplata);

		JLabel lblNajmanjaUplata = new JLabel("Najmanja uplata:");
		lblNajmanjaUplata.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNajmanjaUplata.setForeground(Color.BLUE);
		lblNajmanjaUplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblNajmanjaUplata.setBounds(45, 93, 102, 14);
		statistika.add(lblNajmanjaUplata);

		JLabel lblNajvecaIsplata = new JLabel("Najve\u0107a isplata: ");
		lblNajvecaIsplata.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNajvecaIsplata.setForeground(Color.BLUE);
		lblNajvecaIsplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblNajvecaIsplata.setBounds(327, 11, 102, 14);
		statistika.add(lblNajvecaIsplata);

		JLabel lblNajmanjaIsplata = new JLabel("Najmanja isplata:");
		lblNajmanjaIsplata.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNajmanjaIsplata.setForeground(Color.BLUE);
		lblNajmanjaIsplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblNajmanjaIsplata.setBounds(322, 93, 107, 14);
		statistika.add(lblNajmanjaIsplata);

		JLabel lblProsje = new JLabel("Prosje\u010Dna uplata:");
		lblProsje.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProsje.setForeground(Color.BLUE);
		lblProsje.setHorizontalAlignment(SwingConstants.LEFT);
		lblProsje.setBounds(45, 179, 104, 14);
		statistika.add(lblProsje);

		JLabel lblProsjecnaIsplata = new JLabel("Prosje\u010Dna isplata:");
		lblProsjecnaIsplata.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProsjecnaIsplata.setForeground(Color.BLUE);
		lblProsjecnaIsplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblProsjecnaIsplata.setBounds(329, 179, 102, 14);
		statistika.add(lblProsjecnaIsplata);

		final JLabel lblMaxUplata = new JLabel("Nema podataka");
		lblMaxUplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblMaxUplata.setBounds(45, 25, 165, 57);
		statistika.add(lblMaxUplata);

		final JLabel lblMinUplata = new JLabel("Nema podataka");
		lblMinUplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblMinUplata.setBounds(45, 107, 196, 70);
		statistika.add(lblMinUplata);

		final JLabel lblAvgUplata = new JLabel("Nema podataka");
		lblAvgUplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblAvgUplata.setBounds(47, 204, 102, 14);
		statistika.add(lblAvgUplata);

		final JLabel lblMaxIsplata = new JLabel("Nema podataka");
		lblMaxIsplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblMaxIsplata.setBounds(327, 25, 211, 57);
		statistika.add(lblMaxIsplata);

		final JLabel lblMinIsplata = new JLabel("Nema podataka");
		lblMinIsplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblMinIsplata.setBounds(327, 107, 211, 70);
		statistika.add(lblMinIsplata);

		final JLabel lblAvgIsplata = new JLabel("Nema podataka");
		lblAvgIsplata.setHorizontalAlignment(SwingConstants.LEFT);
		lblAvgIsplata.setBounds(329, 204, 102, 14);
		statistika.add(lblAvgIsplata);

		final JLabel lblUkupnaUplata = new JLabel("Nema podataka");
		lblUkupnaUplata.setBounds(45, 254, 165, 14);
		statistika.add(lblUkupnaUplata);

		final JLabel lblUkupnaIsplata = new JLabel("Nema podataka");
		lblUkupnaIsplata.setBounds(327, 254, 165, 14);
		statistika.add(lblUkupnaIsplata);

		try {

			// spajanje na bazu
			Class.forName(driver).newInstance();
			Connection konekcija = DriverManager.getConnection(url + dbName,
					userName, password);

			Statement upit = konekcija.createStatement();

			// podaci za UPLATU (max, min, avg)
			// MAX-------------------------------------------------------------
			ResultSet rezultatMaxUplata = upit
					.executeQuery("SELECT MAX(iznos) FROM uplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultatMaxUplata.next();
			String maxUplata = rezultatMaxUplata.getString(1);
			float maxUplata1 = (float) Double.parseDouble(maxUplata);

			ResultSet rezultatMaxUplataNaziv = upit
					.executeQuery("SELECT nazivUplate FROM uplata WHERE korisnik LIKE '"
							+ username + "'ORDER BY iznos DESC LIMIT 0,1;");
			rezultatMaxUplataNaziv.next();
			String maxUplataNaziv = rezultatMaxUplataNaziv.getString(1);

			ResultSet rezultatMaxUplataDatum = upit
					.executeQuery("SELECT DATE_FORMAT(DATE(vrijemeUplate),'%d%.%m%.%Y') FROM uplata WHERE korisnik LIKE '"
							+ username + "'ORDER BY iznos DESC LIMIT 0,1;");
			rezultatMaxUplataDatum.next();
			String maxUplataDatum = rezultatMaxUplataDatum.getString(1);

			lblMaxUplata.setText("<html>" + maxUplataNaziv + "<br>"
					+ mojformat.format(maxUplata1) + " kn<br>" + maxUplataDatum
					+ "</html>");

			// MIN-------------------------------------------------------------------------
			ResultSet rezultatMinUplata = upit
					.executeQuery("SELECT MIN(iznos) FROM uplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultatMinUplata.next();
			String minUplata = rezultatMinUplata.getString(1);
			float minUplata1 = (float) Double.parseDouble(minUplata);

			ResultSet rezultatMinUplataNaziv = upit
					.executeQuery("SELECT nazivUplate FROM uplata WHERE korisnik LIKE '"
							+ username + "'ORDER BY iznos ASC LIMIT 0,1;");
			rezultatMinUplataNaziv.next();
			String minUplataNaziv = rezultatMinUplataNaziv.getString(1);

			ResultSet rezultatMinUplataDatum = upit
					.executeQuery("SELECT DATE_FORMAT(DATE(vrijemeUplate),'%d%.%m%.%Y') FROM uplata WHERE korisnik LIKE '"
							+ username + "'ORDER BY iznos ASC LIMIT 0,1;");
			rezultatMinUplataDatum.next();
			String minUplataDatum = rezultatMinUplataDatum.getString(1);

			lblMinUplata.setText("<html>" + minUplataNaziv + "<br>"
					+ mojformat.format(minUplata1) + " kn<br>" + minUplataDatum
					+ "</html>");

			// AVG---------------------------------------------------------------------------
			ResultSet rezultatAvgUplata = upit
					.executeQuery("SELECT AVG(iznos) FROM uplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultatAvgUplata.next();
			String avgUplata = rezultatAvgUplata.getString(1);
			float avgUplata1 = (float) Double.parseDouble(avgUplata);
			lblAvgUplata.setText(mojformat.format(avgUplata1) + " kn");

			// UKUPNA UPLATA DO SADA----------------------------------------
			ResultSet rezultatUkupnaUplata = upit
					.executeQuery("SELECT SUM(iznos) FROM uplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultatUkupnaUplata.next();
			String ukupnaUplata = rezultatUkupnaUplata.getString(1);
			float ukupnaUplata1 = (float) Double.parseDouble(ukupnaUplata);
			lblUkupnaUplata.setText(mojformat.format(ukupnaUplata1) + " kn");

			// podaci za ISPLATU (max, min, avg)
			// MAX------------------------------------------------------------------
			ResultSet rezultatMaxIsplata = upit
					.executeQuery("SELECT MAX(iznos) FROM isplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultatMaxIsplata.next();
			String maxIsplata = rezultatMaxIsplata.getString(1);
			float maxIsplata1 = (float) Double.parseDouble(maxIsplata);

			ResultSet rezultatMaxIsplataNaziv = upit
					.executeQuery("SELECT nazivIsplate FROM isplata WHERE korisnik LIKE '"
							+ username + "'ORDER BY iznos DESC LIMIT 0,1;");
			rezultatMaxIsplataNaziv.next();
			String maxIsplataNaziv = rezultatMaxIsplataNaziv.getString(1);

			ResultSet rezultatMaxIsplataDatum = upit
					.executeQuery("SELECT DATE_FORMAT(DATE(vrijemeIsplate),'%d%.%m%.%Y') FROM isplata WHERE korisnik LIKE '"
							+ username + "'ORDER BY iznos DESC LIMIT 0,1;");
			rezultatMaxIsplataDatum.next();
			String maxIsplataDatum = rezultatMaxIsplataDatum.getString(1);

			lblMaxIsplata.setText("<html>" + maxIsplataNaziv + "<br>"
					+ mojformat.format(maxIsplata1) + " kn<br>"
					+ maxIsplataDatum + "</html>");

			// MIN--------------------------------------------------------------------
			ResultSet rezultatMinIsplata = upit
					.executeQuery("SELECT MIN(iznos) FROM isplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultatMinIsplata.next();
			String minIsplata = rezultatMinIsplata.getString(1);
			float minIsplata1 = (float) Double.parseDouble(minIsplata);

			ResultSet rezultatMinIsplataNaziv = upit
					.executeQuery("SELECT nazivIsplate FROM isplata WHERE korisnik LIKE '"
							+ username + "'ORDER BY iznos ASC LIMIT 0,1;");
			rezultatMinIsplataNaziv.next();
			String minIsplataNaziv = rezultatMinIsplataNaziv.getString(1);

			ResultSet rezultatMinIsplataDatum = upit
					.executeQuery("SELECT DATE_FORMAT(DATE(vrijemeIsplate),'%d%.%m%.%Y') FROM isplata WHERE korisnik LIKE '"
							+ username + "'ORDER BY iznos ASC LIMIT 0,1;");
			rezultatMinIsplataDatum.next();
			String minIsplataDatum = rezultatMinIsplataDatum.getString(1);

			lblMinIsplata.setText("<html>" + minIsplataNaziv + "<br>"
					+ mojformat.format(minIsplata1) + " kn<br>"
					+ minIsplataDatum + "</html>");

			// AVG-------------------------------------------------------------------
			ResultSet rezultatAvgIsplata = upit
					.executeQuery("SELECT AVG(iznos) FROM isplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultatAvgIsplata.next();
			String avgIsplata = rezultatAvgIsplata.getString(1);
			float avgIsplata1 = (float) Double.parseDouble(avgIsplata);
			lblAvgIsplata.setText(mojformat.format(avgIsplata1) + " kn");

			// UKUPNA ISPLATA DO SADA----------------------------------------
			ResultSet rezultatUkupnaIsplata = upit
					.executeQuery("SELECT SUM(iznos) FROM isplata WHERE korisnik LIKE '"
							+ username + "'");
			rezultatUkupnaIsplata.next();
			String ukupnaIsplata = rezultatUkupnaIsplata.getString(1);
			float ukupnaIsplata1 = (float) Double.parseDouble(ukupnaIsplata);
			lblUkupnaIsplata.setText(mojformat.format(ukupnaIsplata1) + " kn");

			konekcija.close();

		} catch (Exception a) {
			a.printStackTrace();
		}

		JButton btnOsvjezi1 = new JButton("Osvje\u017Ei");
		btnOsvjezi1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					// spajanje na bazu
					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					Statement upit = konekcija.createStatement();

					// podaci za UPLATU (max, min, avg)
					// MAX-------------------------------------------------------------
					ResultSet rezultatMaxUplata = upit
							.executeQuery("SELECT MAX(iznos) FROM uplata WHERE korisnik LIKE '"
									+ username + "'");
					rezultatMaxUplata.next();
					String maxUplata = rezultatMaxUplata.getString(1);
					float maxUplata1 = (float) Double.parseDouble(maxUplata);

					ResultSet rezultatMaxUplataNaziv = upit
							.executeQuery("SELECT nazivUplate FROM uplata WHERE korisnik LIKE '"
									+ username
									+ "'ORDER BY iznos DESC LIMIT 0,1;");
					rezultatMaxUplataNaziv.next();
					String maxUplataNaziv = rezultatMaxUplataNaziv.getString(1);

					ResultSet rezultatMaxUplataDatum = upit
							.executeQuery("SELECT DATE_FORMAT(DATE(vrijemeUplate),'%d%.%m%.%Y') FROM uplata WHERE korisnik LIKE '"
									+ username
									+ "'ORDER BY iznos DESC LIMIT 0,1;");
					rezultatMaxUplataDatum.next();
					String maxUplataDatum = rezultatMaxUplataDatum.getString(1);

					lblMaxUplata.setText("<html>" + maxUplataNaziv + "<br>"
							+ mojformat.format(maxUplata1) + " kn<br>"
							+ maxUplataDatum + "</html>");

					// MIN-------------------------------------------------------------------------
					ResultSet rezultatMinUplata = upit
							.executeQuery("SELECT MIN(iznos) FROM uplata WHERE korisnik LIKE '"
									+ username + "'");
					rezultatMinUplata.next();
					String minUplata = rezultatMinUplata.getString(1);
					float minUplata1 = (float) Double.parseDouble(minUplata);

					ResultSet rezultatMinUplataNaziv = upit
							.executeQuery("SELECT nazivUplate FROM uplata WHERE korisnik LIKE '"
									+ username
									+ "'ORDER BY iznos ASC LIMIT 0,1;");
					rezultatMinUplataNaziv.next();
					String minUplataNaziv = rezultatMinUplataNaziv.getString(1);

					ResultSet rezultatMinUplataDatum = upit
							.executeQuery("SELECT DATE_FORMAT(DATE(vrijemeUplate),'%d%.%m%.%Y') FROM uplata WHERE korisnik LIKE '"
									+ username
									+ "'ORDER BY iznos ASC LIMIT 0,1;");
					rezultatMinUplataDatum.next();
					String minUplataDatum = rezultatMinUplataDatum.getString(1);

					lblMinUplata.setText("<html>" + minUplataNaziv + "<br>"
							+ mojformat.format(minUplata1) + " kn<br>"
							+ minUplataDatum + "</html>");

					// AVG---------------------------------------------------------------------------
					ResultSet rezultatAvgUplata = upit
							.executeQuery("SELECT AVG(iznos) FROM uplata WHERE korisnik LIKE '"
									+ username + "'");
					rezultatAvgUplata.next();
					String avgUplata = rezultatAvgUplata.getString(1);
					float avgUplata1 = (float) Double.parseDouble(avgUplata);
					lblAvgUplata.setText(mojformat.format(avgUplata1) + " kn");

					// UKUPNA UPLATA DO
					// SADA----------------------------------------
					ResultSet rezultatUkupnaUplata = upit
							.executeQuery("SELECT SUM(iznos) FROM uplata WHERE korisnik LIKE '"
									+ username + "'");
					rezultatUkupnaUplata.next();
					String ukupnaUplata = rezultatUkupnaUplata.getString(1);
					float ukupnaUplata1 = (float) Double
							.parseDouble(ukupnaUplata);
					lblUkupnaUplata.setText(mojformat.format(ukupnaUplata1)
							+ " kn");

					// podaci za ISPLATU (max, min, avg)
					// MAX------------------------------------------------------------------
					ResultSet rezultatMaxIsplata = upit
							.executeQuery("SELECT MAX(iznos) FROM isplata WHERE korisnik LIKE '"
									+ username + "'");
					rezultatMaxIsplata.next();
					String maxIsplata = rezultatMaxIsplata.getString(1);
					float maxIsplata1 = (float) Double.parseDouble(maxIsplata);

					ResultSet rezultatMaxIsplataNaziv = upit
							.executeQuery("SELECT nazivIsplate FROM isplata WHERE korisnik LIKE '"
									+ username
									+ "'ORDER BY iznos DESC LIMIT 0,1;");
					rezultatMaxIsplataNaziv.next();
					String maxIsplataNaziv = rezultatMaxIsplataNaziv
							.getString(1);

					ResultSet rezultatMaxIsplataDatum = upit
							.executeQuery("SELECT DATE_FORMAT(DATE(vrijemeIsplate),'%d%.%m%.%Y') FROM isplata WHERE korisnik LIKE '"
									+ username
									+ "'ORDER BY iznos DESC LIMIT 0,1;");
					rezultatMaxIsplataDatum.next();
					String maxIsplataDatum = rezultatMaxIsplataDatum
							.getString(1);

					lblMaxIsplata.setText("<html>" + maxIsplataNaziv + "<br>"
							+ mojformat.format(maxIsplata1) + " kn<br>"
							+ maxIsplataDatum + "</html>");

					// MIN--------------------------------------------------------------------
					ResultSet rezultatMinIsplata = upit
							.executeQuery("SELECT MIN(iznos) FROM isplata WHERE korisnik LIKE '"
									+ username + "'");
					rezultatMinIsplata.next();
					String minIsplata = rezultatMinIsplata.getString(1);
					float minIsplata1 = (float) Double.parseDouble(minIsplata);

					ResultSet rezultatMinIsplataNaziv = upit
							.executeQuery("SELECT nazivIsplate FROM isplata WHERE korisnik LIKE '"
									+ username
									+ "'ORDER BY iznos ASC LIMIT 0,1;");
					rezultatMinIsplataNaziv.next();
					String minIsplataNaziv = rezultatMinIsplataNaziv
							.getString(1);

					ResultSet rezultatMinIsplataDatum = upit
							.executeQuery("SELECT DATE_FORMAT(DATE(vrijemeIsplate),'%d%.%m%.%Y') FROM isplata WHERE korisnik LIKE '"
									+ username
									+ "'ORDER BY iznos ASC LIMIT 0,1;");
					rezultatMinIsplataDatum.next();
					String minIsplataDatum = rezultatMinIsplataDatum
							.getString(1);

					lblMinIsplata.setText("<html>" + minIsplataNaziv + "<br>"
							+ mojformat.format(minIsplata1) + " kn<br>"
							+ minIsplataDatum + "</html>");

					// AVG-------------------------------------------------------------------
					ResultSet rezultatAvgIsplata = upit
							.executeQuery("SELECT AVG(iznos) FROM isplata WHERE korisnik LIKE '"
									+ username + "'");
					rezultatAvgIsplata.next();
					String avgIsplata = rezultatAvgIsplata.getString(1);
					float avgIsplata1 = (float) Double.parseDouble(avgIsplata);
					lblAvgIsplata.setText(mojformat.format(avgIsplata1) + " kn");

					// UKUPNA ISPLATA DO
					// SADA----------------------------------------
					ResultSet rezultatUkupnaIsplata = upit
							.executeQuery("SELECT SUM(iznos) FROM isplata WHERE korisnik LIKE '"
									+ username + "'");
					rezultatUkupnaIsplata.next();
					String ukupnaIsplata = rezultatUkupnaIsplata.getString(1);
					float ukupnaIsplata1 = (float) Double
							.parseDouble(ukupnaIsplata);
					lblUkupnaIsplata.setText(mojformat.format(ukupnaIsplata1)
							+ " kn");

					konekcija.close();

					// ---------------------

				} catch (Exception a) {
					a.printStackTrace();
				}

			}
		});
		btnOsvjezi1.setBounds(189, 296, 89, 23);
		statistika.add(btnOsvjezi1);

		JLabel lblUkupnoUplaeno = new JLabel("Ukupno upla\u0107eno:");
		lblUkupnoUplaeno.setForeground(Color.BLUE);
		lblUkupnoUplaeno
				.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblUkupnoUplaeno.setBounds(45, 229, 102, 14);
		statistika.add(lblUkupnoUplaeno);

		JLabel lblNewLabel = new JLabel("Ukupno ispla\u0107eno:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setBounds(327, 229, 102, 14);
		statistika.add(lblNewLabel);

		// GRAF-----------------------------------------------------------------------------------
		final JPanel grafovi = new JPanel();
		tabovi.addTab("Grafovi", null, grafovi, null);
		grafovi.setLayout(null);

		// Linijski graf za isplate
		JButton btnIsplateLinijski = new JButton("Linijski graf");
		btnIsplateLinijski.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {

					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					String upit = ("SELECT vrijemeIsplate, iznos FROM isplata WHERE korisnik LIKE '"
							+ username + "'");

					JDBCCategoryDataset dataset = new JDBCCategoryDataset(
							konekcija, upit);

					JFreeChart chart = ChartFactory.createLineChart("Isplate",
							"Datum", "Iznos (kn)", dataset,
							PlotOrientation.VERTICAL, false, true, true);

					BarRenderer renderer = null;
					CategoryPlot plot = null;

					renderer = new BarRenderer();

					ChartFrame frame = new ChartFrame("Graf ", chart);

					frame.setVisible(true);
					frame.setSize(700, 560);

				}

				catch (Exception a) {
					a.printStackTrace();
				}

			}
		});
		btnIsplateLinijski.setBounds(118, 304, 106, 23);
		grafovi.add(btnIsplateLinijski);

		JLabel lblGrafikiPrikazSvih = new JLabel(
				"Grafi\u010Dki prikaz svih isplata ");
		lblGrafikiPrikazSvih.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblGrafikiPrikazSvih.setBounds(154, 235, 269, 23);
		grafovi.add(lblGrafikiPrikazSvih);

		// gumb za prikazivanje stupèastog grafa za prikazivanje isplata

		JButton btnIsplateStupcani = new JButton("Stup\u010Dani graf");
		btnIsplateStupcani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					String upit = ("SELECT vrijemeIsplate, iznos FROM isplata WHERE korisnik LIKE '"
							+ username + "'");

					JDBCCategoryDataset dataset = new JDBCCategoryDataset(
							konekcija, upit);

					JFreeChart chart = ChartFactory.createBarChart("Isplate",
							"Datum", "Iznos (kn)", dataset,
							PlotOrientation.VERTICAL, false, true, true);

					BarRenderer renderer = null;
					CategoryPlot plot = null;

					renderer = new BarRenderer();

					ChartFrame frame = new ChartFrame("Graf ", chart);

					frame.setVisible(true);
					frame.setSize(700, 560);

				} catch (Exception a) {
					a.printStackTrace();
				}

			}
		});
		btnIsplateStupcani.setBounds(289, 304, 115, 23);
		grafovi.add(btnIsplateStupcani);

		JLabel lblGrafickeUplate = new JLabel(
				"Grafi\u010Dki prikaz svih uplata ");
		lblGrafickeUplate.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblGrafickeUplate.setBounds(154, 40, 269, 23);
		grafovi.add(lblGrafickeUplate);

		// gumb za prikaz linijskog grafa za uplate
		JButton btnUplateLinijski = new JButton("Linijski graf");
		btnUplateLinijski.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					String upit = ("SELECT vrijemeUplate, iznos FROM uplata WHERE korisnik LIKE '"
							+ username + "'");

					JDBCCategoryDataset dataset = new JDBCCategoryDataset(
							konekcija, upit);

					JFreeChart chart = ChartFactory.createLineChart("Uplate",
							"Datum", "Iznos (kn)", dataset,
							PlotOrientation.VERTICAL, false, true, true);

					BarRenderer renderer = null;
					CategoryPlot plot = null;

					renderer = new BarRenderer();

					ChartFrame frame = new ChartFrame("Graf ", chart);

					frame.setVisible(true);
					frame.setSize(700, 560);

				}

				catch (Exception a) {
					a.printStackTrace();
				}

			}
		});
		btnUplateLinijski.setBounds(118, 112, 106, 23);
		grafovi.add(btnUplateLinijski);
		// gumb za prikazivanje stupèastog grafa za prikazivanje uplata
		JButton btnUplateStupcani = new JButton("Stup\u010Dani graf");
		btnUplateStupcani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					Class.forName(driver).newInstance();
					Connection konekcija = DriverManager.getConnection(url
							+ dbName, userName, password);

					String upit = ("SELECT vrijemeUplate, iznos FROM uplata WHERE korisnik LIKE '"
							+ username + "'");

					JDBCCategoryDataset dataset = new JDBCCategoryDataset(
							konekcija, upit);

					JFreeChart chart = ChartFactory.createBarChart("Uplate",
							"Datum", "Iznos (kn)", dataset,
							PlotOrientation.VERTICAL, false, true, true);

					BarRenderer renderer = null;
					CategoryPlot plot = null;

					renderer = new BarRenderer();

					ChartFrame frame = new ChartFrame("Graf ", chart);

					frame.setVisible(true);
					frame.setSize(700, 560);

				} catch (Exception a) {
					a.printStackTrace();
				}

			}
		});
		btnUplateStupcani.setBounds(289, 112, 115, 23);
		grafovi.add(btnUplateStupcani);

		// tab POMOÆ--------------------------------------
		JPanel pomoc = new JPanel();
		tabovi.addTab("Pomoæ", null, pomoc, null);
		pomoc.setLayout(new BorderLayout(0, 0));
		
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension( 450,1100));
		JScrollPane scrollFrame = new JScrollPane(panel);
		panel.setAutoscrolls(true);
		scrollFrame.setPreferredSize(new Dimension( 450,300));
		pomoc.add(scrollFrame);
		
		
//naslov Pomoæ
		panel.setLayout(null);
		JLabel lblPomoc = new JLabel("Pomo\u0107");
		lblPomoc.setHorizontalAlignment(SwingConstants.CENTER);
		lblPomoc.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPomoc.setBounds(236, 11, 49, 19);
		panel.add(lblPomoc);

	//opis za tab Novèanik
		JLabel lblNovcanik = new JLabel("Nov\u010Danik");
		lblNovcanik.setHorizontalAlignment(SwingConstants.CENTER);
		lblNovcanik.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblNovcanik.setBounds(10, 62, 81, 19);
		panel.add(lblNovcanik);

		JLabel lblNovcanikOPIS = new JLabel("<html>Kartica 'Nov\u010Danik' sadr\u017Ei trenutno stanje va\u0161eg nov\u010Danika koje mo\u017Eete pretvarati u \u017Eeljene valute(Euro, Dolar i natrag u Kune) pritiskom na odgovaraju\u0107i gumb. Svakim novim pokretanjem aplikacije iznos nov\u010Danika se ponovno prikazuje u Kunama.</html>");
		lblNovcanikOPIS.setVerticalAlignment(SwingConstants.TOP);
		lblNovcanikOPIS.setHorizontalAlignment(SwingConstants.CENTER);
		lblNovcanikOPIS.setBounds(127, 66, 346, 91);
		panel.add(lblNovcanikOPIS);
		
		//opis za tab Uplata
		JLabel lblUplata = new JLabel("Uplata");
		lblUplata.setHorizontalAlignment(SwingConstants.CENTER);
		lblUplata.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblUplata.setBounds(14, 176, 66, 19);
		panel.add(lblUplata);
		
		JLabel lblUplataOPIS = new JLabel("<html>Kartica 'Uplata' se koristi prilikom uplate željenog iznosa u novèanik. Potrebno je upisati naziv uplate i iznos, u protivnom se uplata neæe izvršiti.</html>");
		lblUplataOPIS.setVerticalAlignment(SwingConstants.TOP);
		lblUplataOPIS.setHorizontalAlignment(SwingConstants.CENTER);
		lblUplataOPIS.setBounds(127, 180, 346, 58);
		panel.add(lblUplataOPIS);
		
		//opis za tab Isplata
		JLabel lblIsplata = new JLabel("Isplata");
		lblIsplata.setHorizontalAlignment(SwingConstants.CENTER);
		lblIsplata.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblIsplata.setBounds(10, 293, 70, 19);
		panel.add(lblIsplata);
		
		JLabel lblIsplataOPIS = new JLabel("<html>Kartica 'Isplata' se koristi prilikom isplate \u017Eeljenog iznosa iz nov\u010Danika. Potrebno je upisati naziv isplate i iznos, u protivnom se isplata ne\u0107e izvr\u0161iti.</html>");
		lblIsplataOPIS.setVerticalAlignment(SwingConstants.TOP);
		lblIsplataOPIS.setHorizontalAlignment(SwingConstants.CENTER);
		lblIsplataOPIS.setBounds(127, 297, 346, 70);
		panel.add(lblIsplataOPIS);
		
	
		
		
		//opis za tab Zadnje transakcije
		
		JLabel lblZadnjeTransakcije = new JLabel("<html>Zadnje transakcije<html>");
		lblZadnjeTransakcije.setHorizontalAlignment(SwingConstants.CENTER);
		lblZadnjeTransakcije.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblZadnjeTransakcije.setBounds(10, 413, 100, 49);
		panel.add(lblZadnjeTransakcije);
		
		
		
		JLabel lblTransakcijeOPIS = new JLabel("<html>Na kartici 'Zadnje transakcije' se nalazi naziv, iznos i datum zadnje isplate i zadnje uplate. (Poželjno je stisnuti gumb 'Osvježi' kako bi bili sigurni da su podaci ažurirani)</html>");
		lblTransakcijeOPIS.setVerticalAlignment(SwingConstants.TOP);
		lblTransakcijeOPIS.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransakcijeOPIS.setBounds(127, 413, 346, 70);
		panel.add(lblTransakcijeOPIS);
		
		
		//opis za tab Popis Uplata
		JLabel lblPopisUplata = new JLabel("<html>Popis uplata<html>");
		lblPopisUplata.setHorizontalAlignment(SwingConstants.CENTER);
		lblPopisUplata.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblPopisUplata.setBounds(10, 533, 100, 49);
		panel.add(lblPopisUplata);
		
		
		
		JLabel lblPopisUplataOPIS = new JLabel("<html>Na kartici 'Popis Uplata' se nalazi tablica u kojoj se nalaze sve uplate koje je korisnik uplatio od poèetka svog korištenja aplikacije do sada. (Poželjno je stisnuti gumb 'Osvježi' kako bi bili sigurni da su podaci ažurirani)</html>");
		lblPopisUplataOPIS.setVerticalAlignment(SwingConstants.TOP);
		lblPopisUplataOPIS.setHorizontalAlignment(SwingConstants.CENTER);
		lblPopisUplataOPIS.setBounds(127, 533, 346, 70);
		panel.add(lblPopisUplataOPIS);
		
		//opis za tab Popis Isplata
		JLabel lblPopisIsplata = new JLabel("<html>Popis isplata<html>");
		lblPopisIsplata.setHorizontalAlignment(SwingConstants.CENTER);
		lblPopisIsplata.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblPopisIsplata.setBounds(10, 653, 100, 49);
		panel.add(lblPopisIsplata);
		
		JLabel lbllblStatistikaOPIS = new JLabel("<html>Na kartici 'Popis Isplata' se nalazi tablica u kojoj se nalaze sve isplate koje je korisnik isplatio od poèetka svog korištenja aplikacije do sada. (Poželjno je stisnuti gumb 'Osvježi' kako bi bili sigurni da su podaci ažurirani)</html>");
		lbllblStatistikaOPIS.setVerticalAlignment(SwingConstants.TOP);
		lbllblStatistikaOPIS.setHorizontalAlignment(SwingConstants.CENTER);
		lbllblStatistikaOPIS.setBounds(127, 653, 346, 70);
		panel.add(lbllblStatistikaOPIS);
		
		
		//opis za tab Statistika
		JLabel lblStatistika = new JLabel("<html>Statistika<html>");
		lblStatistika.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatistika.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblStatistika.setBounds(10, 773, 100, 49);
		panel.add(lblStatistika);
		
		JLabel lblPopisIsplataOPIS = new JLabel("<html>Na kartici 'Statistika' se nalaze podaci kao što su najveæa, najmanja i prosjeèna uplata i isplata te ukupni iznos koji je isplaæen i uplaæen do sada . (Poželjno je stisnuti gumb 'Osvježi' kako bi bili sigurni da su podaci ažurirani)</html>");
		lblPopisIsplataOPIS.setVerticalAlignment(SwingConstants.TOP);
		lblPopisIsplataOPIS.setHorizontalAlignment(SwingConstants.CENTER);
		lblPopisIsplataOPIS.setBounds(127, 773, 346, 70);
		panel.add(lblPopisIsplataOPIS);
		
		//opis za tab Grafovi
		JLabel lblGrafovi = new JLabel("<html>Grafovi<html>");
		lblGrafovi.setHorizontalAlignment(SwingConstants.CENTER);
		lblGrafovi.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblGrafovi.setBounds(10, 893, 100, 49);
		panel.add(lblGrafovi);
		
		JLabel lblGrafoviOPIS = new JLabel("<html>Na kartici 'Grafovi' se nalaze opcije grafièkog prikaza svih uplata i isplata. Moguæe je odabrati prikaz linijskim ili stupèastim grafom klikom na gumb odgovarajuæeg naziva.</html>");
		lblGrafoviOPIS.setVerticalAlignment(SwingConstants.TOP);
		lblGrafoviOPIS.setHorizontalAlignment(SwingConstants.CENTER);
		lblGrafoviOPIS.setBounds(127, 893, 346, 70);
		panel.add(lblGrafoviOPIS);
		
		
		//opis za tab Izlazak
		JLabel lblIzlazak = new JLabel("<html>Izlazak<html>");
		lblIzlazak.setHorizontalAlignment(SwingConstants.CENTER);
		lblIzlazak.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblIzlazak.setBounds(10, 1013, 100, 49);
		panel.add(lblIzlazak);
		
		JLabel lblIzlazakOPIS = new JLabel("<html>Na kartici 'Izlazak' možemo izaæi iz aplikacije ili se odjaviti sa trenutnim korisnikom i prijaviti kao drugi.</html>");
		lblIzlazakOPIS.setVerticalAlignment(SwingConstants.TOP);
		lblIzlazakOPIS.setHorizontalAlignment(SwingConstants.CENTER);
		lblIzlazakOPIS.setBounds(127, 1013, 346, 70);
		panel.add(lblIzlazakOPIS);
	
		
		
	

		
		//tab IZLAZAK-------------------------------------------------------
		JPanel izlazak = new JPanel();
		tabovi.addTab("Izlazak", null, izlazak, null);
		izlazak.setLayout(null);

		JButton btnIzlazak = new JButton("Izlazak");
		btnIzlazak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// prozor za potvrðivanje izlaska
				Object[] options = { "Da", "Ne" };
				int n = JOptionPane.showOptionDialog(null,
						"Jeste li sigurni da želite izaæi?", "Izlazak",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null,

						options, options[0]);
				// ako se stisne DA, prozor se zatvara
				if (n == JOptionPane.YES_OPTION) {
					dispose();

				}
			}
		});
		btnIzlazak.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnIzlazak.setBounds(173, 88, 150, 60);
		izlazak.add(btnIzlazak);

		JButton btnPromjeniKorisnika = new JButton("Promijeni korisnika");
		btnPromjeniKorisnika.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// prozor za potvrdu promjene korisnika
				Object[] options = { "Da", "Ne" };
				int n = JOptionPane.showOptionDialog(null,
						"Jeste li sigurni da želite promijeniti korisnika?",
						"Promjena korisnika", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null,

						options, options[0]);
				// ako se stisne DA, prozor se zatvara
				if (n == JOptionPane.YES_OPTION) {

					dispose();
					Login login = new Login();
					login.main(null);
				}

			}
		});
		btnPromjeniKorisnika.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPromjeniKorisnika.setBounds(142, 187, 218, 63);
		izlazak.add(btnPromjeniKorisnika);

		JPanel logo = new JPanel();
		logo.setBackground(Color.LIGHT_GRAY);
		logo.setForeground(Color.BLACK);
		logo.setBounds(10, 194, 92, 183);
		contentPane.add(logo);
		logo.setLayout(null);
		// putanja do logo-a
		JLabel lblLogo = new JLabel("logo-novcanik");
		lblLogo.setBounds(0, 40, 92, 97);
		lblLogo.setIcon(new ImageIcon(
				"D:\\FAKS\\4. SEMESTAR\\Objektno Java\\Seminar Moj Nov\u010Danik\\Moj Nov\u010Danik 1.0\\logo\\novcanik.png"));
		logo.add(lblLogo);

	}
}
