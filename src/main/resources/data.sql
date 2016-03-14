INSERT INTO company(id, name, address, city, country, email, phone_number) VALUES (1, 'IBM', '#3 Brown St.', 'London', 'UK', 'sales@ibm.com', '+44 432 567 567');
INSERT INTO beneficial_owner(id, name) VALUES (1, 'Beneficial Owner 1');
INSERT INTO beneficial_owner(id, name) VALUES (2, 'Beneficial Owner 2');
INSERT INTO company_beneficial_owners (company_id, beneficial_owners_id) values (1,1);
INSERT INTO company_beneficial_owners (company_id, beneficial_owners_id) values (1,2);

INSERT INTO company(id, name, address, city, country, email, phone_number) VALUES (2, 'GM', '#5 15th St.', 'Detroit', 'USA', 'gm@gm.com', '+1 876 876 876');
INSERT INTO beneficial_owner(id, name) VALUES (3, 'Beneficial Owner 3');
INSERT INTO beneficial_owner(id, name) VALUES (4, 'Beneficial Owner 4');
INSERT INTO company_beneficial_owners (company_id, beneficial_owners_id) values (2,3);
INSERT INTO company_beneficial_owners (company_id, beneficial_owners_id) values (2,4);

INSERT INTO company(id, name, address, city, country, email, phone_number) VALUES (3, 'Apple', '#43 Creative St.', 'San Diego', 'USA', 'apple@apple.com', '+1 345 658 342');
INSERT INTO beneficial_owner(id, name) VALUES (5, 'Beneficial Owner 5');
INSERT INTO beneficial_owner(id, name) VALUES (6, 'Beneficial Owner 6');
INSERT INTO company_beneficial_owners (company_id, beneficial_owners_id) values (3,5);
INSERT INTO company_beneficial_owners (company_id, beneficial_owners_id) values (3,6);

INSERT INTO company(id, name, address, city, country, email, phone_number) VALUES (4, 'HP', '#3 Packard St.', 'Dallas', 'USA', 'sales@hm.com', '+44 432 567 567');