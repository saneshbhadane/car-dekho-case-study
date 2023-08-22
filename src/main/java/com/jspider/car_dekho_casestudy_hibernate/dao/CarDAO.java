package com.jspider.car_dekho_casestudy_hibernate.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.jspider.car_dekho_casestudy_hibernate.dto.Car;

public class CarDAO implements CarOpreation {

	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;
	private static EntityTransaction entityTransaction;
	private static Scanner sc = new Scanner(System.in);

	private static void openConnection() {
		entityManagerFactory = Persistence.createEntityManagerFactory("connect");
		entityManager = entityManagerFactory.createEntityManager();
		entityTransaction = entityManager.getTransaction();
	}

	private static void closeConnection() {
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
		}
		if (entityManager != null) {
			entityManager.close();
		}
		if (entityTransaction != null) {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
		}
	}

	public void addCar() {

		try {
			openConnection();
			entityTransaction.begin();
			Car car = new Car();
			List<Car> cars = getCars();
			ArrayList<Integer> ids = new ArrayList<>();
			for (Car car1 : cars) {
				ids.add(car1.getId());
			}
			System.out.println("Car id already taken..!");
			System.out.println(ids);

			System.out.println("How many cars you would want to add ?");
			int n = sc.nextInt();

			for (int i = 1; i <= n; i++) {

				System.out.println("Enter the Car id : ");
				car.setId(sc.nextInt());
				System.out.println("Enter the Car Name :");
				car.setName(sc.next());
				System.out.println("Enter the Car Model :");
				car.setModel(sc.next());
				System.out.println("Enter the Brand Of the Car : ");
				car.setBrand(sc.next());
				System.out.println("Enter the Fule type of Car : ");
				car.setFuletype(sc.next());
				System.out.println("Enter the Price : ");
				car.setPrice(sc.nextDouble());

				entityManager.persist(car);

				System.out.println("Car Added..!");
				entityTransaction.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();

		}
	}

	private void availableCar() {

		try {

			openConnection();
			Query query = entityManager.createQuery("select car from Car car");
			@SuppressWarnings("unchecked")
			List<Car> cars = query.getResultList();

			System.out.println(
					"======================================Avaibale Cars======================================");
			System.out.println("CarId   \tCarName  \tCarModel  \tCarBrand  \tCarFuleType  \tCarPrice");

			for (Car car : cars) {

				System.out.println(car);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			closeConnection();
		}

	}

	private boolean isCarAvailable() {
		int count = 0;
		try {
			openConnection();
			entityTransaction.begin();
			Query query = entityManager.createQuery("select car from Car car");
			@SuppressWarnings("unchecked")
			List<Car> cars = query.getResultList();
			count = cars.size();
			entityTransaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		return count > 0;
	}

	@Override
	public void deleteCar() {

		if (isCarAvailable()) {

			availableCar();

			try {

				openConnection();

				entityTransaction.begin();

				System.out.println("Enter the Car id to delete the Car details");
				int c_id = sc.nextInt();
				Car car = entityManager.find(Car.class, c_id);
				if (car != null) {
					entityManager.remove(car);
					System.out.println("Car Details Deleted ...!");
				} else {
					System.out.println("Car Details not Found..!");
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				entityTransaction.commit();
				closeConnection();
			}

		} else {
			System.out.println("Cars Not Avaibales..!");
		}

	}

	private void display(List<Car> list) {
		if (list.size() != 0) {
			System.out.println(
					"======================================Avaibale Cars Result======================================");
			System.out.println("CarId   \tCarName  \tCarModel  \tCarBrand  \tCarFuleType  \tCarPrice");

			for (Car car : list) {
				System.out.println(car);
			}
		} else {
			System.out.println("Cars Not Avaibales..!");
		}
	}

	private List<Car> getCars() {

		List<Car> cars1 = new ArrayList<>();
		try {
			openConnection();
			Query query = entityManager.createQuery("select car from Car car");
			@SuppressWarnings("unchecked")
			List<Car> cars = query.getResultList();
			for (Car car : cars) {
				cars1.add(car);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cars1;
	}

	@Override
	public void searchCar() {

		if (isCarAvailable()) {

			try {
				openConnection();

				List<Car> cars = getCars();
				List<Car> result = new ArrayList<>();
				System.out.println("=========Serach By=========");
				System.out.println("1.Name \n2.Brand \n3.Fule Type \n4.Go Back");
				System.out.println("Enter the valid choice..!");

				switch (sc.nextInt()) {

				case 1:
					System.out.println("Enter the Car name..!");
					String name = sc.next();

					entityTransaction.begin();

					for (Car car : cars) {
						if (car.getName().equalsIgnoreCase(name)) {
							result.add(car);
						}
					}

					display(result);

					entityTransaction.commit();
					break;
				case 2:
					System.out.println("Enter the Car brand..!");
					String brand = sc.next();
					entityTransaction.begin();

					for (Car car : cars) {
						if (car.getBrand().equalsIgnoreCase(brand)) {
							result.add(car);
						}
					}
					display(result);
					entityTransaction.commit();
					break;
				case 3:
					System.out.println("Enter the Car Fuletype..!");
					String fuletype = sc.next();
					entityTransaction.begin();
					for (Car car : cars) {
						if (car.getFuletype().equalsIgnoreCase(fuletype)) {
							result.add(car);
						}
					}
					display(result);
					entityTransaction.commit();
					break;
				case 4:
					return;
				default:
					System.out.println("Enter the valid choice..!");

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				closeConnection();
			}

		} else {
			System.out.println("Cars Not Avaibales..!");
		}

	}

	@Override
	public void updateCar() {

		if (isCarAvailable()) {

			availableCar();

			try {
				openConnection();
				System.out.println("Enter the Car id to update Car details..!");
				int c_id = sc.nextInt();

				System.out.println("What would you like to update..!");
				System.out.println("1.Name \n2.Model \n3.Brand \n4.Fule Type \n5.Price");
				switch (sc.nextInt()) {
				case 1:
					System.out.println("Enter the name");
					String name = sc.next();
					entityTransaction.begin();
					Car car1 = entityManager.find(Car.class, c_id);
					car1.setName(name);
					entityManager.persist(car1);
					entityTransaction.commit();

					System.out.println("Name Updated..!");
					break;
				case 2:
					System.out.println("Enter the Model");
					String model = sc.next();
					entityTransaction.begin();
					Car car2 = entityManager.find(Car.class, c_id);
					car2.setModel(model);
					entityManager.persist(car2);
					entityTransaction.commit();
					System.out.println("Model Updated..!");
					break;
				case 3:
					System.out.println("Enter the Brand");
					String brand = sc.next();
					entityTransaction.begin();
					Car car3 = entityManager.find(Car.class, c_id);
					car3.setBrand(brand);
					entityManager.persist(car3);
					entityTransaction.commit();
					System.out.println("Brand Updated..!");
					break;
				case 4:
					System.out.println("Enter the Fule Type");
					String fuletype = sc.next();
					entityTransaction.begin();
					Car car4 = entityManager.find(Car.class, c_id);
					car4.setFuletype(fuletype);
					entityManager.persist(car4);
					entityTransaction.commit();
					System.out.println("Fule Type Updated..!");
					break;
				case 5:
					System.out.println("Enter the Price");
					Double price = sc.nextDouble();
					entityTransaction.begin();
					Car car5 = entityManager.find(Car.class, c_id);
					car5.setPrice(price);
					entityManager.persist(car5);
					entityTransaction.commit();
					System.out.println("Price Updated..!");
					break;
				default:
					System.out.println("Enter the valid choice..!");
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

				closeConnection();
			}

		} else {
			System.out.println("Cars Not Avaibales..!");

		}

	}

}
