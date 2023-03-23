INSERT INTO conference (id, description, nom, presentateur) VALUES ('e98ff751-6974-4b8a-9561-d6fdb3e1548f', 'Description de la conférence', 'Conférence sur le CI/CD', 'Présentateur 1'),
                                                                   ('f3214ab3-0912-3c21-8712-dacd3203132c', 'Description de la conférence', 'Conférence sur le ML', 'Présentateur 2'),
                                                                   ('c7b8c9d0-0a1b-2c3d-4e5f-6g7h8i9j0k1l', 'Description de la conférence', 'Conférence sur GitHub', 'Présentateur 1');

INSERT INTO session (id, conference_id, date, lieu, nb_places, nb_places_restantes, prix) VALUES ('e109e6d7-1234-987e-120a-789d8c7d7d6a', 'e98ff751-6974-4b8a-9561-d6fdb3e1548f', '2023-04-01 20:30:00', 'Paris', 50, 40, 10.0),
                                                                                                                 ('a3455b3e-4934-405f-a837-98e2eea8efa6', 'e98ff751-6974-4b8a-9561-d6fdb3e1548f', '2023-04-06 19:00:00', 'Londres', 100, 50, 10.0),

                                                                                                                 ('8f7ae5ed-7dbe-4011-8636-e41c8d5ddf3a', 'f3214ab3-0912-3c21-8712-dacd3203132c', '2023-04-01 16:30:00', 'Rio', 50, 43, 10.0),
                                                                                                                 ('2a551f53-fe2d-4937-bbb0-0de8e28a5b4b', 'f3214ab3-0912-3c21-8712-dacd3203132c', '2023-04-06 21:00:00', 'Lille', 40, 35, 15.0),

                                                                                                                 ('20d877ad-2c0c-4e05-a6c5-b5cb9909d460', 'c7b8c9d0-0a1b-2c3d-4e5f-6g7h8i9j0k1l', '2023-04-11 19:30:00', 'Quebec', 50, 40, 10.0);

INSERT INTO reservation (id, conference_id, session_id, user_id, nb_places, payee, annulee) VALUES ('9b565c7e-1763-4391-bf38-36d2c63939af', 'e98ff751-6974-4b8a-9561-d6fdb3e1548f', 'e109e6d7-1234-987e-120a-789d8c7d7d6a', 'jean@bon.fr', 2, true, false),
                                                                                                                   ('fe48eb5d-cd19-4e9f-b167-02f26d18b916', 'e98ff751-6974-4b8a-9561-d6fdb3e1548f', 'e109e6d7-1234-987e-120a-789d8c7d7d6a', 'marc@unbut.fr', 2, false, false),
                                                                                                                   ('cdfc303c-4a60-4308-9bc5-bac49ccf2eb2', 'e98ff751-6974-4b8a-9561-d6fdb3e1548f', 'e109e6d7-1234-987e-120a-789d8c7d7d6a', 'hal@Opera.com', 1, false, false),
                                                                                                                   ('ecf1ee2b-bc4f-4727-8e9f-806d7ff2abc4', 'e98ff751-6974-4b8a-9561-d6fdb3e1548f', 'e109e6d7-1234-987e-120a-789d8c7d7d6a', 'sarah@croche.fr', 5, true, false),
                                                                                                                   ('ed0fd0ee-a600-4d08-89bb-c97934b7df56', 'e98ff751-6974-4b8a-9561-d6fdb3e1548f', 'e109e6d7-1234-987e-120a-789d8c7d7d6a', 'aqua@poney.asso', 10, false, true),

                                                                                                                   ('bce8bdf5-b921-4056-ab25-e0398be7ea86', 'e98ff751-6974-4b8a-9561-d6fdb3e1548f', 'a3455b3e-4934-405f-a837-98e2eea8efa6', 'idmc@univ.fr', 50, true, false),

                                                                                                                   ('e261d722-027a-4abf-9c98-68f1e6373fa1', 'f3214ab3-0912-3c21-8712-dacd3203132c', '8f7ae5ed-7dbe-4011-8636-e41c8d5ddf3a', 'marc@unbut.fr', 2, false, false),
                                                                                                                   ('37d4cdcc-86e4-45e2-b496-4fa4b43e0644', 'f3214ab3-0912-3c21-8712-dacd3203132c', '8f7ae5ed-7dbe-4011-8636-e41c8d5ddf3a', 'sarah@croche.fr', 5, true, false),

                                                                                                                   ('44a2c0db-e4fc-4b9f-8021-f5cf370ea6f0', 'f3214ab3-0912-3c21-8712-dacd3203132c', '2a551f53-fe2d-4937-bbb0-0de8e28a5b4b', 'sarah@croche.fr', 5, true, false),

                                                                                                                   ('6f4776cb-cc04-4c33-a1ad-320e6205e3d3', 'c7b8c9d0-0a1b-2c3d-4e5f-6g7h8i9j0k1l', '20d877ad-2c0c-4e05-a6c5-b5cb9909d460', 'hal@Opera.com', 10, true, false);