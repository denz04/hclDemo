CREATE TABLE user_info (
        id UUID NOT NULL DEFAULT uuid_generate_v1() PRIMARY KEY,
        username TEXT NOT NULL UNIQUE,
        password TEXT NOT NULL,
        firstName TEXT NOT NULL,
        lastName TEXT NOT NULL,
        civilId TEXT NOT NULL,
        email TEXT ,
        phoneNumber TEXT NOT NULL,
        isVerified BOOLEAN NOT NULL DEFAULT FALSE,
        address TEXT,
        createdAt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        lastLoginTimestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
