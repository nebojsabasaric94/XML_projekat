insert into national_bank (common_name, country, email, organization, organization_unit) values ('NBS','Srbija','nbs@nbs','org','orgU');

insert into bank(bank_code,pib,name,address,email,web,phone,fax,obracunski_racun_banke,swift_kod_banke,stanje_racuna_banke) values ('BAC','asdfggfdsa','uni credit','glavna','uni@gmail.com','web','4892810','fax','200123456123141124','BACXRSBG',0);
insert into bank(bank_code,pib,name,address,email,web,phone,fax,obracunski_racun_banke,swift_kod_banke,stanje_racuna_banke) values ('GIB','pooppooppo','erste','bulevar','erste@gmail.com','web','4892810','fax','300124510974841158','GIBARS22',0);
insert into bank(bank_code,pib,name,address,email,web,phone,fax,obracunski_racun_banke,swift_kod_banke,stanje_racuna_banke) values ('DBD','intesansns','intesa','glavna','intesa@gmail.com','web','4892810','fax','400123456321454147','DBDBRSBG',0);

insert into firma(name,address,city,country,email,phone,web,bank_id,pib_firm,stanje_racuna,broj_racuna) values ('levi','trifkovicev trg','Novi Sad','Srbija','levi@levi.com','012656','web',1,'asdfggfdsaa',0,'200636547896321456');
insert into firma(name,address,city,country,email,phone,web,bank_id,pib_firm,stanje_racuna,broj_racuna) values ('vega','trifkovicev trg','Novi Sad','Srbija','vega@vega.com','012656','web',1,'asdfggfdsas',0,'200854578787874202');
insert into firma(name,address,city,country,email,phone,web,bank_id,pib_firm,stanje_racuna,broj_racuna) values ('dms','trifkovicev trg','Novi Sad','Srbija','dms@dms.com','012656','web',2,'asdfggfdsrr',0,'300874521693695451');
insert into firma(name,address,city,country,email,phone,web,bank_id,pib_firm,stanje_racuna,broj_racuna) values ('ftn','bulevar','Novi Sad','Srbija','ftn@uns.com','012656','web',3,'asdfggfdsea',0,'400254547845612156');


insert into user (username, password, email) values ('dachakg','$2a$10$bdLAFEAmvgWUApO.uKWqvOjVDlN7riKyB/d0/4w.9e.aeTF1Z3KQ.','a@s.com');
insert into user (username, password, email, firma_id) values ('dachakg1','$2a$10$NGxm0C0hF28mYpmISxkrEe0JLMf8/2CsHBq4czTP9Rq9DX9AhEgYe','s@a.org',1);
insert into user (username, password, email, firma_id) values ('dachakg2','$2a$10$NGxm0C0hF28mYpmISxkrEe0JLMf8/2CsHBq4czTP9Rq9DX9AhEgYe','p@b.com',2);
insert into user (username, password, email, firma_id) values ('milance','$2a$10$LeQbMB55qkJYitFFGe2j3.uqe/1nrduf9ZRb8F6zSzg5n3GizOvw.','choda.94@gmail.com',3);
insert into user (username, password, email) values ('duca','$2a$10$UteruuYLEll8yHbjCM7Q8OhIvB0EIzT9ErS7Wqqltj1gk37qEHkpW','ed@c.com');
insert into user (username, password, email, firma_id) values ('rada', '$2a$10$YQefMKcyoC1LNTPoqXRreOSvJvs.ytC857rlHHn5MjE0DqQluxnqi', 'rada@r.com',4);

insert into certificate (povucen, serial_number) values (0, '1497911438');
insert into certificate (povucen, serial_number) values (0, '1497911492');
insert into certificate (povucen, serial_number) values (0, '1497911614');


insert into role (enum_role) values ('ROLE_ADMIN');
insert into role (enum_role) values ('ROLE_USER');
insert into role (enum_role) values ('ROLE_BANKER');
insert into role (enum_role) values ('ROLE_FIRM');


insert into users_roles(user_id, role_id) values (3,2);
insert into users_roles(user_id, role_id) values (1,3);
insert into users_roles(user_id, role_id) values (4,2);
insert into users_roles(user_id, role_id) values (5,1);
insert into users_roles(user_id, role_id) values (6,4);
insert into users_roles(user_id, role_id) values (2,4);


insert into privilege (privilege) values ('addCertificate');
insert into privilege (privilege) values ('revokeCertificate');
insert into privilege (privilege) values ('registerUser');
insert into privilege (privilege) values ('addCaSignedCertificate');
insert into privilege (privilege) values ('sendInvoice');
insert into privilege (privilege) values ('createCSR');
insert into privilege (privilege) values ('signCSR');
insert into privilege (privilege) values ('activateUser');




insert into roles_privileges (role_id, privilege_id) values (1,1);
insert into roles_privileges (role_id, privilege_id) values (1,2);
insert into roles_privileges (role_id, privilege_id) values (3,3);
insert into roles_privileges (role_id, privilege_id) values (1,4);
insert into roles_privileges (role_id, privilege_id) values (4,5);
insert into roles_privileges (role_id, privilege_id) values (2,5);
insert into roles_privileges (role_id, privilege_id) values (2,6);
insert into roles_privileges (role_id, privilege_id) values (4,6);
insert into roles_privileges (role_id, privilege_id) values (3,7);
insert into roles_privileges (role_id, privilege_id) values (1,8);



insert into faktura(adresa_dobavljaca, adresa_kupca, broj_racuna, datum_racuna, datum_valute, iznos_za_uplatu, naziv_dobavljaca, naziv_kupca, obradjena, oznaka_valute, pib_dobavljaca, pib_kupca, ukupan_porez, ukupan_rabat, ukupno_robaiusluge, uplata_na_racun, vrednost_robe, vrednost_usluga, firma_id) values ('bulevar', 'trifkovicev trg', '123321123147852369', '2017-06-12 02:00:00', '2017-06-12 02:00:00', '7', 'ftn', 'levi', false, '7', 'asdfggfdsea', 'asdfggfdsaa', '7', '7', '77', '789654123369852147', '312312', '3212', '1')

insert into stavka_fakture(iznos_rabata, jedinica_mere, jedinicna_cena, kolicina, naziv_robe_ili_usluge, procena_rabata, redni_broj, ukupan_porez_stavka, umanjeno_za_rabat, vrednost, faktura_id) values ('5', '5', '5', '6', '5', '5', '141', '7', '5', '5', 1);
