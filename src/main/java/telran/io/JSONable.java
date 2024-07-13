package telran.io;

import telran.employees.Employee;

public interface JSONable {
String getJSON();
Employee setObject(String json);
}
