INSERT INTO FIX_SERVER (FIX_SERVER_UID, NAME) VALUES (1, 'FIXSERVER1');

INSERT INTO FIX_SESSION (FIX_SESSION_UID, FIX_SERVER_UID, CONNECTION_TYPE, BEGIN_STRING, SENDER_COMP_ID, TARGET_COMP_ID, HOST, PORT) VALUES (1, 1, 'ACCEPTOR', 'FIX.4.4', 'SHARPROUTE', 'CLIENT1', 'LOCALHOST', 10001);
INSERT INTO FIX_SESSION (FIX_SESSION_UID, FIX_SERVER_UID, CONNECTION_TYPE, BEGIN_STRING, SENDER_COMP_ID, TARGET_COMP_ID, HOST, PORT) VALUES (2, 1, 'ACCEPTOR', 'FIX.4.4', 'SHARPROUTE', 'CLIENT2', 'LOCALHOST', 10002);
INSERT INTO FIX_SESSION (FIX_SESSION_UID, FIX_SERVER_UID, CONNECTION_TYPE, BEGIN_STRING, SENDER_COMP_ID, TARGET_COMP_ID, HOST, PORT) VALUES (3, 1, 'ACCEPTOR', 'FIX.4.4', 'SHARPROUTE', 'CLIENT3', 'LOCALHOST', 10003);