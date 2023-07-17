package com.addressbook;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class AddressBookImp {
	static Scanner sc = new Scanner(System.in);
	static Scanner s1 = new Scanner(System.in);
	static List<Contacts> list = new ArrayList<>();

	public static void getAddressBook() {
		boolean n = true;

		while (n) {
			System.out.println("1. Add contact");
			System.out.println("2. Edit contact");
			System.out.println("3. Delete contact");
			System.out.println("4. Add multiple contact");
			System.out.println("5. Search contacts");
			System.out.println("6. Search persion by city or state");
			System.out.println("7. Contact count by city or state");
			System.out.println("8. Sort contact by name");
			System.out.println("9. File IO (Read/Write)");
			System.out.println("10. CSV File (Read/Write)");
			System.out.println("11. JDBC Statement select");
			System.out.println("12. Exit \n");

			System.out.print("Enter your choice : ");
			int c = sc.nextInt();
			System.out.println();

			switch (c) {

			case 1:
				addContact(list);
				break;

			case 2:
				editContact(list);
				break;

			case 3:
				deleteContact(list);
				break;

			case 4:
				addMultipleContacts(list);
				break;

			case 5:
				searchContact(list);
				break;

			case 6:
				searchByCityOrState(list);
				break;

			case 7:
				countByCityOrState(list);
				break;

			case 8:
				sortByName(list);
				break;

			case 9:
				fileIO(list);
				break;

			case 10:
				CSVFile(list);
				break;

			case 11:
				selectStatement();
				break;

			case 12:
				n = false;
				System.out.println("exit successfull...");
				break;

			default:
				System.out.println("Enter valid choice \n");

			}
		}
	}

	// UC14
	private static void selectStatement() {

		final String READ_QUERY = "select * from addressbooktable";

		Connection connection = getConnection();
		ArrayList<Contacts> addressBookDB = new ArrayList<Contacts>();

		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(READ_QUERY);
			while (result.next()) {

				addressBookDB.add(
						new Contacts(result.getString(1), result.getString(2), result.getString(3), result.getString(4),
								result.getString(5), result.getInt(6), result.getInt(7), result.getString(8)));
			}

			System.out.println("Records from db ");
			addressBookDB.forEach(contacts -> System.out.println(contacts));
			System.out.println();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static Connection getConnection() {
		final String URL = "jdbc:mysql://localhost:3306/addressbook";
		final String USER = "root";
		final String PASSWORD = "root";

		Connection connection = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USER, PASSWORD);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;

	}

	// UC13
	private static void CSVFile(List<Contacts> list) {
		System.out.println("1. Write");
		System.out.println("2. Read");
		System.out.print("Enter your choice : ");
		int c = sc.nextInt();
		switch (c) {
		case 1:
			CSVFileWrite(list);
			break;
		case 2:
			CSVFileRead();
			break;
		default:
			System.out.println("Please enter valid choice \n");

		}

	}

	private static void CSVFileRead() {

		String path = "C:\\AddressBook\\Contact.csv";
		try (CSVReader csvReader = new CSVReader(new FileReader(path))) {
			String[] header = csvReader.readNext();
			String[] file;
			while ((file = csvReader.readNext()) != null) {
				String firstname = file[0];
				String lastname = file[1];
				String address = file[2];
				String city = file[3];
				String state = file[4];
				String zip = String.valueOf(file[5]);
				String phoneno = String.valueOf(file[6]);
				String email = file[7];

				System.out.println(
						"firstname=" + firstname + " lastname=" + lastname + " address=" + address + " " + "city="
								+ city + " state=" + state + " zip=" + zip + " phoneno=" + phoneno + " email=" + email);
			}
			System.out.println();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void CSVFileWrite(List<Contacts> list) {
		String path = "C:\\AddressBook\\Contact.csv";
		try (CSVWriter csvWriter = new CSVWriter(new FileWriter(path))) {

			String[] header = { "firstname", "lastname", "address", "city", "state", "zip", "phoneno", "email" };
			csvWriter.writeNext(header);
			if (!list.isEmpty()) {

				Iterator<Contacts> l = list.iterator();
				while (l.hasNext()) {
					Contacts c = l.next();
					String[] values = { c.getFirstname(), c.getLastname(), c.getAddress(), c.getCity(), c.getState(),
							String.valueOf(c.getZip()), String.valueOf(c.getPhoneno()), c.getEmail() };
					csvWriter.writeNext(values);
				}
			}
			System.out.println("File created...\n");
		} catch (IOException e) {
			throw new RuntimeException(e);

		}

	}

	// UC12
	private static void fileIO(List<Contacts> list) {

		System.out.println("1. Write");
		System.out.println("2. Read");
		System.out.println("Enter your choice");
		int c = sc.nextInt();

		switch (c) {
		case 1:
			fileWrite(list);
			break;
		case 2:
			fileRead();
			break;
		default:
			System.out.println("Enter valid choice");
		}

	}

	private static void fileRead() {

		try {
			FileReader fileReader = new FileReader("C:\\AddressBook\\Contact.txt");
			int i;
			while ((i = fileReader.read()) != -1) {
				System.out.print((char) i);
			}
			System.out.println("\n");
			fileReader.close();

		} catch (IOException e) {
			System.out.println("error");
		}

	}

	private static void fileWrite(List<Contacts> list) {
		try {
			File filepath = new File("C:\\AddressBook\\Contact.txt");
			FileWriter filewriter = new FileWriter(filepath);
			Iterator<Contacts> l = list.iterator();
			while (l.hasNext()) {
				Contacts c = l.next();
				String s = c.toString();
				filewriter.write(s);
			}
			filewriter.close();
			System.out.println("File created...");
			System.out.println();

		} catch (IOException e) {
			System.out.println("error");
		}

	}

	// UC11
	private static void sortByName(List<Contacts> list) {

		Comparator<Contacts> cbfn = Comparator.comparing(Contacts::getFirstname);
		List<Contacts> sc = list.stream().sorted(cbfn).collect(Collectors.toList());
		sc.forEach(contacts -> System.out.println(contacts));
		System.out.println();

	}

	// UC10
	private static void countByCityOrState(List<Contacts> list) {

		System.out.println("1. City");
		System.out.println("2. State");
		System.out.print("Enter choice to search : ");

		int choice = sc.nextInt();

		switch (choice) {
		case 1:
			getCountByCity(list);
			break;

		case 2:
			getCountByState(list);
			break;

		default:
			System.out.println("Enter valid choice \n");

		}

	}

	private static void getCountByState(List<Contacts> list) {

		System.out.print("Enter state : ");
		String searchbystate = sc.next();

		long stream = list.stream().filter(l -> (l.getState().equals(searchbystate))).count();
		System.out.println(stream + "\n");

	}

	private static void getCountByCity(List<Contacts> list) {

		System.out.print("Enter city : ");
		String searchbycity = sc.next();

		long stream = list.stream().filter(l -> (l.getCity().equals(searchbycity))).count();
		System.out.println(stream + "\n");

	}

	// UC9
	private static void searchByCityOrState(List<Contacts> list) {

		System.out.println("1. City");
		System.out.println("2. State");
		System.out.print("Enter choice to search : ");

		int choice = sc.nextInt();

		switch (choice) {
		case 1:
			getByCity(list);
			break;

		case 2:
			getByState(list);
			break;

		default:
			System.out.println("Enter valid choice");
			System.out.println();

		}

	}

	private static void getByState(List<Contacts> list) {

		System.out.print("Enter state : ");
		String searchbystate = sc.next();

		Map<Object, Object> map = list.stream().filter(l -> (l.getState().equals(searchbystate)))
				.collect(Collectors.toMap(l -> l.getFirstname(), l -> l.getState()));
		System.out.println(map + "\n");

	}

	private static void getByCity(List<Contacts> list) {

		System.out.print("Enter city : ");
		String searchbycity = sc.next();

		Map<Object, Object> map = list.stream().filter(l -> (l.getCity().equals(searchbycity)))
				.collect(Collectors.toMap(l -> l.getFirstname(), l -> l.getCity()));
		System.out.println(map + "\n");

	}

	// UC8
	private static void searchContact(List<Contacts> list) {

		System.out.print("Enter city to search : ");
		String city = sc.next();
		List<Contacts> l = list.stream().filter(search -> search.getCity().equals(city)).collect(Collectors.toList());
		System.out.println("Contacts from " + city);
		l.forEach(contacts -> System.out.println(contacts));
		System.out.println();

	}

	// UC5
	private static void addMultipleContacts(List<Contacts> list) {

		System.out.print("Enter count to add contacts : ");
		int count = sc.nextInt();
		boolean record = true;
		while (count > 0) {

			System.out.println("Please enter below contact details...");
			System.out.print("First Name : ");
			String firstname = sc.next();

			record = checkDuplicateEntry(list, firstname);

			if (record) {
				System.out.print("Last Name : ");
				String lastname = sc.next();

				System.out.print("Address : ");
				String address = s1.nextLine();

				System.out.print("City : ");
				String city = sc.next();

				System.out.print("State : ");
				String state = sc.next();

				System.out.print("Zip : ");
				int zip = sc.nextInt();

				System.out.print("Phone : ");
				long phone = sc.nextLong();

				System.out.print("Email : ");
				String email = sc.next();

				list.add(new Contacts(firstname, lastname, address, city, state, zip, phone, email));
				count--;
			}
			if (!record) {
				count = 0;
			}

		}
		if (record) {
			System.out.print("Contacts added...\n");
			list.forEach(contacts -> System.out.println(contacts));
			System.out.println();
		}

	}

	// UC6, UC7
	private static boolean checkDuplicateEntry(List<Contacts> list, String firstname) {
		boolean record = true;
		if (!list.isEmpty()) {
			Iterator<Contacts> iterator = list.iterator();
			while (iterator.hasNext()) {
				Contacts c = iterator.next();
				if (c.getFirstname().equals(firstname)) {
					System.out.println("Contact already exist");
					record = false;
					System.out.println();
				}
			}
		}
		return record;
	}

	// UC4
	private static void deleteContact(List<Contacts> list) {
		boolean record = false;
		System.out.println("Enter name to delete : ");
		String deleterecord = sc.next();
		Iterator<Contacts> iterator = list.iterator();
		while (iterator.hasNext()) {
			Contacts c = iterator.next();
			if (c.getFirstname().equals(deleterecord)) {
				iterator.remove();
				record = true;
			}
		}
		if (record) {
			System.out.println("Contact deleted...");
			list.forEach(contacts -> System.out.println(contacts));
		} else {
			System.out.println("Contact not found");
			System.out.println();
		}

	}

	// UC3
	private static void editContact(List<Contacts> list) {

		boolean record = false;
		Iterator<Contacts> iterator = list.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}

		System.out.print("Enter name to edit : ");
		String firstname = sc.next();

		ListIterator<Contacts> iterator1 = list.listIterator();
		while (iterator1.hasNext()) {
			Contacts c = iterator1.next();
			if (c.getFirstname().equals(firstname)) {

				System.out.print("Last Name : ");
				c.setLastname(sc.next());

				System.out.print("Address : ");
				c.setAddress(sc.next());

				System.out.print("City : ");
				c.setCity(sc.next());

				System.out.print("State : ");
				c.setState(sc.next());

				System.out.print("Zip : ");
				c.setZip(sc.nextInt());

				System.out.print("Phone : ");
				c.setPhoneno(sc.nextLong());

				System.out.print("Email : ");
				c.setEmail(sc.next());

				record = true;

			}

		}
		if (record) {
			System.out.println("Contact updated...");
			list.forEach(contacts -> System.out.println(contacts));
		} else {
			System.out.println("Contact not found \n");
		}
	}

	// UC2
	private static void addContact(List<Contacts> list) {

		System.out.println("Please enter below contact details...");
		System.out.print("First Name : ");
		String firstname = sc.next();

		boolean record = checkDuplicateEntry(list, firstname);

		if (record) {
			System.out.print("Last Name : ");
			String lastname = sc.next();

			System.out.print("Address : ");
			String address = s1.nextLine();

			System.out.print("City : ");
			String city = sc.next();

			System.out.print("State : ");
			String state = sc.next();

			System.out.print("Zip : ");
			int zip = sc.nextInt();

			System.out.print("Phone : ");
			long phone = sc.nextLong();
			long phone1 = 0L;

			// Regex validation
			if (!(Pattern.matches("[1-9]{1}[0-9]{9}", String.valueOf(phone)))) {
				int n = 1;
				while (n > 0) {
					System.out.println("Please enter phone number starting from [1 to 9] and 10 digit");
					System.out.print("Phone : ");
					phone1 = sc.nextLong();
					if ((Pattern.matches("[1-9]{1}[0-9]{9}", String.valueOf(phone1)))) {
						n = 0;
					}
				}
			}
			phone = phone1;

			System.out.print("Email : ");
			String email = sc.next();

			list.add(new Contacts(firstname, lastname, address, city, state, zip, phone, email));
			System.out.println();
			System.out.print("Contact added...\n");
			list.forEach(contacts -> System.out.println(contacts));
			System.out.println();
		}
	}
}
