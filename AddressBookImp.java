package com.addressbook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AddressBookImp {
	static Scanner sc = new Scanner(System.in);
	static Scanner s1 = new Scanner(System.in);
	static List<Contacts> list = new ArrayList<>();

	public static void getAddressBook() {
		boolean n = true;

		while (n) {
			System.out.println("1. Add contact");
			System.out.println("2. Edit contact");
			System.out.println("3. Exit \n");

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
				n = false;
				System.out.println("exit successfull...");
				break;

			default:
				System.out.println("Enter valid choice \n");

			}
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
