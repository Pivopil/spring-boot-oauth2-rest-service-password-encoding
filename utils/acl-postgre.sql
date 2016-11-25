create table acl_sid(
	id bigserial not null primary key,
	principal boolean not null,
	sid varchar(100) not null,
	constraint unique_uk_1 unique(sid,principal)
);

create table acl_class(
	id bigserial not null primary key,
	class varchar(100) not null,
	constraint unique_uk_2 unique(class)
);

create table acl_object_identity(
	id bigserial primary key,
	object_id_class bigint not null,
	object_id_identity bigint not null,
	parent_object bigint,
	owner_sid bigint,
	entries_inheriting boolean not null,
	constraint unique_uk_3 unique(object_id_class,object_id_identity),
	constraint foreign_fk_1 foreign key(parent_object)references acl_object_identity(id),
	constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
	constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id)
);

create table acl_entry(
	id bigserial primary key,
	acl_object_identity bigint not null,
	ace_order int not null,
	sid bigint not null,
	mask integer not null,
	granting boolean not null,
	audit_success boolean not null,
	audit_failure boolean not null,
	constraint unique_uk_4 unique(acl_object_identity,ace_order),
	constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
	constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);




INSERT INTO acl_sid (id, principal, sid) VALUES
(1, true, 'admin'),
(2, true, 'user'),
(3, true, 'visitor');

INSERT INTO acl_class (id, class) VALUES
(1, 'io.github.pivopil.share.entities.impl.domain.AdminPost'),
(2, 'io.github.pivopil.share.entities.impl.domain.PersonalPost'),
(3, 'io.github.pivopil.share.entities.impl.domain.PublicPost');


INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 1, false),
(2, 1, 2, NULL, 1, false),
(3, 1, 3, NULL, 1, false),
(4, 2, 1, NULL, 1, false),
(5, 2, 2, NULL, 1, false),
(6, 2, 3, NULL, 1, false),
(7, 3, 1, NULL, 1, false),
(8, 3, 2, NULL, 1, false),
(9, 3, 3, NULL, 1, false);


INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
(1, 1, 1, 1, 1, true, true, true),
(2, 2, 1, 1, 1, true, true, true),
(3, 3, 1, 1, 1, true, true, true),
(4, 1, 2, 1, 2, true, true, true),
(5, 2, 2, 1, 2, true, true, true),
(6, 3, 2, 1, 2, true, true, true),
(7, 4, 1, 1, 1, true, true, true),
(8, 5, 1, 1, 1, true, true, true),
(9, 6, 1, 1, 1, true, true, true),
(10, 7, 1, 1, 1, true, true, true),
(11, 8, 1, 1, 1, true, true, true),
(12, 9, 1, 1, 1, true, true, true),
(13, 7, 2, 1, 2, true, true, true),
(14, 8, 2, 1, 2, true, true, true),
(15, 9, 2, 1, 2, true, true, true),
(28, 4, 3, 2, 1, true, true, true),
(29, 5, 3, 2, 1, true, true, true),
(30, 6, 3, 2, 1, true, true, true),
(31, 4, 4, 2, 2, true, true, true),
(32, 5, 4, 2, 2, true, true, true),
(33, 6, 4, 2, 2, true, true, true),
(34, 7, 3, 2, 1, true, true, true),
(35, 8, 3, 2, 1, true, true, true),
(36, 9, 3, 2, 1, true, true, true),
(37, 7, 4, 2, 2, true, true, true),
(38, 8, 4, 2, 2, true, true, true),
(39, 9, 4, 2, 2, true, true, true),
(40, 7, 5, 3, 1, true, true, true),
(41, 8, 5, 3, 1, true, true, true),
(42, 9, 5, 3, 1, true, true, true);

INSERT INTO admin_post (id, title) VALUES
(1,  'Custom post #1 from admin'),
(2,  'Custom post #2 from admin'),
(3,  'Custom post #3 from admin');


INSERT INTO personal_post (id, title) VALUES
(1, 'Custom post #1 from user'),
(2,  'Custom post #2 from user'),
(3,  'Custom post #3 from user');


INSERT INTO public_post (id, title) VALUES
(1,  'Custom post #1 from public'),
(2,  'Custom post #2 from public'),
(3,  'Custom post #3 from public');
