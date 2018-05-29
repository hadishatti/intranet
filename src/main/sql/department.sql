INSERT INTO intranet.department(id, name, companyID, parentId, secondParentId, isParentEmployee, isSecondParentEmployee, chartId, color)
VALUES
(1,'Sales & Business Development',1, 3, NULL, 1, 0, 53, 'darkblue'),
(2,'Technical', 1, 3, NULL, 1, 0, 54, 'darkblue'),
(3,'System Integration', 1,  3, NULL, 1, 0, 55, 'darkblue'),
(4,'Projects', 1,  3, NULL, 1, 0, 56, 'darkblue'),
(5, 'Operation & Maintenance', 1,  3, NULL, 1, 0, 57, 'darkblue'),
(6, 'Civil', 1,  3, NULL, 1, 0, 58, 'darkblue'),
(7, 'Accounting', 1,  4, NULL, 1, 0, 59, 'cordovan'),
(8, 'Procurement', 1,  4, NULL, 1, 0, 60, 'cordovan'),
(9, 'Administration & Human Resources', 1,  4, NULL, 1, 0, 61, 'cordovan'),
(10, 'Logistic', 1,  4, NULL, 1, 0, 62, 'cordovan'),
(11, 'Quality, Health, Safety, Environment', 1,  4, NULL, 1, 0, 63, 'cordovan'),
(12, 'Marketing', 1, 4, NULL, 1, 0, 64, 'cordovan'),
(13, 'Tunnel Control Room Operation', 1,  30, NULL, 1, 0, 65, 'cordovan'),
(14, 'Tunnel System Maintenance', 1,  30, NULL, 1, 0, 66, 'cordovan')
;