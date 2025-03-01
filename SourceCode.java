import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MySQLAWTCRUDExample extends Frame {

	private TextField empIdField, nameField, salaryField, locationField;
	private TextArea resultArea;

	private Connection connection;

	public MySQLAWTCRUDExample() {
		// Establish database connection
		try {
			String url = "jdbc:mysql://localhost:3306/shravani";
			String user = "root";
			String password = "1234";
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to the database!");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Frame settings
		setTitle("Employee Management System Using AWT");
		setSize(600, 400);
		setLayout(new FlowLayout());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					if (connection != null) connection.close();
					System.out.println("Connection closed.");
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				System.exit(0);
			}
		});

		// Components
		add(new Label("Employee ID:"));
		empIdField = new TextField(10);
		add(empIdField);

		add(new Label("Name:"));
		nameField = new TextField(15);
		add(nameField);

		add(new Label("Salary:"));
		salaryField = new TextField(10);
		add(salaryField);

		add(new Label("Location:"));
		locationField = new TextField(10);
		add(locationField);

		Button createButton = new Button("Create");
		Button readButton = new Button("Read All");
		Button updateButton = new Button("Update");
		Button deleteButton = new Button("Delete");
		Button searchButton = new Button("Search");
		Button clearButton = new Button("Clear Fields"); // New Button

		add(createButton);
		add(readButton);
		add(updateButton);
		add(deleteButton);
		add(searchButton);
		add(clearButton);

		resultArea = new TextArea(10, 50);
		resultArea.setEditable(false);
		add(resultArea);

		// Action listeners
		createButton.addActionListener(e -> createRecord());
		readButton.addActionListener(e -> readRecords());
		updateButton.addActionListener(e -> updateRecord());
		deleteButton.addActionListener(e -> deleteRecord());
		searchButton.addActionListener(e -> searchRecord());
		clearButton.addActionListener(e -> clearFields()); // Clear Fields Action

		setVisible(true);
	}

	private void clearFields() {
		empIdField.setText("");
		nameField.setText("");
		salaryField.setText("");
		locationField.setText("");
		resultArea.setText("");
	}

	private void createRecord() {
		try {
			String query = "INSERT INTO s_emp (emp_id, name, salary, location) VALUES (?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(empIdField.getText()));
			preparedStatement.setString(2, nameField.getText());
			preparedStatement.setInt(3, Integer.parseInt(salaryField.getText()));
			preparedStatement.setString(4, locationField.getText());

			int rows = preparedStatement.executeUpdate();
			resultArea.setText(rows + " record(s) inserted.");
		} catch (SQLException e) {
			resultArea.setText("Error: " + e.getMessage());
		}
	}

	private void readRecords() {
		try {
			String query = "SELECT * FROM s_emp";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			StringBuilder results = new StringBuilder("Employee Details:\nID\tName\tSalary\tLocation\n");
			while (resultSet.next()) {
				results.append(resultSet.getInt("emp_id")).append("\t")
						.append(resultSet.getString("name")).append("\t")
						.append(resultSet.getInt("salary")).append("\t")
						.append(resultSet.getString("location")).append("\n");
			}
			resultArea.setText(results.toString());
		} catch (SQLException e) {
			resultArea.setText("Error: " + e.getMessage());
		}
	}

	private void updateRecord() {
		try {
			String query = "UPDATE s_emp SET salary = ?, location = ? WHERE emp_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(salaryField.getText()));
			preparedStatement.setString(2, locationField.getText());
			preparedStatement.setInt(3, Integer.parseInt(empIdField.getText()));

			int rows = preparedStatement.executeUpdate();
			resultArea.setText(rows + " record(s) updated.");
		} catch (SQLException e) {
			resultArea.setText("Error: " + e.getMessage());
		}
	}

	private void deleteRecord() {
		try {
			String query = "DELETE FROM s_emp WHERE emp_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(empIdField.getText()));

			int rows = preparedStatement.executeUpdate();
			resultArea.setText(rows + " record(s) deleted.");
		} catch (SQLException e) {
			resultArea.setText("Error: " + e.getMessage());
		}
	}

	private void searchRecord() {
		try {
			String query = "SELECT * FROM s_emp WHERE emp_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(empIdField.getText()));

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				resultArea.setText("Employee Details:\nID\tName\tSalary\tLocation\n");
				resultArea.append(resultSet.getInt("emp_id") + "\t"
						+ resultSet.getString("name") + "\t"
						+ resultSet.getInt("salary") + "\t"
						+ resultSet.getString("location"));
			} else {
				resultArea.setText("No employee found with the given ID.");
			}
		} catch (SQLException e) {
			resultArea.setText("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		new MySQLAWTCRUDExample();
	}
}