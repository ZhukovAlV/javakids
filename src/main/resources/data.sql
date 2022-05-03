INSERT INTO user_table (id, admin, username, email, password, locked)
SELECT 1,true,'admin','admin@javakids.ru','A+98FnMIylJGjl2jhHcUYQ==',false
WHERE NOT EXISTS (SELECT id
	FROM public.user_table
	WHERE username = 'admin' LIMIT 1);

INSERT INTO user_table (id, admin, username, email, password, locked)
SELECT 2,false,'user','user@javakids.ru','A+98FnMIylJGjl2jhHcUYQ==',false
WHERE NOT EXISTS (SELECT id
	FROM public.user_table
	WHERE username = 'user' LIMIT 1);
