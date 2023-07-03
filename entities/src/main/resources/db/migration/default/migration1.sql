CREATE TABLE public.user (
                        "id" SERIAL PRIMARY KEY,
                        "name" CHARACTER VARYING(255),
                        "email" CHARACTER VARYING(255) NOT NULL,
                        "password" CHARACTER VARYING(255) NOT NULL,
                        "type" CHARACTER VARYING(255) NOT NULL,
                        "uuid" CHARACTER VARYING(255) NOT NULL,
                        "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        "updated_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE public.attendance (
                            id SERIAL PRIMARY KEY,
                            user_id BIGINT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE
);
