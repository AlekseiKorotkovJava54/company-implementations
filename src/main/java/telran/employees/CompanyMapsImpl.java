package telran.employees;

import java.util.*;
//So far we do consider optimization
public class CompanyMapsImpl implements Company {
	
	TreeMap<Long, Employee> employees = new TreeMap<>();
	HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
	TreeMap<Float, List<Manager>> factorManagers = new TreeMap<>();
	
	@Override
	public Iterator<Employee> iterator() {
		return employees.values().iterator();
	}

	@Override
	public void addEmployee(Employee empl) {
		if(employees.containsKey(empl.getId())) throw new IllegalStateException();
		else {
			employees.put(empl.getId(), empl);
			employeesDepartment.computeIfAbsent(empl.getDepartment(), k -> new ArrayList<>()).add(empl);
			if(empl instanceof Manager) 
				factorManagers.computeIfAbsent(((Manager) empl).factor, k -> new ArrayList<>()).add((Manager) empl);
		}
	}

	@Override
	public Employee getEmployee(long id) {
		return employees.get(id);
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee removedEmployee = employees.remove(id);
		if(removedEmployee != null) {
			removeEmployeeFromDepartment(removedEmployee);
			if(removedEmployee instanceof Manager) removeEmployeeFromFactorManagers(removedEmployee);
			
		} else throw new NoSuchElementException();
		return removedEmployee;
	}

	private void removeEmployeeFromFactorManagers(Employee removedEmployee) {
		float factor = ((Manager) removedEmployee).factor;
		List<Manager> managersList = factorManagers.get(factor);
		managersList.remove(removedEmployee);
		if(managersList.isEmpty()) factorManagers.remove(factor);
	}

	private void removeEmployeeFromDepartment(Employee removedEmployee) {
		String department = removedEmployee.getDepartment();
		List<Employee> listEmployeeFromDepartment = employeesDepartment.get(department);
		listEmployeeFromDepartment.remove(removedEmployee);
		if(listEmployeeFromDepartment.isEmpty()) employeesDepartment.remove(department);
	}

	@Override
	public int getDepartmentBudget(String department) {
		int res = 0;
		if (employeesDepartment.containsKey(department))
			res = employeesDepartment.get(department).stream().filter(e -> e != null).mapToInt(n -> n.computeSalary()).sum();
		return res;
	}

	@Override
	public String[] getDepartments() {
		return employeesDepartment.keySet().stream().sorted().toArray(String[]::new);
	}

	@Override
	public Manager[] getManagersWithMostFactor() {
		return factorManagers.lastEntry().getValue().toArray(Manager[]::new);
	}
}
