SELECT
     employees.first_name,
     employees.last_name,
     salaries.salary
FROM employees 
INNER JOIN salaries on employees.emp_no = salaries.emp_no
WHERE salaries.from_date between '1996-08-01' and '1997-08-30'
and salaries.to_date between '1996-08-01' and '1997-08-30';